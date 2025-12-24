package com.example.comicapp.ui.ActivityUser;

import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicapp.R;
import com.example.comicapp.api.TaskApi;
import com.example.comicapp.data.adapter.TaskAdapter;
import com.example.comicapp.data.model.Task;
import com.example.comicapp.network.RetrofitClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TaskActivity extends BaseNavigationActivity {

    RecyclerView rvTasks;
    TextView txtUserPoints;
    BottomNavigationView bottomNav;
    TaskAdapter adapter;
    TaskApi taskApi;
    List<Task> taskList = new ArrayList<>();

    int userPoints = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        rvTasks = findViewById(R.id.rvTasks);
        txtUserPoints = findViewById(R.id.txtUserPoints);
        bottomNav = findViewById(R.id.bottomNav);

        updatePointsDisplay();

        adapter = new TaskAdapter(taskList, this::onTaskCompleted);
        rvTasks.setLayoutManager(new LinearLayoutManager(this));
        rvTasks.setAdapter(adapter);

        taskApi = RetrofitClient.getInstance().create(TaskApi.class);

        loadTasksFromApi();

        setupBottomNavigation(R.id.nav_tasks);
    }


    private static final int MAX_TASKS = 5;

    private void loadTasksFromApi() {
        taskApi.getAllTasks().enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call,
                                   Response<List<Task>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    taskList.clear();

                    List<Task> allTasks = response.body();

                    // 👉 Giới hạn tối đa 5 task
                    int limit = Math.min(allTasks.size(), MAX_TASKS);
                    taskList.addAll(allTasks.subList(0, limit));

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Đánh dấu tab Tasks khi quay lại màn hình
        bottomNav.setSelectedItemId(R.id.nav_tasks);
    }

    @Override
    protected int getCurrentNavItemId() {
        return 0;
    }

    private void onTaskCompleted(Task task) {
        if (task.isCompleted()) return;

        taskApi.completeTask(task.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call,
                                   Response<Void> response) {
                if (response.isSuccessful()) {
                    task.setCompleted(true);
                    userPoints += task.getReward();
                    updatePointsDisplay();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    private void updatePointsDisplay() {
        txtUserPoints.setText(String.valueOf(userPoints));
    }
}