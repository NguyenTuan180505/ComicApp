package com.example.comicapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class TaskActivity extends AppCompatActivity {

    RecyclerView rvTasks;
    TextView txtUserPoints;
    BottomNavigationView bottomNav;
    int userPoints = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        rvTasks = findViewById(R.id.rvTasks);
        txtUserPoints = findViewById(R.id.txtUserPoints);
        bottomNav = findViewById(R.id.bottomNav);

        // Hiển thị điểm ban đầu
        updatePointsDisplay();

        List<Task> tasks = getDummyTasks();
        TaskAdapter adapter = new TaskAdapter(tasks, this::onTaskCompleted);
        rvTasks.setAdapter(adapter);
        rvTasks.setLayoutManager(new LinearLayoutManager(this));

        // Setup Bottom Navigation
        setupBottomNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Đánh dấu tab Tasks khi quay lại màn hình
        bottomNav.setSelectedItemId(R.id.nav_tasks);
    }

    private void setupBottomNavigation() {
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                // Chuyển về màn hình Home
                Intent intent = new Intent(TaskActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                overridePendingTransition(0, 0); // Không có animation
                return true;
            }
            else if (id == R.id.nav_tasks) {
                // Đã ở màn hình Tasks
                return true;
            }
            // Bỏ comment khi có các Activity khác
            /*
            else if (id == R.id.nav_favorite) {
                Intent intent = new Intent(TaskActivity.this, FavoriteActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;
            }
            else if (id == R.id.nav_account) {
                Intent intent = new Intent(TaskActivity.this, AccountActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;
            }
            */

            return false;
        });
    }

    private List<Task> getDummyTasks() {
        List<Task> list = new ArrayList<>();
        list.add(new Task("Đọc 3 truyện", "Hoàn thành 3 truyện bất kỳ", 50, false));
        list.add(new Task("Đăng nhập mỗi ngày", "Điểm thưởng cho đăng nhập", 10, true));
        list.add(new Task("Chia sẻ truyện yêu thích", "Chia sẻ 1 truyện cho bạn bè", 20, false));
        list.add(new Task("Đánh giá truyện", "Đánh giá 5 sao cho truyện", 15, false));
        return list;
    }

    private void onTaskCompleted(Task task) {
        if (!task.isCompleted()) {
            userPoints += task.getReward();
            task.setCompleted(true);
            updatePointsDisplay();
            // TODO: Gọi API cập nhật điểm người dùng và UserTasks
        }
    }

    private void updatePointsDisplay() {
        txtUserPoints.setText(String.valueOf(userPoints));
    }
}