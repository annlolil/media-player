package com.example.media_player.dtos;

public class MediaDto {

    private Long mediaId;
    private String url;

    public MediaDto() {}

    public Long getMediaId() {
        return mediaId;
    }

    public void setMediaId(Long mediaId) {
        this.mediaId = mediaId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
