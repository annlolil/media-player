package com.example.media_player.controllers;

import com.example.media_player.dtos.MediaDto;
import com.example.media_player.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mediaplayer")
public class UserHistoryController {

    private final UserService userService;

    public UserHistoryController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/mostplayed")
    public ResponseEntity<MediaDto> getMostPlayedMedia() { //Add @Authenticationprincipal jwt jwt later on
        return new ResponseEntity<>(userService.getMostPlayedMedia(), HttpStatus.OK);
    }

}
