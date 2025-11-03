package com.example.media_player.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class UserHistoryController {

    @GetMapping("/play")
    public String play() {
        return "Play";
    }
}
