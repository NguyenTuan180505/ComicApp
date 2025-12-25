package com.example.comicapp.api;

import com.example.comicapp.dto.request.LoginRequest;
import com.example.comicapp.dto.request.RegisterRequest;
import com.example.comicapp.dto.response.LoginResponse;
import com.example.comicapp.dto.response.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);
    @POST("/auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);
}
