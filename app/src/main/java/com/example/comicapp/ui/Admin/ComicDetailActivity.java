package com.example.comicapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class ComicDetailActivity extends AppCompatActivity {

    private ImageView ivThumbnail;
    private TextView tvTitle, tvChapters;
    private RecyclerView recyclerChapters;
    private FloatingActionButton fabAddChapter;
    private java.util.List<Integer> chapterNumbers;
    private ChapterAdapter adapter;
    private int chaptersCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_detail);

        ivThumbnail = findViewById(R.id.ivThumbnail);
        tvTitle = findViewById(R.id.tvTitle);
        tvChapters = findViewById(R.id.tvChapters);
        recyclerChapters = findViewById(R.id.recyclerChapters);
        fabAddChapter = findViewById(R.id.fabAddChapter);

        // Get data from intent
        String title = getIntent().getStringExtra("title");
        int chapters = getIntent().getIntExtra("chapters", 0);
        String thumbnail = getIntent().getStringExtra("thumbnail");

        // Set data to views
        tvTitle.setText("Truyện: " + title);
        android.content.SharedPreferences prefs = getSharedPreferences("chapters", MODE_PRIVATE);
        chaptersCount = prefs.getInt(title + "_chapter_count", chapters);
        tvChapters.setText("Chap: " + chaptersCount);
        int imageId = getResources().getIdentifier(thumbnail, "drawable", getPackageName());
        ivThumbnail.setImageResource(imageId);

        recyclerChapters.setLayoutManager(new LinearLayoutManager(this));
        chapterNumbers = new java.util.ArrayList<>();
        for (int i = 1; i <= chaptersCount; i++) {
            chapterNumbers.add(i);
        }
        adapter = new ChapterAdapter(this, chapterNumbers, chapterNumber -> {
            BottomSheetDialog dialog = new BottomSheetDialog(ComicDetailActivity.this);
            View sheet = LayoutInflater.from(ComicDetailActivity.this).inflate(R.layout.activity_edit_chapter, null);
            dialog.setContentView(sheet);
            EditText et = sheet.findViewById(R.id.etContent);
            Button btn = sheet.findViewById(R.id.btnSave);
            String key = title + "_chapter_" + chapterNumber;
            String existing = prefs.getString(key, "");
            et.setText(existing);
            btn.setOnClickListener(v -> {
                String content = et.getText().toString();
                prefs.edit().putString(key, content).apply();
                dialog.dismiss();
            });
            dialog.show();
        });
        recyclerChapters.setAdapter(adapter);

        fabAddChapter.setOnClickListener(v -> {
            chaptersCount++;
            tvChapters.setText("Chap: " + chaptersCount);
            chapterNumbers.add(chaptersCount);
            adapter.notifyItemInserted(chapterNumbers.size() - 1);
            prefs.edit().putInt(title + "_chapter_count", chaptersCount).apply();
        });
    }
}