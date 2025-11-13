package com.example.comicapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {
    Button btnStart, btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        btnStart = findViewById(R.id.btnStart);
        btnSignup = findViewById(R.id.btnSignup);

        btnStart.setOnClickListener(v -> {
            Toast.makeText(this, "Đi tới trang chính!", Toast.LENGTH_SHORT).show();
            // startActivity(new Intent(this, MainActivity.class));
        });

        btnSignup.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
