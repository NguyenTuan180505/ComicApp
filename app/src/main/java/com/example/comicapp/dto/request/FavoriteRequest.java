package com.example.comicapp.dto.request;

public class FavoriteRequest {
    private Long storyId;

    public FavoriteRequest(Long storyId) {
        this.storyId = storyId;
    }

    public Long getStoryId() {
        return storyId;
    }
}
