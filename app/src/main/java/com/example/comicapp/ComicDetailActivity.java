package com.example.comicapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ComicDetailActivity extends AppCompatActivity {

    private ImageView ivThumbnail;
    private TextView tvTitle, tvChapters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_detail);

        ivThumbnail = findViewById(R.id.ivThumbnail);
        tvTitle = findViewById(R.id.tvTitle);
        tvChapters = findViewById(R.id.tvChapters);

        // Get data from intent
        String title = getIntent().getStringExtra("title");
        int chapters = getIntent().getIntExtra("chapters", 0);
        String thumbnail = getIntent().getStringExtra("thumbnail");

        // Set data to views
        tvTitle.setText("Truyện: " + title);
        tvChapters.setText("Chap: " + chapters);
        int imageId = getResources().getIdentifier(thumbnail, "drawable", getPackageName());
        ivThumbnail.setImageResource(imageId);
    }
}