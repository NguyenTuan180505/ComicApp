package com.example.comicapp;

public class Story {
    private String title;
    private String author;
    private int imageResId; // hình ảnh trong drawable

    public Story(String title, String author, int imageResId) {
        this.title = title;
        this.author = author;
        this.imageResId = imageResId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getImageResId() {
        return imageResId;
    }
}
