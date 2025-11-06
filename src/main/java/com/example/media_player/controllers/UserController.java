package com.example.media_player.controllers;

import com.example.media_player.dtos.MediaDto;
import com.example.media_player.entities.UserMedia;
import com.example.media_player.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mediaplayer")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/play")
    public ResponseEntity<MediaDto> playMedia(@RequestParam Long id) { //Add @AuthenticationPrincipal Jwt jwt later on...
        return new ResponseEntity<>(userService.playMedia(id), HttpStatus.OK);
    }

    @PutMapping("/like")
    public ResponseEntity<UserMedia> likeMedia(@RequestParam Long id) { // Add @AuthenticationPrincipal Jwt jwt later on...
        return new ResponseEntity<>(userService.likeMedia(id), HttpStatus.OK);
    }

    @PutMapping("/dislike")
    public ResponseEntity<UserMedia> dislikeMedia(@RequestParam Long id) {// Add @AuthenticationPrincipal Jwt jwt later on...
        return new ResponseEntity<>(userService.dislikeMedia(id), HttpStatus.OK);
    }
}
