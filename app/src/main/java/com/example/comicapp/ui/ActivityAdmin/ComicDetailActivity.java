package com.example.comicapp.ui.ActivityAdmin;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.comicapp.R;

public class ComicDetailActivity extends AppCompatActivity {

    private ImageView imgCover;
    private TextView tvTitle;
    private TextView tvViewCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_detail);

        imgCover = findViewById(R.id.imgCover);
        tvTitle = findViewById(R.id.tvTitle);
        tvViewCount = findViewById(R.id.tvViewCount);

        String title = getIntent().getStringExtra("title");
        int chapters = getIntent().getIntExtra("chapters", 0);
        String thumbnail = getIntent().getStringExtra("thumbnail");

        tvTitle.setText(title);
        tvViewCount.setText(chapters + " Chapters");
        int imageId = getResources().getIdentifier(thumbnail, "drawable", getPackageName());
        if (imageId != 0) {
            imgCover.setImageResource(imageId);
        }
    }
}
