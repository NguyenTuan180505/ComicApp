package com.example.comicapp.dto.response;

import com.google.gson.annotations.SerializedName;

public class FavoriteResponse {
    @SerializedName("id")
    private Long id; // id của bản ghi favorite

    @SerializedName("userId")
    private Long userId;

    @SerializedName("storyId")
    private Long storyId; // <-- quan trọng: đây là ID của truyện

    @SerializedName("createdAt")
    private String createdAt;

    // Getter
    public Long getStoryId() {
        return storyId;
    }

    // Có thể thêm các getter khác nếu cần
    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
