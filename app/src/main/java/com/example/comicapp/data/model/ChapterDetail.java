package com.example.comicapp.data.model;

import java.io.Serializable;

public class ChapterDetail implements Serializable {

    private Long id;
    private Long storyId;
    private String title;
    private String content;        // ← THÊM FIELD QUAN TRỌNG: nội dung chương
    private int chapterNumber;
    private boolean isLocked;
    private String createdAt;

    // Constructor rỗng cho Retrofit/Gson
    public ChapterDetail() {}

    // Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getStoryId() { return storyId; }
    public void setStoryId(Long storyId) { this.storyId = storyId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public int getChapterNumber() { return chapterNumber; }
    public void setChapterNumber(int chapterNumber) { this.chapterNumber = chapterNumber; }

    public boolean isLocked() { return isLocked; }
    public void setLocked(boolean locked) { this.isLocked = locked; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
}