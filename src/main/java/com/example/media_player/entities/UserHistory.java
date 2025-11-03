package com.example.media_player.entities;

import com.example.media_player.dtos.MediaDto;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class UserHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;

    //stores the users jwt token sub
    @Column(length = 100, unique = true) //nullable= false?
    private String userSub;

    @Column(nullable = false)
    private Long playedMediaId;

    public Long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Long historyId) {
        this.historyId = historyId;
    }

    public Long getPlayedMediaId() {
        return playedMediaId;
    }

    public void setPlayedMediaId(Long playedMediaId) {
        this.playedMediaId = playedMediaId;
    }

    public UserHistory() {}


    public String getUserSub() {
        return userSub;
    }

    public void setUserSub(String userSub) {
        this.userSub = userSub;
    }
}
