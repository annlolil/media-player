package com.example.media_player.controllers;

import com.example.media_player.dtos.MediaDto;
import com.example.media_player.entities.UserMedia;
import com.example.media_player.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/mediaplayer")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/play")
    public ResponseEntity<MediaDto> playMedia(@RequestParam Long id, @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(userService.playMedia(id, jwt), HttpStatus.OK);
    }

    @PutMapping("/like")
    public ResponseEntity<UserMedia> likeMedia(@RequestParam Long id, @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(userService.likeDislikeMedia(id, "like", jwt), HttpStatus.OK);
    }

    @PutMapping("/dislike")
    public ResponseEntity<UserMedia> dislikeMedia(@RequestParam Long id, @AuthenticationPrincipal Jwt jwt) {
        return new ResponseEntity<>(userService.likeDislikeMedia(id, "dislike", jwt), HttpStatus.OK);
    }
}
