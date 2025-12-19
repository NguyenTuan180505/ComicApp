package com.example.comicapp.ui.ActivityUser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicapp.R;
import com.example.comicapp.data.adapter.ChapterAdapterUser;
import com.example.comicapp.data.model.Chapter;
import com.example.comicapp.data.model.Story;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ComicDetailActivity extends AppCompatActivity {

    Button btnReadNow;
    ImageButton btnFavorite, btnBack, btnAddComment, btnSendComment;
    LinearLayout layoutInputComment, commentContainer;
    EditText edtComment;
    TextView tvCommentTitle ,tvViewAllChapters;

    private Story story;
    private int commentCount = 0;
    private boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_detail);

        // NHẬN DỮ LIỆU TRUYỆN TỪ INTENT
        story = getIntent().getParcelableExtra("story");
        if (story == null) {
            Toast.makeText(this, "Không tải được thông tin truyện!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // --- ÁNH XẠ VIEW ---
        btnReadNow = findViewById(R.id.btnReadNow);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnBack = findViewById(R.id.btnBack);
        btnAddComment = findViewById(R.id.btnAddComment);
        btnSendComment = findViewById(R.id.btnSendComment);
        layoutInputComment = findViewById(R.id.layoutInputComment);
        commentContainer = findViewById(R.id.commentContainer);
        edtComment = findViewById(R.id.edtComment);
        tvCommentTitle = findViewById(R.id.tvCommentTitle);
        tvViewAllChapters=findViewById(R.id.tvViewAllChapters);
        // Trong onCreate() của ComicDetailActivity
        RecyclerView rcvChapters = findViewById(R.id.rcvChapters);

        rcvChapters.setHasFixedSize(false);
        rcvChapters.setNestedScrollingEnabled(true);
// Giả lập dữ liệu chapter (sau này thay bằng story.getChapters())
        List<Chapter> chapters = new ArrayList<>();
        for (int i = 1; i <= 15; i++) { // chỉ hiển thị 15 chương mới nhất
            String name = i % 5 == 0 ? "Boss cuối xuất hiện" : "Hành trình của Sung Jin Woo";
            chapters.add(new Chapter(name, i, i <= 3 ? "vừa xong" : (i <= 7 ? "1 tuần trước" : "1 tháng trước"), 20 + i));
        }
// Đảo ngược để chương mới nhất lên đầu
        Collections.reverse(chapters);

        ChapterAdapterUser adapter = new ChapterAdapterUser(this, chapters, story);
        rcvChapters.setAdapter(adapter);
        rcvChapters.setLayoutManager(new LinearLayoutManager(this));
//        rcvChapters.setNestedScrollingEnabled(false);

// TẮT SCROLL
//        rcvChapters.setNestedScrollingEnabled(false);
        // NÚT "ĐỌC NGAY" → CHUYỂN SANG MÀN HÌNH ĐỌC TRUYỆN
        btnReadNow.setOnClickListener(v -> {
            Intent intent = new Intent(ComicDetailActivity.this, ReadComicActivity.class);
            intent.putExtra("story", story); // Truyền cả object Story
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left); // Hiệu ứng đẹp
        });

// Ví dụ từ ComicDetailActivity
        tvViewAllChapters.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChapterListActivity.class);
//            intent.putExtra(ChapterListActivity.EXTRA_STORY, story);
//            intent.putExtra(ChapterListActivity.EXTRA_CURRENT_CHAPTER_NUMBER, 125); // chương đang đọc
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });
        btnFavorite.setOnClickListener(v -> {
            isFavorite = !isFavorite;
            btnFavorite.setImageResource(isFavorite
                    ? R.mipmap.ic_heart_filled
                    : R.mipmap.ic_heart_outline);

            Toast.makeText(this, isFavorite
                    ? "Đã thêm vào yêu thích ❤️"
                    : "Đã bỏ yêu thích", Toast.LENGTH_SHORT).show();

            v.animate().scaleX(1.3f).scaleY(1.3f).setDuration(150)
                    .withEndAction(() -> v.animate().scaleX(1f).scaleY(1f).setDuration(100)).start();
        });

        // NÚT QUAY LẠI
        btnBack.setOnClickListener(v -> finish());

        // MỞ/HIỆN BÌNH LUẬN
        btnAddComment.setOnClickListener(v -> {
            boolean isVisible = layoutInputComment.getVisibility() == View.VISIBLE;
            layoutInputComment.setVisibility(isVisible ? View.GONE : View.VISIBLE);
            if (!isVisible) edtComment.requestFocus();
        });

        // GỬI BÌNH LUẬN
        btnSendComment.setOnClickListener(v -> {
            String content = edtComment.getText().toString().trim();
            if (content.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập nội dung bình luận", Toast.LENGTH_SHORT).show();
                return;
            }
            addComment("Bạn", content);
            edtComment.setText("");
            layoutInputComment.setVisibility(View.GONE);
        });
    }

    // THÊM BÌNH LUẬN MỚI
    private void addComment(String username, String content) {
        LinearLayout commentLayout = new LinearLayout(this);
        commentLayout.setOrientation(LinearLayout.HORIZONTAL);
        commentLayout.setPadding(16, 16, 16, 16);

        ImageButton avatar = new ImageButton(this);
        avatar.setLayoutParams(new LinearLayout.LayoutParams(100, 100));
        avatar.setScaleType(ImageButton.ScaleType.CENTER_CROP);
        avatar.setImageResource(R.drawable.ic_person_24); // Thêm ảnh avatar nếu có
        avatar.setBackground(null);

        LinearLayout textLayout = new LinearLayout(this);
        textLayout.setOrientation(LinearLayout.VERTICAL);
        textLayout.setPadding(20, 0, 0, 0);

        TextView tvUser = new TextView(this);
        tvUser.setText(username);
        tvUser.setTextSize(15);
        tvUser.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
        tvUser.setTypeface(tvUser.getTypeface(), android.graphics.Typeface.BOLD);

        TextView tvContent = new TextView(this);
        tvContent.setText(content);
        tvContent.setTextSize(14);
        tvContent.setPadding(0, 8, 0, 20);

        textLayout.addView(tvUser);
        textLayout.addView(tvContent);
        commentLayout.addView(avatar);
        commentLayout.addView(textLayout);
        commentContainer.addView(commentLayout);

        commentCount++;
        tvCommentTitle.setText("Bình Luận (" + commentCount + ")");
        Toast.makeText(this, "Đã gửi bình luận!", Toast.LENGTH_SHORT).show();
    }
}