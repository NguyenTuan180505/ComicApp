package com.example.comicapp.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UnlockChapterApi {

    @GET("api/unlock-chapters/{chapterId}/is-unlocked")
    Call<Boolean> isUnlocked(
            @Header("Authorization") String token,
            @Path("chapterId") Long chapterId
    );

    @POST("api/unlock-chapters/{chapterId}/unlock")
    Call<Void> unlockChapter(
            @Header("Authorization") String token,
            @Path("chapterId") Long chapterId
    );
}