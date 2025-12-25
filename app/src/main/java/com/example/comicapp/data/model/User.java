package com.example.comicapp.data.model;

import com.google.gson.annotations.SerializedName;

public class User {
    // Backend có thể trả về "username", "user", "name", etc.
    @SerializedName(value = "username", alternate = {"user", "name"})
    private String username;
    
    private String email;
    
    // Có thể có thêm các field khác từ backend
    private Long id;
    private String role;

    // Constructor mặc định cho Gson
    public User() {
    }

    // Getters và Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
}