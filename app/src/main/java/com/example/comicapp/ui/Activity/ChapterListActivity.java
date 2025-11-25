package com.example.comicapp.ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.comicapp.R;
import java.util.ArrayList;
import java.util.List;

public class ChapterListActivity extends AppCompatActivity {

    public static final String EXTRA_COMIC_TITLE = "extra_comic_title";
    public static final String EXTRA_CURRENT_CHAPTER = "extra_current_chapter";

    private String comicTitle = "Tên truyện";
    private int currentChapter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_list);

        // Nhận dữ liệu từ ReadComicActivity
        Intent i = getIntent();
        if (i != null) {
            comicTitle = i.getStringExtra(EXTRA_COMIC_TITLE);
            currentChapter = i.getIntExtra(EXTRA_CURRENT_CHAPTER, 1);
        }

        TextView tvTitle = findViewById(R.id.tvComicTitle);
        tvTitle.setText(comicTitle != null ? comicTitle : "Danh sách chương");

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        RecyclerView rv = findViewById(R.id.rvChapters);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new ChapterAdapter(generateDummyChapters(150), currentChapter));
    }

    private List<Chapter> generateDummyChapters(int count) {
        List<Chapter> list = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            list.add(new Chapter(i, "Chương " + i + ": Tiêu đề chương cực hay ở đây nè", "25/11/2025"));
        }
        return list;
    }

    class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.VH> {
        private final List<Chapter> data;
        private final int current;

        ChapterAdapter(List<Chapter> d, int c) {
            this.data = d;
            this.current = c;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            return new VH(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chapter_card, parent, false));
        }

        @Override
        public void onBindViewHolder(VH h, int pos) {
            Chapter chap = data.get(pos);
            h.tvTitle.setText(chap.title);
            h.tvDate.setText(chap.date);

            if (chap.number == current) {
                h.itemView.setBackgroundResource(R.drawable.bg_chapter_highlight);
                h.tvTitle.setTextColor(0xFF673AB7);
            } else {
                h.itemView.setBackgroundResource(0);
                h.tvTitle.setTextColor(0xFF1A1A1A);
            }

            h.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(ChapterListActivity.this, ReadComicActivity.class);
                intent.putExtra(ReadComicActivity.EXTRA_COMIC_TITLE, comicTitle);
                intent.putExtra(ReadComicActivity.EXTRA_CHAPTER_NUMBER, chap.number);
                intent.putExtra(ReadComicActivity.EXTRA_CHAPTER_TITLE, chap.title);
                startActivity(intent);
                finish();
            });
        }

        @Override
        public int getItemCount() { return data.size(); }

        class VH extends RecyclerView.ViewHolder {
            TextView tvTitle, tvDate;
            VH(View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tvChapterTitle);
                tvDate = itemView.findViewById(R.id.tvChapterDate);
            }
        }
    }

    static class Chapter {
        int number;
        String title, date;
        Chapter(int n, String t, String d) {
            number = n; title = t; date = d;
        }
    }
}