package com.example.media_player.controllers;

import com.example.media_player.dtos.MediaDto;
import com.example.media_player.services.UserHistoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserHistoryController {

    private final UserHistoryService userHistoryService;

    public UserHistoryController(UserHistoryService userHistoryService) {
        this.userHistoryService = userHistoryService;
    }

    @PostMapping("/play")
    public ResponseEntity<MediaDto> playMedia(@RequestParam Long id) { //Add @authenticationprincipal jwt jwt later on...
        MediaDto media = userHistoryService.playMedia(id);
        return new ResponseEntity<>(media, HttpStatus.OK);
    }

//    @GetMapping("/mostplayed")
//    public ResponseEntity<MediaDto> getMostPlayedMedia() { //Add @Authenticationprincipal jwt jwt later on
//        return new ResponseEntity<>(media, HttpStatus.OK);
//    }
}
