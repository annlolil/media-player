package com.example.media_player.services;

import com.example.media_player.dtos.MediaDto;
import com.example.media_player.entities.UserMedia;
//import org.springframework.security.core.Authentication;

public interface UserServiceInterface {

    MediaDto playMedia(Long id); //add authentication user later on...

    MediaDto getMostPlayedMedia(); //Add authentication user later on...

    UserMedia likeMedia(Long id); // Add authentication user later on....

    UserMedia dislikeMedia(Long id); // Add authentication user later on...
}
