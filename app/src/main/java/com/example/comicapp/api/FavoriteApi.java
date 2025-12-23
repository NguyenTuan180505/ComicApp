package com.example.comicapp.api;

import com.example.comicapp.data.model.Story;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface FavoriteApi {

    // Backend trong Postman: {{API_PREFIX}}favorites/me
    // Trả về list favorite (có thể chỉ có id hoặc storyId)
    @GET("api/favorites/me")
    Call<List<Story>> getFavoriteStories(@Header("Authorization") String token);
    
    // API lấy tất cả stories để match với favorites
    @GET("api/stories")
    Call<List<Story>> getAllStories(@Header("Authorization") String token);

    @DELETE("favorites/{storyId}")
    Call<Void> removeFavorite(
            @Header("Authorization") String token,
            @Path("storyId") Long storyId
    );
}