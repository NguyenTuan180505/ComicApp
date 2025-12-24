
package com.example.comicapp.api;
import com.example.comicapp.dto.request.FavoriteRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FavoriteApi {

    // Thêm truyện vào yêu thích
    @POST("api/favorites")
    Call<Void> addFavorite(
            @Header("Authorization") String token,
            @Body FavoriteRequest request
    );

    // Bỏ truyện khỏi yêu thích
    @DELETE("api/favorites/{storyId}")
    Call<Void> removeFavorite(
            @Header("Authorization") String token,
            @Path("storyId") Long storyId
    );

    // Trong FavoriteApi.java
    @GET("api/favorites/check")
    Call<Boolean> isFavorite(
            @Header("Authorization") String token,
            @Query("storyId") Long storyId
    );
  @GET("api/favorites/me")
    Call<List<Story>> getFavoriteStories(@Header("Authorization") String token);
    
    // API lấy tất cả stories để match với favorites
    @GET("api/stories")
    Call<List<Story>> getAllStories(@Header("Authorization") String token);
}
