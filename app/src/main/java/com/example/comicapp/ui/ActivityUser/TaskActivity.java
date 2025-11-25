package com.example.comicapp.ui.ActivityUser;

import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicapp.R;
import com.example.comicapp.data.model.Task;
import com.example.comicapp.data.adapter.TaskAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class TaskActivity extends BaseNavigationActivity {

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
        setupBottomNavigation(R.id.nav_tasks);
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