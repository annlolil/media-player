package com.example.media_player.services;

import com.example.media_player.dtos.MediaDto;
import com.example.media_player.entities.UserMedia;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface UserServiceInterface {

    MediaDto playMedia(Long id, Jwt jwt);

    List<UserMedia> getAllPlayedMedia(Jwt jwt);

    List<UserMedia> getMostPlayedMedia(Jwt jwt);

    UserMedia likeDislikeMedia(Long id, String likeDislike, Jwt jwt);

    List<UserMedia> getPlayedMediaByReaction(boolean reaction, Jwt jwt);
}
