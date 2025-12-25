// api/EmotionApi.java
package com.example.comicapp.api;

import com.example.comicapp.data.model.Emotion;
import com.example.comicapp.data.model.Story;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface EmotionApi {

    @GET("api/emotions")
    Call<List<Emotion>> getAllEmotions(@Header("Authorization") String token);

    @GET("api/emotions/{id}/stories")
    Call<List<Story>> getStoriesByEmotionId(
            @Header("Authorization") String token,
            @Path("id") Long emotionId
    );
}