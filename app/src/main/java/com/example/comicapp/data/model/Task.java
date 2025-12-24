package com.example.comicapp.data.model;

import com.google.gson.annotations.SerializedName;

public class Task {

    private long id;

    private String name;

    @SerializedName("description")
    private String desc;

    @SerializedName("pointsReward")
    private int reward;

    private boolean completed;

    // BẮT BUỘC cho Gson
    public Task() {
    }

    // DÙNG cho dummy data / test
    public Task(String name, String desc, int reward, boolean completed) {
        this.name = name;
        this.desc = desc;
        this.reward = reward;
        this.completed = completed;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDesc() {
        return desc;
    }

    public int getReward() {
        return reward;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
