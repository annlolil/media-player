package com.example.media_player.services;

import com.example.media_player.dtos.MediaDto;
import com.example.media_player.entities.UserMedia;
import com.example.media_player.repositories.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
//import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
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
    public MediaDto playMedia(Long id, Jwt jwt) {

        String userId = jwt.getSubject();

        // Fetching media to play from media-handling service
        MediaDto playedMedia = fetchMediaById(id, jwt);

        // Fetch existing history or create a new one
        UserMedia userMedia = userRepository
                .findByUserIdAndMediaId(userId, playedMedia.getMediaId())
                .orElseGet(() -> {
                    UserMedia newUserMedia = new UserMedia();
                    newUserMedia.setUserId(userId);
                    newUserMedia.setMediaId(playedMedia.getMediaId());
                    newUserMedia.setPlayCount(0L);
                    return newUserMedia;
                });

        userMedia.setPlayCount(userMedia.getPlayCount() + 1);

        userRepository.save(userMedia);

        return playedMedia;
    }

    @Override
    public List<UserMedia> getAllPlayedMedia(Jwt jwt) {

        String userId = jwt.getSubject();

        return userRepository.findUserMediaByUserId(userId);
    }

    @Override
    public List<UserMedia> getMostPlayedMedia(Jwt jwt) {

        String userId = jwt.getSubject();

        List<UserMedia> allPlayed = userRepository.findUserMediaByUserId(userId);

        long maxCount = allPlayed.stream().map(UserMedia::getPlayCount).max(Long::compareTo).orElse(0L);

        List<UserMedia> mostPlayed = allPlayed.stream().filter(userMedia -> userMedia.getPlayCount() == maxCount).toList();

        return mostPlayed;
    }


    // Can only like/dislike media that a user has listened to, fetching from media-player database.
    @Override
    public UserMedia likeDislikeMedia(Long id, String likeDislike, Jwt jwt) {

        String userId = jwt.getSubject();

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
    public List<UserMedia> getPlayedMediaByReaction(boolean likedMedia, Jwt jwt) {

        String userId = jwt.getSubject();

        if(likedMedia) {
            return userRepository.findByUserIdAndLikedMediaTrue(userId);
        }
        else {
            return userRepository.findByUserIdAndDislikedMediaTrue(userId);
        }
    }

    // Fetches a media by id from microservice media-handling
    private MediaDto fetchMediaById(Long id, Jwt jwt) {

        String token = jwt.getTokenValue();

        ServiceInstance serviceInstance = loadBalancerClient.choose("media-handling");
        if (serviceInstance == null) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The service is not available");
        }

        try {
            return restClient.get()
                    .uri(serviceInstance.getUri() + "/api/v1/mediahandling/media/" + id)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .retrieve()
                    .body(MediaDto.class);
        }
        catch (RestClientResponseException e) {
            if(e.getStatusCode().value() == 404) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Media not found");
            } else {
                throw new ResponseStatusException(HttpStatus.valueOf(e.getStatusCode().value()),
                        "Something went wrong when fetching media: " + e.getResponseBodyAsString());
            }
        }
    }
}