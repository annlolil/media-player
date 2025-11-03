package com.example.media_player.entities;

import com.example.media_player.dtos.MediaDto;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class UserHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    //stores the users jwt token sub
    @Column(length = 100, unique = true) //nullable= false?
    private String userSub;

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
