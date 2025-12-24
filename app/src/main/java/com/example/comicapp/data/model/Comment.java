// data/model/Comment.java
package com.example.comicapp.data.model;

import java.io.Serializable;

public class Comment implements Serializable {
    private Long id;
    private Long storyId;
    private Long userId;
    private String content;
    private String createdAt;
    private String username; // Nếu backend trả thêm tên user (tùy chọn)

    // Constructor rỗng
    public Comment() {}

    // Getter
    public Long getId() { return id; }
    public Long getStoryId() { return storyId; }
    public Long getUserId() { return userId; }
    public String getContent() { return content; }
    public String getCreatedAt() { return createdAt; }
    public String getUsername() { return username == null ? "Đọc giả" : username; }

    // Setter nếu cần
    public void setId(Long id) { this.id = id; }
    public void setStoryId(Long storyId) { this.storyId = storyId; }
    public void setContent(String content) { this.content = content; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setUsername(String username) { this.username = username; }
}