// api/CommentApi.java
package com.example.comicapp.api;

import com.example.comicapp.data.model.Comment;
import com.example.comicapp.dto.request.CommentRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CommentApi {

    // Lấy danh sách bình luận theo truyện
    @GET("api/comments")
    Call<List<Comment>> getCommentsByStoryId(
            @Header("Authorization") String token,
            @Query("storyId") Long storyId
    );

    // Gửi bình luận mới
    @POST("api/comments")
    Call<Comment> postComment(
            @Header("Authorization") String token,
            @Body CommentRequest request
    );

    // Xóa bình luận (tùy chọn sau)
    @DELETE("api/comments/{id}")
    Call<Void> deleteComment(
            @Header("Authorization") String token,
            @Path("id") Long commentId
    );
}
