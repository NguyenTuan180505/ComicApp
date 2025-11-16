package com.example.comicapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // === BOTTOM NAVIGATION ===
        LinearLayout navHome = findViewById(R.id.nav_home);
        LinearLayout navTask = findViewById(R.id.nav_task);
        LinearLayout navFavourite = findViewById(R.id.nav_favourite);
        LinearLayout navAccount = findViewById(R.id.nav_account);

        // Nhấn "Yêu thích" → FollowActivity
        navFavourite.setOnClickListener(v -> {
            startActivity(new Intent(this, FollowActivity.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish(); // Tùy chọn: thoát UserActivity
        });

        // Nhấn "Tài khoản" → đang ở đây (không làm gì)
        navAccount.setOnClickListener(v -> { /* Đang ở trang tài khoản */ });

        // (Tùy chọn) Các tab khác
        if (navHome != null) {
            navHome.setOnClickListener(v -> {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
            });
        }

        if (navTask != null) {
            navTask.setOnClickListener(v -> {
                startActivity(new Intent(this, TaskActivity.class));
                finish();
            });
        }

        // === MENU ITEM: CẬP NHẬT THÔNG TIN ===
        TextView tvUpdateInfo = findViewById(R.id.tv_update_info);
        if (tvUpdateInfo != null) {
            tvUpdateInfo.setOnClickListener(v -> {
                startActivity(new Intent(this, AccountActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            });
        }
    }
}