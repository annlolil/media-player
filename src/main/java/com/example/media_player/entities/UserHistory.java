package com.example.media_player.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "user_media_stats",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"userId", "mediaId"})
        })
public class UserHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //stores the users jwt token sub
    @Column(length = 100, nullable = false)
    private String userId;

    @Column(nullable = false)
    private Long mediaId;

    @Column(nullable = false)
    private Long playCount = 0L;

    public UserHistory() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getPlayCount() {
        return playCount;
    }

    public void setPlayCount(Long playCount) {
        this.playCount = playCount;
    }
}
