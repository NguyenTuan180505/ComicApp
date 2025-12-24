// api/ChapterApi.java
package com.example.comicapp.api;

import com.example.comicapp.data.model.Chapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface ChapterApi {
    @GET("api/chapters")
    Call<List<Chapter>> getChaptersByStoryId(
            @Header("Authorization") String token,
            @Query("storyId") Long storyId
    );
}