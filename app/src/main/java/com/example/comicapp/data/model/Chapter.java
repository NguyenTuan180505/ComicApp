package com.example.comicapp.data.model;


public class Chapter {
    private String name;
    private int number;
    private String uploadDate;
    private int pageCount;

    public Chapter(String name, int number, String uploadDate, int pageCount) {
        this.name = name;
        this.number = number;
        this.uploadDate = uploadDate;
        this.pageCount = pageCount;
    }

    // getter...
    public String getName() { return name; }
    public int getNumber() { return number; }
    public String getUploadDate() { return uploadDate; }
    public int getPageCount() { return pageCount; }
}