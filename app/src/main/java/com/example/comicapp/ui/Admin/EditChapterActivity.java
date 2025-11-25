package com.example.comicapp.ui.Admin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.comicapp.R;

public class EditChapterActivity extends AppCompatActivity {

    private EditText etContent;
    private Button btnSave;
    private String comicTitle;
    private int chapterNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_chapter);
        etContent = findViewById(R.id.etContent);
        btnSave = findViewById(R.id.btnSave);
        comicTitle = getIntent().getStringExtra("comic_title");
        chapterNumber = getIntent().getIntExtra("chapter_number", 0);
        String key = buildKey(comicTitle, chapterNumber);
        SharedPreferences prefs = getSharedPreferences("chapters", MODE_PRIVATE);
        String existing = prefs.getString(key, "");
        etContent.setText(existing);
        btnSave.setOnClickListener(v -> {
            String content = etContent.getText().toString();
            prefs.edit().putString(key, content).apply();
            finish();
        });
    }

    private String buildKey(String title, int chapter) {
        return title + "_chapter_" + chapter;
    }
}