package com.example.comicapp.ui.ActivityAdmin;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicapp.R;
import com.example.comicapp.data.adapter.ChapterAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class ComicDetailAdActivity extends AppCompatActivity {

    private ImageView ivThumbnail;
    private TextView tvTitle;
    private TextView tvChapters;
    private RecyclerView recyclerChapters;
    private FloatingActionButton fabAddChapter;
    private MaterialButton btnBackToManage;

    private List<Integer> chapterNumbers = new ArrayList<>();
    private int chaptersCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_detail_ad);

        ivThumbnail = findViewById(R.id.ivThumbnail);
        tvTitle = findViewById(R.id.tvTitle);
        tvChapters = findViewById(R.id.tvChapters);
        recyclerChapters = findViewById(R.id.recyclerChapters);
        fabAddChapter = findViewById(R.id.fabAddChapter);
        btnBackToManage = findViewById(R.id.btnBackToManage);

        String title = getIntent().getStringExtra("title");
        int chapters = getIntent().getIntExtra("chapters", 0);
        String thumbnail = getIntent().getStringExtra("thumbnail");

        tvTitle.setText(title);

        SharedPreferences prefs = getSharedPreferences("chapters", MODE_PRIVATE);
        chaptersCount = prefs.getInt(title + "_chapter_count", chapters);
        tvChapters.setText("Chap: " + chaptersCount);

        if (thumbnail != null) {
            if (thumbnail.startsWith("content://") || thumbnail.startsWith("file://")) {
                ivThumbnail.setImageURI(Uri.parse(thumbnail));
            } else {
                int imageId = getResources().getIdentifier(thumbnail, "drawable", getPackageName());
                if (imageId != 0) {
                    ivThumbnail.setImageResource(imageId);
                }
            }
        }

        recyclerChapters.setLayoutManager(new LinearLayoutManager(this));
        chapterNumbers.clear();
        for (int i = 1; i <= chaptersCount; i++) {
            chapterNumbers.add(i);
        }
        final ChapterAdapter adapter = new ChapterAdapter(this, chapterNumbers, new ChapterAdapter.OnChapterClickListener() {
            @Override
            public void onChapterClick(int chapterNumber) {
                BottomSheetDialog dialog = new BottomSheetDialog(ComicDetailAdActivity.this);
                View sheet = LayoutInflater.from(ComicDetailAdActivity.this)
                        .inflate(R.layout.activity_edit_chapter, null);
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
            }

            @Override
            public void onChapterDelete(int chapterNumber, int position) {
                chapterNumbers.remove(position);
                chaptersCount = chapterNumbers.size();
                tvChapters.setText("Chap: " + chaptersCount);
                RecyclerView.Adapter rvAdapter = recyclerChapters.getAdapter();
                if (rvAdapter instanceof ChapterAdapter) {
                    rvAdapter.notifyItemRemoved(position);
                }
                prefs.edit()
                        .remove(title + "_chapter_" + chapterNumber)
                        .putInt(title + "_chapter_count", chaptersCount)
                        .apply();
            }
        });
        recyclerChapters.setAdapter(adapter);

        if (fabAddChapter != null) {
            fabAddChapter.setOnClickListener(v -> {
                chaptersCount++;
                tvChapters.setText("Chap: " + chaptersCount);
                chapterNumbers.add(chaptersCount);
                adapter.notifyItemInserted(chapterNumbers.size() - 1);
                prefs.edit().putInt(title + "_chapter_count", chaptersCount).apply();
            });
        }

        btnBackToManage.setOnClickListener(v -> {
            finish();
        });
    }
}
