package com.example.comicapp.data.model;

import java.io.Serializable;

public class Emotion implements Serializable {
    private Long id;
    private String name;      // ví dụ: "Buồn", "Vui", "Tức giận"
    private String emoji;     // ví dụ: "😔", "😀", "😡"
    // có thể có thêm icon, color, v.v.

    public Emotion() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    @Override
    public String toString() {
        return emoji + " " + name;
    }
}