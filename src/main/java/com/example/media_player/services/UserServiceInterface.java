package com.example.media_player.services;

import com.example.media_player.dtos.MediaDto;
import com.example.media_player.entities.UserMedia;

import java.util.List;

public interface UserServiceInterface {

    MediaDto playMedia(Long id); //add authentication user later on...

    List<UserMedia> getAllPlayedMedia(); //add authentication user later on...

    List<UserMedia> getMostPlayedMedia(); //Add authentication user later on...

    UserMedia likeDislikeMedia(Long id, String likeDislike); // Add authentication user later on....

    List<UserMedia> getPlayedMediaByReaction(boolean reaction); // Add user later on....
}
