package com.example.comicapp.data.model;

import com.google.gson.annotations.SerializedName;

public class Task {
    private Long id;
    private String name;

    @SerializedName("description")
    private String desc;

    @SerializedName("pointsReward")
    private int reward;

    private boolean completed;

    // Constructor mặc định cho Gson
    public Task() {
    }

    // Constructor cho dummy data / test
    public Task(String name, String desc, int reward, boolean completed) {
        this.name = name;
        this.desc = desc;
        this.reward = reward;
        this.completed = completed;
    }

    // Getters và Setters
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
