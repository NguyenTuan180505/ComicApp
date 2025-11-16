package com.example.comicapp;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class AccountActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account);

        // Nút lưu
        findViewById(R.id.btnSave).setOnClickListener(v ->
                Toast.makeText(this, "Đã lưu thông tin!", Toast.LENGTH_SHORT).show()
        );

        // Nút quay lại → quay về UserActivity (hoặc finish nếu từ User)
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            finish(); // Quay lại màn hình trước (thường là UserActivity)
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
    }
}