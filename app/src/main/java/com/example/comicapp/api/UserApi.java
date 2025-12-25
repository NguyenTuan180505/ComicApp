// app/src/main/java/com/example/comicapp/api/UserApi.java
package com.example.comicapp.api;

import com.example.comicapp.data.model.User;
import com.example.comicapp.dto.request.ChangePasswordRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserApi {

    @GET("api/users/me")
    Call<User> getCurrentUser(@Header("Authorization") String token);
    @POST("api/users/change-password")
    Call<User> changePassword(
            @Header("Authorization") String token,
            @Body ChangePasswordRequest request
    );
}