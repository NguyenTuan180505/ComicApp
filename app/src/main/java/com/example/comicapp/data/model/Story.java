// data/model/Story.java
package com.example.comicapp.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Story implements Parcelable {
    private String title;
    private String author;
    private int imageResId;

    public Story(String title, String author, int imageResId) {
        this.title = title;
        this.author = author;
        this.imageResId = imageResId;
    }

    protected Story(Parcel in) {
        title = in.readString();
        author = in.readString();
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

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getImageResId() { return imageResId; }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(author);
        dest.writeInt(imageResId);
    }
}