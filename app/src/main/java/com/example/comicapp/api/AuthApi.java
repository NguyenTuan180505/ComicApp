package com.example.comicapp.api;

import com.example.comicapp.dto.request.LoginRequest;
import com.example.comicapp.dto.response.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("/auth/login")
    Call<LoginResponse> login(@Body LoginRequest request);
}
