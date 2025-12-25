package com.example.comicapp.api;

import com.example.comicapp.data.model.Task;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TaskApi {
    // Lấy danh sách tất cả nhiệm vụ (admin)
    @GET("api/tasks")
    Call<List<Task>> getAllTasks(@Header("Authorization") String token);

    // Lấy danh sách nhiệm vụ của người dùng (kiểm tra trạng thái hoàn thành)
    @GET("api/tasks/my")
    Call<List<Task>> getMyTasks(@Header("Authorization") String token);

    // Đánh dấu hoàn thành nhiệm vụ
    @POST("api/tasks/{id}/complete")
    Call<Void> completeTask(
            @Header("Authorization") String token,
            @Path("id") Long taskId
    );
}
