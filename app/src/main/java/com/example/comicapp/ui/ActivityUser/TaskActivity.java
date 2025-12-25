package com.example.comicapp.ui.ActivityUser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.comicapp.R;
import com.example.comicapp.api.TaskApi;
import com.example.comicapp.api.UserApi;
import com.example.comicapp.data.model.Task;
import com.example.comicapp.data.adapter.TaskAdapter;
import com.example.comicapp.dto.response.UserPointsResponse;
import com.example.comicapp.network.RetrofitClient;
import com.example.comicapp.utils.SessionManager;
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
    UserApi userApi;
    List<Task> taskList = new ArrayList<>(); // Chỉ chứa task chưa hoàn thành
    int userPoints = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        rvTasks = findViewById(R.id.rvTasks);
        txtUserPoints = findViewById(R.id.txtUserPoints);
        bottomNav = findViewById(R.id.bottomNav);

        // Khởi tạo adapter với danh sách rỗng
        adapter = new TaskAdapter(taskList, this::onTaskCompleted);
        rvTasks.setLayoutManager(new LinearLayoutManager(this));
        rvTasks.setAdapter(adapter);

        // Khởi tạo API
        taskApi = RetrofitClient.getInstance().create(TaskApi.class);
        userApi = RetrofitClient.getInstance().create(UserApi.class);

        // Load dữ liệu
        loadUserPoints();
        loadTasksFromApi();

        // Setup Bottom Navigation
        setupBottomNavigation(R.id.nav_tasks);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNav.setSelectedItemId(R.id.nav_tasks);
        // Refresh khi quay lại màn hình
        loadUserPoints();
        loadTasksFromApi();
    }

    @Override
    protected int getCurrentNavItemId() {
        return 0;
    }

    private String getAuthToken() {
        String rawToken = SessionManager.getToken(this);
        if (rawToken == null) return null;
        return rawToken.startsWith("Bearer ") ? rawToken : "Bearer " + rawToken;
    }

    private boolean checkAuth() {
        String token = getAuthToken();
        if (token == null) {
            Toast.makeText(this, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return false;
        }
        return true;
    }

    /**
     * Load danh sách nhiệm vụ CHỈ HIỆN NHỮNG TASK CHƯA HOÀN THÀNH
     */
    private void loadTasksFromApi() {
        if (!checkAuth()) return;
        String authToken = getAuthToken();

        taskApi.getMyTasks(authToken).enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Task> allTasks = response.body();

                    // LỌC CHỈ GIỮ TASK CHƯA HOÀN THÀNH
                    taskList.clear();
                    for (Task task : allTasks) {
                        if (!task.isCompleted()) { // Chỉ thêm nếu chưa hoàn thành
                            taskList.add(task);
                        }
                    }

                    // Sắp xếp theo ID giảm dần (mới nhất trước)
                    taskList.sort((a, b) -> Long.compare(b.getId(), a.getId()));

                    adapter.notifyDataSetChanged();

                    Log.d("TaskActivity", "Loaded " + taskList.size() + " unfinished tasks");
                } else {
                    Log.e("TaskActivity", "Failed to load tasks: " + response.code());
                    handleAuthError(response.code());
                    fallbackToDummyTasks();
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Log.e("TaskActivity", "Error loading tasks", t);
                Toast.makeText(TaskActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                fallbackToDummyTasks();
            }
        });
    }

    /**
     * Fallback dummy data - chỉ thêm các task chưa hoàn thành
     */
    private void fallbackToDummyTasks() {
        if (taskList.isEmpty()) {
            taskList.add(new Task("Đọc 3 truyện", "Hoàn thành 3 truyện bất kỳ", 50, false));
            taskList.add(new Task("Chia sẻ truyện yêu thích", "Chia sẻ 1 truyện cho bạn bè", 20, false));
            taskList.add(new Task("Đánh giá truyện", "Đánh giá 5 sao cho truyện", 15, false));
            adapter.notifyDataSetChanged();
        }
    }

    private void loadUserPoints() {
        if (!checkAuth()) return;
        String authToken = getAuthToken();

        userApi.getUserPoints(authToken).enqueue(new Callback<UserPointsResponse>() {
            @Override
            public void onResponse(Call<UserPointsResponse> call, Response<UserPointsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userPoints = response.body().getPoints();
                    updatePointsDisplay();
                    Log.d("TaskActivity", "User points: " + userPoints);
                } else {
                    Log.e("TaskActivity", "Failed to load points: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserPointsResponse> call, Throwable t) {
                Log.e("TaskActivity", "Error loading points", t);
            }
        });
    }

    private void onTaskCompleted(Task task) {
        if (task.getId() == null) {
            Toast.makeText(this, "Lỗi: Không tìm thấy ID nhiệm vụ", Toast.LENGTH_SHORT).show();
            return;
        }

        String authToken = getAuthToken();
        if (authToken == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        Log.d("TaskActivity", "Completing task: " + task.getName() + " (ID: " + task.getId() + ")");

        taskApi.completeTask(authToken, task.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Refresh lại → task hoàn thành sẽ bị loại khỏi danh sách
                    loadTasksFromApi();
                    loadUserPoints();

                    Toast.makeText(TaskActivity.this,
                            "Hoàn thành nhiệm vụ! +" + task.getReward() + " điểm",
                            Toast.LENGTH_SHORT).show();
                } else {
                    handleCompleteTaskError(response);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("TaskActivity", "Network error completing task", t);
                Toast.makeText(TaskActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleCompleteTaskError(Response<Void> response) {
        int code = response.code();
        String msg;

        if (code == 400) {
            msg = "Nhiệm vụ đã được hoàn thành trước đó";
        } else if (code == 401) {
            msg = "Phiên đăng nhập hết hạn";
            new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                startActivity(new Intent(TaskActivity.this, LoginActivity.class));
                finish();
            }, 2000);
        } else if (code == 404) {
            msg = "Không tìm thấy nhiệm vụ";
        } else {
            msg = "Lỗi server (" + code + ")";
        }

        Toast.makeText(TaskActivity.this, msg, Toast.LENGTH_LONG).show();
        loadTasksFromApi(); // Refresh để đồng bộ trạng thái
    }

    private void handleAuthError(int code) {
        if (code == 401 || code == 403) {
            Toast.makeText(this, "Phiên đăng nhập đã hết hạn", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void updatePointsDisplay() {
        txtUserPoints.setText(String.valueOf(userPoints));
    }
}