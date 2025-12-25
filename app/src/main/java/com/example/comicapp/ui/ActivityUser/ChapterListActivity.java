// ChapterListActivity.java
package com.example.comicapp.ui.ActivityUser;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicapp.R;
import com.example.comicapp.api.ChapterApi;
import com.example.comicapp.data.adapter.ChapterAdapterUser;
import com.example.comicapp.data.model.Chapter;
import com.example.comicapp.data.model.Story;
import com.example.comicapp.network.RetrofitClient;
import com.example.comicapp.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChapterListActivity extends AppCompatActivity {

    private RecyclerView rvChapters;
    private ChapterAdapterUser adapter;
    private List<Chapter> chapterList = new ArrayList<>();

    private Long storyId = -1L;
    private int currentChapterNumber = -1;
    private String comicTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_list);

        rvChapters = findViewById(R.id.rvChapters);
        rvChapters.setLayoutManager(new LinearLayoutManager(this));

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            storyId = intent.getLongExtra("storyId", -1L);
            currentChapterNumber = intent.getIntExtra("currentChapterNumber", -1);
            comicTitle = intent.getStringExtra("comicTitle");
        }

        if (storyId == -1L) {
            Toast.makeText(this, "Không có thông tin truyện!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        loadChaptersFromApi();

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

                    // Sắp xếp chương mới nhất lên đầu
                    chapterList.sort((c1, c2) -> Integer.compare(c2.getChapterNumber(), c1.getChapterNumber()));

                    // Tạo object Story tạm để truyền vào adapter (chỉ cần id và title)
                    Story story = new Story();
                    story.setId(storyId);
                    story.setTitle(comicTitle);

                    // Dùng chung ChapterAdapterUser như ở màn hình yêu thích
                    adapter = new ChapterAdapterUser(ChapterListActivity.this, chapterList, story);

                    rvChapters.setAdapter(adapter);

                    // Highlight chương đang đọc (nếu có)
                    if (currentChapterNumber != -1 && adapter != null) {
                        for (int i = 0; i < chapterList.size(); i++) {
                            if (chapterList.get(i).getChapterNumber() == currentChapterNumber) {
                                // AdapterUser không có highlight built-in → bạn có thể thêm sau nếu cần
                                // Hiện tại giữ nguyên, hoặc scroll đến vị trí
                                rvChapters.scrollToPosition(i);
                                break;
                            }
                        }
                    }

                    // Cập nhật tiêu đề
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
                Toast.makeText(ChapterListActivity.this, "Lỗi kết nối server: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}