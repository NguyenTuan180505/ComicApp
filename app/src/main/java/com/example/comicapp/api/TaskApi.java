package com.example.comicapp.api;

import com.example.comicapp.data.model.Task;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TaskApi {

    @GET("api/tasks")
    Call<List<Task>> getAllTasks();

    @POST("api/tasks/{id}/complete")
    Call<Void> completeTask(@Path("id") long taskId);
}

