package com.example.media_player.services;

import com.example.media_player.dtos.MediaDto;
import com.example.media_player.entities.UserHistory;
import com.example.media_player.repositories.UserHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

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
    public MediaDto playMedia(Long id) { //add authentication user later on

        ServiceInstance serviceInstance = loadBalancerClient.choose("MediaHandlingService");
        if (serviceInstance == null) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "MediaHandlingService not available");
        }

        MediaDto mediaDtoResponse = restClient.get()
                .uri(serviceInstance.getUri() + "/media/getmediabyid/" + id)
                .retrieve()
                .body(MediaDto.class);
        if (mediaDtoResponse == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Something went wrong");
        }
        UserHistory userHistory = new UserHistory();
        //userHistory.setUserSub();
        userHistory.setPlayedMediaId(mediaDtoResponse.getMediaId());
        userHistoryRepository.save(userHistory);
        return mediaDtoResponse;
    }

    @Override
    public MediaDto getMostPlayedMedia() { //add authentication user later on...
        return null;
    }
}
