// data/model/Chapter.java
package com.example.comicapp.data.model;

import java.io.Serializable;

public class Chapter implements Serializable {
    private Long id;
    private Long storyId;
    private String title;
    private int chapterNumber;
    private boolean isLocked;
    private String createdAt;

    // Constructor rỗng cho Retrofit
    public Chapter() {}

    // Getter
    public Long getId() { return id; }
    public Long getStoryId() { return storyId; }
    public String getTitle() { return title; }
    public int getChapterNumber() { return chapterNumber; }
    public boolean isLocked() { return isLocked; }
    public String getCreatedAt() { return createdAt; }

    // Setter (nếu cần)
    public void setId(Long id) { this.id = id; }
    public void setStoryId(Long storyId) { this.storyId = storyId; }
    public void setTitle(String title) { this.title = title; }
    public void setChapterNumber(int chapterNumber) { this.chapterNumber = chapterNumber; }
    public void setLocked(boolean locked) { isLocked = locked; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    // Để hiển thị đẹp trong RecyclerView
    @Override
    public String toString() {
        return title + (isLocked ? " [Khóa]" : "");
    }
}