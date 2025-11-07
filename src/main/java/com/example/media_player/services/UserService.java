package com.example.media_player.services;

import com.example.media_player.dtos.MediaDto;
import com.example.media_player.entities.UserMedia;
import com.example.media_player.repositories.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpStatus;
//import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;

@Service
public class UserService implements UserServiceInterface {

    private final UserRepository userRepository;
    private final RestClient restClient;
    private final LoadBalancerClient loadBalancerClient;

    @Autowired
    public UserService(UserRepository userRepository, RestClient.Builder restClientBuilder, LoadBalancerClient loadBalancerClient) {
        this.userRepository = userRepository;
        this.restClient = restClientBuilder.build();
        this.loadBalancerClient = loadBalancerClient;
    }

    @Override
    public MediaDto playMedia(Long id) { // add jwt later on

        String userId = "TESTSUB";

        // Fetching media to play from media-handling service
        MediaDto playedMedia = fetchMediaById(id);

        // fetch existing history or create a new one
        UserMedia userMedia = userRepository
                .findByUserIdAndMediaId(userId, playedMedia.getMediaId())
                .orElseGet(() -> {
                    UserMedia newUserMedia = new UserMedia();
                    newUserMedia.setUserId(userId);
                    newUserMedia.setMediaId(playedMedia.getMediaId());
                    newUserMedia.setMediaName(playedMedia.getMediaName());
//                    newUserMedia.setGenre(playedMedia.getGenres()); // Choose one genre or add a new entity representing genre?
                    newUserMedia.setPlayCount(0L);
                    return newUserMedia;
                });

        userMedia.setPlayCount(userMedia.getPlayCount() + 1);

        userRepository.save(userMedia);

        return playedMedia;
    }

    @Override
    public List<UserMedia> getAllPlayedMedia() { // add authentication user later on

        String userId = "TESTSUB";

        List<UserMedia> allPlayedMedia = userRepository.findUserMediaByUserId(userId);

        return allPlayedMedia;
    }

    @Override
    public List<UserMedia> getMostPlayedMedia() { //add authentication user later on... Jwt jwt

        String userId = "TESTSUB"; // The jwt-token sub should be stored here. String userId = jwt.getSubject()

        List<UserMedia> allPlayed = userRepository.findUserMediaByUserId(userId);

        long maxCount = allPlayed.stream().map(UserMedia::getPlayCount).max(Long::compareTo).orElse(0L);

        List<UserMedia> mostPlayed = allPlayed.stream().filter(userMedia -> userMedia.getPlayCount() == maxCount).toList();

//        UserMedia mostPlayed = userRepository.findTopByUserIdOrderByPlayCountDesc(userId)
//                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No media played yet"));

//        MediaDto mostPlayedMedia = fetchMediaById(mostPlayed.getMediaId());

        return mostPlayed;
    }

    // Can only like/dislike media that a user has listened to, fetching from media-player database.
    @Override
    public UserMedia likeDislikeMedia(Long id, String likeDislike) {  //add authentication user later on.... jwt jwt

        String userId = "TESTSUB"; // for testing before keycloak

        UserMedia mediaToLikeOrDislike = userRepository.findByUserIdAndMediaId(userId, id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "No media played yet"));

        if(likeDislike.equals("like")) {
            mediaToLikeOrDislike.setLikedMedia(true);
            mediaToLikeOrDislike.setDislikedMedia(false);
        }
        else if(likeDislike.equals("dislike")) {
            mediaToLikeOrDislike.setLikedMedia(false);
            mediaToLikeOrDislike.setDislikedMedia(true);
        }

        userRepository.save(mediaToLikeOrDislike);

        return mediaToLikeOrDislike;
    }

    // Get a list of media that a user has liked/disliked
    @Override
    public List<UserMedia> getPlayedMediaByReaction(boolean likedMedia) { // add authentication later on...

        String userId = "TESTSUB";

        if(likedMedia) {
            return userRepository.findByUserIdAndLikedMediaTrue(userId);
        }
        else {
            return userRepository.findByUserIdAndDislikedMediaTrue(userId);
        }
    }

    // Fetches a media by id from microservice media-handling
    private MediaDto fetchMediaById(Long id) {
        ServiceInstance serviceInstance = loadBalancerClient.choose("media-handling");
        if (serviceInstance == null) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The service is not available");
        }

        MediaDto mediaDto = restClient.get()
                .uri(serviceInstance.getUri() + "/api/v1/mediahandling/media/" + id)
                .retrieve()
                .body(MediaDto.class);

        if (mediaDto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Media not found with id: " + id);
        }

        return mediaDto;
    }
}