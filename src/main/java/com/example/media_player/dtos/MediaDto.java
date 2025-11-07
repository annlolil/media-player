package com.example.media_player.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class MediaDto {
//DO WE NEED MEDIANAME AND GENRES HERE? SHOULD MAYBE JUST BE URL.. LIKE IT WAS BEFORE.
    @JsonProperty("id")
    private Long mediaId;
    private String url;
    private List<String> genres;
    @JsonProperty("name")
    private String mediaName;

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

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }
}
