package com.example.comicapp.api;

import com.example.comicapp.data.model.ChapterDetail;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ChapterDetailApi {

    @GET("api/chapters/{id}")
    Call<ChapterDetail> getChapterDetail(
            @Header("Authorization") String token,
            @Path("id") Long chapterId
    );
}