// data/model/Story.java
package com.example.comicapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Story implements Serializable, Parcelable {
    private Long id;
    private String title;
    private String author;
    private String description;
    private Integer emotionId;
    private String coverImage;     // URL ảnh bìa
    private String createdAt;      // "2025-10-26T20:56:21.937"

    // Constructor rỗng cho Retrofit/Gson
    public Story() {}

    // Getter & Setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getEmotionId() { return emotionId; }
    public void setEmotionId(Integer emotionId) { this.emotionId = emotionId; }

    public String getCoverImage() { return coverImage; }
    public void setCoverImage(String coverImage) { this.coverImage = coverImage; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    // Parcelable implementation
    protected Story(Parcel in) {
        if (in.readByte() == 0) id = null; else id = in.readLong();
        title = in.readString();
        author = in.readString();
        description = in.readString();
        if (in.readByte() == 0) emotionId = null; else emotionId = in.readInt();
        coverImage = in.readString();
        createdAt = in.readString();
    }

    public static final Creator<Story> CREATOR = new Creator<Story>() {
        @Override
        public Story createFromParcel(Parcel in) { return new Story(in); }
        @Override
        public Story[] newArray(int size) { return new Story[size]; }
    };

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) dest.writeByte((byte) 0); else { dest.writeByte((byte) 1); dest.writeLong(id); }
        dest.writeString(title);
        dest.writeString(author);
        dest.writeString(description);
        if (emotionId == null) dest.writeByte((byte) 0); else { dest.writeByte((byte) 1); dest.writeInt(emotionId); }
        dest.writeString(coverImage);
        dest.writeString(createdAt);
    }
}