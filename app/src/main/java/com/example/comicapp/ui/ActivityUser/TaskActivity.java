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
import com.example.comicapp.data.model.User;
import com.example.comicapp.data.adapter.TaskAdapter;
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
    List<Task> taskList = new ArrayList<>();
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

        // Load dữ liệu từ API
        loadUserPoints();
        loadTasksFromApi();

        // Setup Bottom Navigation
        setupBottomNavigation(R.id.nav_tasks);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Đánh dấu tab Tasks khi quay lại màn hình
        bottomNav.setSelectedItemId(R.id.nav_tasks);
        // Refresh dữ liệu khi quay lại
        loadUserPoints();
        loadTasksFromApi();
    }

    @Override
    protected int getCurrentNavItemId() {
        return 0;
    }

    /**
     * Lấy token từ SessionManager và format với Bearer prefix
     */
    private String getAuthToken() {
        String rawToken = SessionManager.getToken(this);
        if (rawToken == null) {
            return null;
        }
        // Đảm bảo token có prefix "Bearer "
        return rawToken.startsWith("Bearer ") ? rawToken : "Bearer " + rawToken;
    }

    /**
     * Kiểm tra và chuyển về LoginActivity nếu chưa đăng nhập
     */
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
     * Load danh sách nhiệm vụ từ API
     */
    private void loadTasksFromApi() {
        if (!checkAuth()) return;

        String authToken = getAuthToken();
        taskApi.getUserTasks(authToken).enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    taskList.clear();
                    taskList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    Log.d("TaskActivity", "Loaded " + taskList.size() + " tasks from API");
                } else {
                    Log.e("TaskActivity", "Failed to load tasks: " + response.code());
                    if (response.code() == 401 || response.code() == 403) {
                        Toast.makeText(TaskActivity.this, "Phiên đăng nhập đã hết hạn", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(TaskActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(TaskActivity.this, "Không thể tải danh sách nhiệm vụ", Toast.LENGTH_SHORT).show();
                        // Fallback to dummy data nếu có lỗi
                        fallbackToDummyTasks();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Log.e("TaskActivity", "Error loading tasks", t);
                Toast.makeText(TaskActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                // Fallback to dummy data nếu có lỗi
                fallbackToDummyTasks();
            }
        });
    }

    /**
     * Fallback về dummy data nếu API lỗi
     */
    private void fallbackToDummyTasks() {
        if (taskList.isEmpty()) {
            taskList.add(new Task("Đọc 3 truyện", "Hoàn thành 3 truyện bất kỳ", 50, false));
            taskList.add(new Task("Đăng nhập mỗi ngày", "Điểm thưởng cho đăng nhập", 10, true));
            taskList.add(new Task("Chia sẻ truyện yêu thích", "Chia sẻ 1 truyện cho bạn bè", 20, false));
            taskList.add(new Task("Đánh giá truyện", "Đánh giá 5 sao cho truyện", 15, false));
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * Load điểm người dùng từ API (lấy từ User model)
     */
    private void loadUserPoints() {
        if (!checkAuth()) return;

        String authToken = getAuthToken();
        userApi.getCurrentUser(authToken).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    // TODO: Nếu User model có field points, uncomment dòng dưới
                    // userPoints = user.getPoints() != null ? user.getPoints() : 0;
                    updatePointsDisplay();
                    Log.d("TaskActivity", "Loaded user data");
                } else {
                    Log.e("TaskActivity", "Failed to load user points: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("TaskActivity", "Error loading user points", t);
            }
        });
    }

    /**
     * Xử lý khi bấm nút hoàn thành nhiệm vụ
     */
    private void onTaskCompleted(Task task) {
        if (task.isCompleted()) {
            Toast.makeText(this, "Nhiệm vụ này đã được hoàn thành", Toast.LENGTH_SHORT).show();
            return;
        }

        if (task.getId() == null) {
            Toast.makeText(this, "Lỗi: Không tìm thấy ID nhiệm vụ", Toast.LENGTH_SHORT).show();
            return;
        }

        String rawToken = SessionManager.getToken(this);
        if (rawToken == null) {
            Toast.makeText(this, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        String authToken = rawToken.startsWith("Bearer ") ? rawToken : "Bearer " + rawToken;
        
        Log.d("TaskActivity", "Completing task ID: " + task.getId() + ", Name: " + task.getName());
        Log.d("TaskActivity", "Token: " + (authToken.length() > 30 ? authToken.substring(0, 30) + "..." : authToken));
        
        taskApi.completeTask(authToken, task.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.d("TaskActivity", "Complete task response code: " + response.code());
                
                if (response.isSuccessful()) {
                    // Cập nhật trạng thái nhiệm vụ
                    task.setCompleted(true);
                    userPoints += task.getReward();
                    updatePointsDisplay();
                    adapter.notifyDataSetChanged();
                    
                    Toast.makeText(TaskActivity.this, 
                            "Hoàn thành nhiệm vụ! +" + task.getReward() + " điểm", 
                            Toast.LENGTH_SHORT).show();
                    Log.d("TaskActivity", "Task completed successfully: " + task.getName());
                    
                    // Refresh điểm từ API để đảm bảo đồng bộ với server (không bắt buộc)
                    // Chỉ refresh nếu cần, không block UI
                    refreshUserPointsSilently();
                } else {
                    // Log chi tiết lỗi
                    String errorBody = "";
                    if (response.errorBody() != null) {
                        try {
                            errorBody = response.errorBody().string();
                            Log.e("TaskActivity", "Error body: " + errorBody);
                        } catch (Exception e) {
                            Log.e("TaskActivity", "Error reading error body", e);
                        }
                    }
                    
                    Log.e("TaskActivity", "Failed to complete task. Code: " + response.code() + ", Body: " + errorBody);
                    
                    String errorMsg = "Không thể hoàn thành nhiệm vụ (Code: " + response.code() + ")";
                    if (response.code() == 400) {
                        errorMsg = "Nhiệm vụ đã được hoàn thành trước đó";
                    } else if (response.code() == 401) {
                        errorMsg = "Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại";
                        // Chỉ logout khi thực sự là lỗi authentication
                        Toast.makeText(TaskActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                        // Đợi một chút trước khi chuyển màn hình để user đọc được thông báo
                        new android.os.Handler(android.os.Looper.getMainLooper()).postDelayed(() -> {
                            startActivity(new Intent(TaskActivity.this, LoginActivity.class));
                            finish();
                        }, 2000);
                        return;
                    } else if (response.code() == 403) {
                        errorMsg = "Không có quyền thực hiện thao tác này";
                        // Không tự động logout với 403, có thể là lỗi permission
                    } else if (response.code() == 404) {
                        errorMsg = "Không tìm thấy nhiệm vụ";
                    }
                    Toast.makeText(TaskActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("TaskActivity", "Error completing task", t);
                Toast.makeText(TaskActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Refresh điểm người dùng một cách im lặng (không hiển thị lỗi nếu fail)
     */
    private void refreshUserPointsSilently() {
        String rawToken = SessionManager.getToken(this);
        if (rawToken == null) {
            return;
        }

        String authToken = rawToken.startsWith("Bearer ") ? rawToken : "Bearer " + rawToken;
        userApi.getCurrentUser(authToken).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful() && response.body() != null) {
                    User user = response.body();
                    // TODO: Nếu User model có field points, uncomment dòng dưới
                    // userPoints = user.getPoints() != null ? user.getPoints() : 0;
                    updatePointsDisplay();
                    Log.d("TaskActivity", "Silently refreshed user points");
                } else {
                    // Không hiển thị lỗi, chỉ log
                    Log.d("TaskActivity", "Silent refresh failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                // Không hiển thị lỗi, chỉ log
                Log.d("TaskActivity", "Silent refresh error", t);
            }
        });
    }

    private void updatePointsDisplay() {
        txtUserPoints.setText(String.valueOf(userPoints));
    }
}