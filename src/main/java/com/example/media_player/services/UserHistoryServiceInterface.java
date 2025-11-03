package com.example.media_player.services;

import org.springframework.security.core.Authentication;

public interface UserHistoryServiceInterface {

    MediaDto playMedia(Authentication user);


}
