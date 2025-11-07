package com.example.media_player.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "user_media_stats",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"userId", "mediaId"})
        })
public class UserMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Stores the users jwt token sub
    @Column(length = 100, nullable = false)
    private String userId;

    @Column(nullable = false)
    private Long mediaId;

    @Column(nullable = false)
    private Long playCount;

    @Column
    private boolean likedMedia = false;

    @Column
    private boolean dislikedMedia = false;

    public UserMedia() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public Long getPlayCount() {
        return playCount;
    }

    public void setPlayCount(Long playCount) {
        this.playCount = playCount;
    }

    public boolean isLikedMedia() {
        return likedMedia;
    }

    public void setLikedMedia(boolean likedMedia) {
        this.likedMedia = likedMedia;
    }

    public boolean isDislikedMedia() {
        return dislikedMedia;
    }

    public void setDislikedMedia(boolean dislikedMedia) {
        this.dislikedMedia = dislikedMedia;
    }
}
