package com.example.comicapp.ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.comicapp.R;
import com.example.comicapp.data.model.Story;

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
        // Trong DocTruyenActivity.java → thêm đoạn này vào onCreate()
        Story story = getIntent().getParcelableExtra("story");
        if (story != null) {
            // Hiển thị tên truyện ở đâu đó (TextView, Toolbar,...)
            TextView tvTitle = findViewById(R.id.tvTitle);
            TextView tvChapter = findViewById(R.id.tvChapterName);
            if (tvTitle != null && tvChapter != null) {
                tvChapter.setText("Chương 1");
            }
        }

        btnComment.setOnClickListener(v ->
                btnComment.setText("💬 Bình luận demo")
        );

        btnBack.setOnClickListener(v -> onBackPressed());


    }
}
