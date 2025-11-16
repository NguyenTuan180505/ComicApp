package com.example.comicapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DocTruyenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_truyen);

        Button btnMusic = findViewById(R.id.btnMusic);
        Button btnComment = findViewById(R.id.btnComment);
        ImageView btnBack = findViewById(R.id.btnBack);

        btnMusic.setOnClickListener(v -> {
            Intent i = new Intent(DocTruyenActivity.this, ChonNhacActivity.class);
            startActivity(i);
        });

        btnComment.setOnClickListener(v ->
                btnComment.setText("💬 Bình luận demo")
        );

        btnBack.setOnClickListener(v -> onBackPressed());
        Intent i = new Intent(DocTruyenActivity.this, ComicDetailActivity.class);
        startActivity(i);

    }
}
