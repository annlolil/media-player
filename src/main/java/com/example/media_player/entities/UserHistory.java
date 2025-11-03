package com.example.media_player.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class UserHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @OneToMany(fetch = FetchType.LAZY)
    private List<MediaDto> likedMedia;

    @OneToMany(fetch = FetchType.LAZY)

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }
}
