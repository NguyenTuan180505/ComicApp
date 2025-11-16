package com.example.comicapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FollowActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_follow);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.followLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Nhấn "Tài khoản" ở thanh dưới → Chuyển sang UserActivity
        LinearLayout navAccount = findViewById(R.id.nav_account);
        navAccount.setOnClickListener(v -> {
            Intent intent = new Intent(FollowActivity.this, UserActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            // Không finish() để có thể quay lại bằng Back
        });

        // (Tùy chọn) Nhấn "Yêu thích" → highlight (đang ở đây)
        LinearLayout navFavourite = findViewById(R.id.nav_favourite);
        if (navFavourite != null) {
            navFavourite.setOnClickListener(v -> {
                // Đang ở FollowActivity → không làm gì hoặc reload
            });
        }
    }
}