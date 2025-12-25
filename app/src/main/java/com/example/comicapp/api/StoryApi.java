// api/StoryApi.java
package com.example.comicapp.api;

import com.example.comicapp.data.model.Story;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface StoryApi {
    @GET("api/stories")
    Call<List<Story>> getAllStories(@Header("Authorization") String token);
    @GET("api/stories/search")
    Call<List<Story>> searchStories(
            @Header("Authorization") String token,
            @Query("title") String title
    );
}
