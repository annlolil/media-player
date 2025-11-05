package com.example.media_player.services;

import com.example.media_player.dtos.MediaDto;
import com.example.media_player.entities.UserMedia;
import com.example.media_player.repositories.UserRepository;
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

        MediaDto playedMedia = fetchMediaById(id);

        // fetch existing history or create a new one
        UserMedia userMedia = userRepository
                .findByUserIdAndMediaId(userId, playedMedia.getMediaId())
                .orElseGet(() -> {
                    UserMedia newHistory = new UserMedia();
                    newHistory.setUserId(userId);
                    newHistory.setMediaId(playedMedia.getMediaId());
                    newHistory.setPlayCount(0L);
                    return newHistory;
                });

        userMedia.setPlayCount(userMedia.getPlayCount() + 1);

        userRepository.save(userMedia);

        return playedMedia;
    }

    @Override
    public MediaDto getMostPlayedMedia() { //add authentication user later on... Jwt jwt

        String userId = "TESTSUB"; // The jwt-token sub should be stored here. String userId = jwt.getSubject()

        List<UserMedia> mostPlayedMediaList = userRepository.findUserHistoriesByUserId(userId);

        UserMedia mostPlayed = mostPlayedMediaList.stream().max(Comparator.comparingLong(UserMedia::getPlayCount))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No media played yet"));

        MediaDto mostPlayedMedia = fetchMediaById(mostPlayed.getMediaId());

        return mostPlayedMedia;
    }

    // Can only like media that a user has listened to, fetching from media-player database.
    @Override
    public UserMedia likeMedia() {  //add authentication user later on.... jwt jwt

        String userId = "TESTSUB"; // for testing before keycloak

        UserMedia likedMedia = userRepository.findByUserIdAndMediaId(userId, id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "No media played yet"));

        likedMedia.setLikedMedia(true);

        userRepository.save(likedMedia);

        return likedMedia;
    }

    // Fetches a media by id from microservice media-handling
    private MediaDto fetchMediaById(Long id) {
        ServiceInstance serviceInstance = loadBalancerClient.choose("media-handling");
        if (serviceInstance == null) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The service is not available");
        }

        MediaDto mediaDto = restClient.get()
                .uri(serviceInstance.getUri() + "/api/mediahandling/media/" + id)
                .retrieve()
                .body(MediaDto.class);

        if (mediaDto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Media not found with id: " + id);
        }

        return mediaDto;
    }
}