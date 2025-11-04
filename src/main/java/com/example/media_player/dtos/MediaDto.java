package com.example.media_player.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MediaDto {

    @JsonProperty("media_id")
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
