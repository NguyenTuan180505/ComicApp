// data/model/Story.java
package com.example.comicapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Story implements Parcelable {
    private Long id;
    
    // Backend có thể trả về storyId trong favorite response
    @SerializedName(value = "storyId", alternate = {"story_id"})
    private Long storyId;
    
    private String title;
    
    // Backend có thể dùng "author", "authorName", "writer", etc.
    // Thử map với nhiều tên có thể có
    @SerializedName(value = "author", alternate = {"authorName", "writer", "createdBy"})
    private String author;
    
    private String description;
    
    @SerializedName("coverImage")
    private String coverImage; // URL ảnh từ backend
    
    // Giữ lại imageResId để tương thích với code cũ (dùng cho dummy data)
    private transient int imageResId = 0;

    // Constructor mặc định cho Gson
    public Story() {
    }

    // Constructor cũ để tương thích
    public Story(String title, String author, int imageResId) {
        this.title = title;
        this.author = author;
        this.imageResId = imageResId;
    }

    protected Story(Parcel in) {
        long idValue = in.readLong();
        id = idValue != 0 ? idValue : null;
        title = in.readString();
        author = in.readString();
        description = in.readString();
        coverImage = in.readString();
        imageResId = in.readInt();
    }

    public static final Creator<Story> CREATOR = new Creator<Story>() {
        @Override
        public Story createFromParcel(Parcel in) {
            return new Story(in);
        }

        @Override
        public Story[] newArray(int size) {
            return new Story[size];
        }
    };

    // Getters
    public Long getId() { return id; }
    public Long getStoryId() { return storyId; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getDescription() { return description; }
    public String getCoverImage() { return coverImage; }
    public int getImageResId() { return imageResId; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setStoryId(Long storyId) { this.storyId = storyId; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setDescription(String description) { this.description = description; }
    public void setCoverImage(String coverImage) { this.coverImage = coverImage; }
    public void setImageResId(int imageResId) { this.imageResId = imageResId; }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id != null ? id : 0L);
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(description);
        dest.writeString(coverImage);
        dest.writeInt(imageResId);
    }
}