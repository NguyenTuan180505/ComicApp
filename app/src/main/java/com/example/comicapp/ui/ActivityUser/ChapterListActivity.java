package com.example.comicapp.ui.ActivityUser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicapp.R;
import com.example.comicapp.api.ChapterApi;
import com.example.comicapp.data.model.Chapter;
import com.example.comicapp.network.RetrofitClient;
import com.example.comicapp.ui.ActivityUser.ReadComicActivity;
import com.example.comicapp.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChapterListActivity extends AppCompatActivity {

    private RecyclerView rvChapters;
    private ChapterAdapter adapter;
    private List<Chapter> chapterList = new ArrayList<>();
    private Long storyId;
    private int currentChapterNumber = -1; // -1 nghĩa là chưa có chương đang đọc
    private String comicTitle; // Lưu tên truyện để truyền cho ReadComicActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_list);

        rvChapters = findViewById(R.id.rvChapters);
        rvChapters.setLayoutManager(new LinearLayoutManager(this));

        // Nhận storyId từ ComicDetailActivity
        Intent intent = getIntent();
        if (intent != null) {
            storyId = intent.getLongExtra("storyId", -1);
            currentChapterNumber = intent.getIntExtra("currentChapterNumber", -1);
            comicTitle = intent.getStringExtra("comicTitle"); // Nhận tên truyện nếu có
        }

        if (storyId == -1) {
            Toast.makeText(this, "Không có thông tin truyện!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load danh sách chương từ API
        loadChaptersFromApi();

        // Nút back
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
    }

    private void loadChaptersFromApi() {
        String token = "Bearer " + SessionManager.getToken(this);
        ChapterApi chapterApi = RetrofitClient.getInstance().create(ChapterApi.class);

        chapterApi.getChaptersByStoryId(token, storyId).enqueue(new Callback<List<Chapter>>() {
            @Override
            public void onResponse(Call<List<Chapter>> call, Response<List<Chapter>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    chapterList = response.body();

                    // Sắp xếp: chương mới nhất lên đầu (chapterNumber giảm dần)
                    chapterList.sort((c1, c2) -> Integer.compare(c2.getChapterNumber(), c1.getChapterNumber()));

                    // Cập nhật adapter
                    adapter = new ChapterAdapter(chapterList, currentChapterNumber);
                    rvChapters.setAdapter(adapter);

                    // Cập nhật tiêu đề (nếu layout có TextView tvComicTitle)
                    TextView tvTitle = findViewById(R.id.tvComicTitle);
                    if (tvTitle != null) {
                        tvTitle.setText("Danh sách chương (" + chapterList.size() + ")");
                    }
                } else {
                    Toast.makeText(ChapterListActivity.this, "Không tải được danh sách chương", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Chapter>> call, Throwable t) {
                Toast.makeText(ChapterListActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Adapter cho danh sách chương
    class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ViewHolder> {

        private final List<Chapter> chapters;
        private final int currentChapter;

        ChapterAdapter(List<Chapter> chapters, int currentChapter) {
            this.chapters = chapters;
            this.currentChapter = currentChapter;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_chapter_card, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Chapter chapter = chapters.get(position);
            String title = null;

            if (chapter.getTitle() != null && !chapter.getTitle().isEmpty()) {
                title = chapter.getTitle();
            }

            title += chapter.isLocked() ? " (Chưa mở)" : " (Đã mở)";

            holder.tvTitle.setText(title);

            holder.tvDate.setText(formatDate(chapter.getCreatedAt()));

            // Highlight chương đang đọc
            if (currentChapter != -1 && chapter.getChapterNumber() == currentChapter) {
                holder.itemView.setBackgroundResource(R.drawable.bg_chapter_highlight);
                holder.tvTitle.setTextColor(0xFF673AB7); // Màu tím nổi bật
            } else {
                holder.itemView.setBackgroundResource(0);
                holder.tvTitle.setTextColor(0xFF1A1A1A);
            }
            holder.itemView.setAlpha(chapter.isLocked() ? 0.5f : 1f);
            // 👉 Click chương khóa → Toast

            // Click vào chương → mở ReadComicActivity
            holder.itemView.setOnClickListener(v -> {
                if (chapter.isLocked()) {
                    Toast.makeText(holder.itemView.getContext(),
                            "Chương này cần mở khóa",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(ChapterListActivity.this, ReadComicActivity.class);
                intent.putExtra("storyId", storyId);
                intent.putExtra("chapterId", chapter.getId());
                intent.putExtra("chapterNumber", chapter.getChapterNumber());
                intent.putExtra("chapterTitle", chapter.getTitle());
                // Truyền tên truyện nếu có
                if (comicTitle != null && !comicTitle.isEmpty()) {
                    intent.putExtra(ReadComicActivity.EXTRA_COMIC_TITLE, comicTitle);
                }
                startActivity(intent);
                finish(); // Đóng danh sách chương sau khi chọn
            });
        }

        @Override
        public int getItemCount() {
            return chapters.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvDate;

            ViewHolder(View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tvChapterTitle);
                tvDate = itemView.findViewById(R.id.tvChapterDate);
            }
        }

        private String formatDate(String isoDate) {
            if (isoDate == null) return "Vừa xong";
            try {
                String[] parts = isoDate.split("T");
                String date = parts[0];
                String[] dateParts = date.split("-");
                return dateParts[2] + "/" + dateParts[1] + "/" + dateParts[0];
            } catch (Exception e) {
                return "Vừa xong";
            }
        }
    }
}