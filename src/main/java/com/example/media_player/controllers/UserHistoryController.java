package com.example.media_player.controllers;

import com.example.media_player.entities.UserMedia;
import com.example.media_player.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/mediaplayer")
public class UserHistoryController {

    private final UserService userService;

    @Autowired
    public UserHistoryController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/mostplayed")
    public ResponseEntity<List<UserMedia>> getMostPlayedMedia(@AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(userService.getMostPlayedMedia(jwt), HttpStatus.OK);
    }

    // Used by recommendations service
    @GetMapping("/allplayed")
    public ResponseEntity<List<UserMedia>> getAllPlayedMedia(@AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(userService.getAllPlayedMedia(jwt), HttpStatus.OK);
    }

    @GetMapping("/liked")
    public ResponseEntity<List<UserMedia>> getLikedMedia(@AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(userService.getPlayedMediaByReaction(true, jwt), HttpStatus.OK);
    }

    @GetMapping("/disliked")
    public ResponseEntity<List<UserMedia>> getDislikedMedia(@AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(userService.getPlayedMediaByReaction(false, jwt), HttpStatus.OK);
    }
}
