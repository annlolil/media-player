package com.example.media_player.services;

import com.example.media_player.dtos.MediaDto;
//import org.springframework.security.core.Authentication;

public interface UserHistoryServiceInterface {

    MediaDto playMedia(Long id); //add authentication user later on...

    MediaDto getMostPlayedMedia(); //Add authentication user later on...
}
