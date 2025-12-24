package com.example.comicapp.dto.request;

public class CommentRequest {
    private Long storyId;
    private String content;

    public CommentRequest(Long storyId, String content) {
        this.storyId = storyId;
        this.content = content;
    }
}
