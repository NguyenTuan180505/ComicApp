package com.example.comicapp.data.model;

public class Task {
    private String name;
    private String desc;
    private int reward;
    private boolean completed;

    public Task(String name, String desc, int reward, boolean completed) {
        this.name = name;
        this.desc = desc;
        this.reward = reward;
        this.completed = completed;
    }

    public String getName() { return name; }
    public String getDesc() { return desc; }
    public int getReward() { return reward; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}
