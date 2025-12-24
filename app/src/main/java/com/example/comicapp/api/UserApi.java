// app/src/main/java/com/example/comicapp/api/UserApi.java
package com.example.comicapp.api;

import com.example.comicapp.data.model.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface UserApi {

    @GET("api/users/me")
    Call<User> getCurrentUser(@Header("Authorization") String token);
}