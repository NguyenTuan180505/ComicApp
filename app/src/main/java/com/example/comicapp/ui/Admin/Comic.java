package com.example.comicapp.ui.Admin;

import java.io.Serializable;

public class Comic implements Serializable {
    private String title;
    private int chapters;
    private String thumbnail;
    private String summary;
    private String genre;

    public Comic(String title, int chapters, String thumbnail) {
        this.title = title;
        this.chapters = chapters;
        this.thumbnail = thumbnail;
        this.summary = "";
        this.genre = "";
    }

    public Comic(String title, int chapters, String thumbnail, String summary, String genre) {
        this.title = title;
        this.chapters = chapters;
        this.thumbnail = thumbnail;
        this.summary = summary;
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getChapters() {
        return chapters;
    }

    public void setChapters(int chapters) {
        this.chapters = chapters;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }
}