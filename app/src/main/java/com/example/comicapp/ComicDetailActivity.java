package com.example.comicapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ComicDetailActivity extends AppCompatActivity {

    Button btnReadNow;
    ImageButton btnFavorite, btnBack, btnAddComment, btnSendComment;
    LinearLayout layoutInputComment, commentContainer;
    EditText edtComment;
    TextView tvCommentTitle;

    private int commentCount = 0; // đếm số lượng bình luận
    private boolean isFavorite = false; // trạng thái yêu thích

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comic_detail);

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

        // --- NÚT "ĐỌC NGAY" ---
        btnReadNow.setOnClickListener(v ->
                Toast.makeText(this, "Đọc ngay!", Toast.LENGTH_SHORT).show());

        // --- NÚT YÊU THÍCH (TRÁI TIM) ---
        btnFavorite.setOnClickListener(v -> {
            isFavorite = !isFavorite; // đảo trạng thái

            if (isFavorite) {
                btnFavorite.setImageResource(R.mipmap.ic_heart_filled); // icon trái tim đỏ
                Toast.makeText(this, "Đã thêm vào yêu thích ❤️", Toast.LENGTH_SHORT).show();
            } else {
                btnFavorite.setImageResource(R.mipmap.ic_heart_outline); // icon trái tim rỗng
                Toast.makeText(this, "Đã bỏ khỏi yêu thích 🤍", Toast.LENGTH_SHORT).show();
            }

            // Hiệu ứng nhỏ khi click
            v.animate().scaleX(1.2f).scaleY(1.2f).setDuration(120)
                    .withEndAction(() -> v.animate().scaleX(1f).scaleY(1f).setDuration(100));
        });

        // --- NÚT QUAY LẠI ---
        btnBack.setOnClickListener(v -> finish());

        // --- XỬ LÝ PHẦN BÌNH LUẬN ---
        btnAddComment.setOnClickListener(v -> {
            if (layoutInputComment.getVisibility() == View.GONE) {
                layoutInputComment.setVisibility(View.VISIBLE);
                edtComment.requestFocus();
            } else {
                layoutInputComment.setVisibility(View.GONE);
            }
        });

        btnSendComment.setOnClickListener(v -> {
            String content = edtComment.getText().toString().trim();
            if (content.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập nội dung bình luận", Toast.LENGTH_SHORT).show();
                return;
            }

            addComment("User ẩn danh", content);
            edtComment.setText("");
            layoutInputComment.setVisibility(View.GONE);
        });
    }

    // --- HÀM THÊM COMMENT MỚI ---
    private void addComment(String username, String content) {
        LinearLayout commentLayout = new LinearLayout(this);
        commentLayout.setOrientation(LinearLayout.HORIZONTAL);
        commentLayout.setPadding(0, 8, 0, 8);

        View avatar = new View(this);
        avatar.setLayoutParams(new LinearLayout.LayoutParams(60, 60));
        avatar.setBackgroundResource(android.R.drawable.presence_online);

        LinearLayout textLayout = new LinearLayout(this);
        textLayout.setOrientation(LinearLayout.VERTICAL);
        textLayout.setPadding(16, 0, 0, 0);

        TextView tvUser = new TextView(this);
        tvUser.setText(username);
        tvUser.setTextSize(14);
        tvUser.setTextColor(getResources().getColor(android.R.color.black));
        tvUser.setTypeface(null, android.graphics.Typeface.BOLD);

        TextView tvContent = new TextView(this);
        tvContent.setText(content);
        tvContent.setTextSize(14);
        tvContent.setTextColor(getResources().getColor(android.R.color.darker_gray));

        textLayout.addView(tvUser);
        textLayout.addView(tvContent);

        commentLayout.addView(avatar);
        commentLayout.addView(textLayout);

        commentContainer.addView(commentLayout);
        commentCount++;
        tvCommentTitle.setText("Bình Luận (" + commentCount + ")");

        Toast.makeText(this, "Đã gửi bình luận", Toast.LENGTH_SHORT).show();
    }
}
