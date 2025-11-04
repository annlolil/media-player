package com.example.media_player.services;

import com.example.media_player.dtos.MediaDto;
import com.example.media_player.entities.UserHistory;
import com.example.media_player.repositories.UserHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
//import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;

@Service
public class UserHistoryService implements UserHistoryServiceInterface {

    private final UserHistoryRepository userHistoryRepository;
    private final RestClient restClient;
    private final LoadBalancerClient loadBalancerClient;

    @Autowired
    public UserHistoryService(UserHistoryRepository userHistoryRepository, RestClient.Builder restClientBuilder, LoadBalancerClient loadBalancerClient) {
        this.userHistoryRepository = userHistoryRepository;
        this.restClient = restClientBuilder.build();
        this.loadBalancerClient = loadBalancerClient;
    }

    @Override
    public MediaDto playMedia(Long id) {
        MediaDto playedMedia = fetchMediaById(id);

        // fetch existing history or create a new one
        UserHistory userHistory = userHistoryRepository
                .findByUserIdAndMediaId("TESTSUB", playedMedia.getMediaId())
                .orElseGet(() -> {
                    UserHistory newHistory = new UserHistory();
                    newHistory.setUserId("TESTSUB");
                    newHistory.setMediaId(playedMedia.getMediaId());
                    newHistory.setPlayCount(0L); // initialize
                    return newHistory;
                });

        userHistory.setPlayCount(userHistory.getPlayCount() + 1);

        userHistoryRepository.save(userHistory);

        return playedMedia;
    }

    @Override
    public MediaDto getMostPlayedMedia() { //add authentication user later on... Jwt jwt

        String userId = "TESTSUB"; // The jwt-token sub should be stored here. String userId = jwt.getSubject()

        List<UserHistory> mostPlayedMediaList = userHistoryRepository.findUserHistoriesByUserId(userId);

        UserHistory mostPlayed = mostPlayedMediaList.stream().max(Comparator.comparingLong(UserHistory::getPlayCount))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No media played yet"));

        MediaDto mostPlayedMedia = fetchMediaById(mostPlayed.getMediaId());

        return mostPlayedMedia;
    }

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