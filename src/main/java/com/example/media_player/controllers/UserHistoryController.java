package com.example.media_player.controllers;

import com.example.media_player.dtos.MediaDto;
import com.example.media_player.entities.UserMedia;
import com.example.media_player.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mediaplayer")
public class UserHistoryController {

    private final UserService userService;

    @Autowired
    public UserHistoryController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/mostplayed")
    public ResponseEntity<List<UserMedia>> getMostPlayedMedia() { //Add @AuthenticationPrincipal jwt jwt later on
        return new ResponseEntity<>(userService.getMostPlayedMedia(), HttpStatus.OK);
    }

    @GetMapping("/allplayed")
    public ResponseEntity<List<UserMedia>> getAllPlayedMedia() {
        return new ResponseEntity<>(userService.getAllPlayedMedia(), HttpStatus.OK);
    }

    @GetMapping("/liked")
    public ResponseEntity<List<UserMedia>> getLikedMedia() { //Add @AuthenticationPrincipal Jwt jwt later on...
        return new ResponseEntity<>(userService.getUserMediaByReaction(true), HttpStatus.OK);
    }

    @GetMapping("/disliked")
    public ResponseEntity<List<UserMedia>> getDislikedMedia() { //Add @AuthenticationPrincipal Jwt jwt later on...
        return new ResponseEntity<>(userService.getUserMediaByReaction(false), HttpStatus.OK);
    }
}
