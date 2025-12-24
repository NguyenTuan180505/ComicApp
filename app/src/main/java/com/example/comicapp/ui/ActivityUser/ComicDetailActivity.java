package com.example.comicapp.ui.ActivityUser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.comicapp.R;
import com.example.comicapp.api.ChapterApi;
import com.example.comicapp.api.CommentApi;
import com.example.comicapp.api.FavoriteApi;
import com.example.comicapp.data.adapter.ChapterAdapterUser;
import com.example.comicapp.data.adapter.CommentAdapter;
import com.example.comicapp.data.model.Chapter;
import com.example.comicapp.data.model.Comment;
import com.example.comicapp.data.model.Story;
import com.example.comicapp.dto.request.CommentRequest;
import com.example.comicapp.dto.request.FavoriteRequest;
import com.example.comicapp.network.RetrofitClient;
import com.example.comicapp.utils.SessionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComicDetailActivity extends AppCompatActivity {

    Button btnReadNow;
    ImageButton btnFavorite, btnBack, btnAddComment, btnSendComment;
    LinearLayout layoutInputComment, commentContainer;
    EditText edtComment;
    TextView tvCommentTitle, tvViewAllChapters, tvTitle, tvDescription, tvAuthor;
    ImageView imgCover;

    private boolean isFavorite = false; // trạng thái hiện tại
    private boolean isLoadingFavorite = false;

    private RecyclerView rcvComments;
    private CommentAdapter commentAdapter;
    private List<Comment> commentList = new ArrayList<>();

    private Story story;
    private int commentCount = 0;

    // === THÊM BIẾN ĐỂ LƯU DANH SÁCH CHƯƠNG (dùng cho nút Đọc ngay) ===
    private List<Chapter> chapters = new ArrayList<>();

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

        // Sau khi set tiêu đề, mô tả, ảnh bìa...
        checkFavoriteStatus();

        // --- ÁNH XẠ VIEW ---
        btnReadNow = findViewById(R.id.btnReadNow);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnBack = findViewById(R.id.btnBack);
        btnAddComment = findViewById(R.id.btnAddComment);
        btnSendComment = findViewById(R.id.btnSendComment);
        layoutInputComment = findViewById(R.id.layoutInputComment);
        edtComment = findViewById(R.id.edtComment);
        tvCommentTitle = findViewById(R.id.tvCommentTitle);
        tvViewAllChapters = findViewById(R.id.tvViewAllChapters);
        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDescription);
        tvAuthor = findViewById(R.id.tvAuthor);

        // Trong onCreate() của ComicDetailActivity
        RecyclerView rcvChapters = findViewById(R.id.rcvChapters);
        imgCover = findViewById(R.id.imgCover);

        if (tvTitle != null) tvTitle.setText(story.getTitle());
        if (tvDescription != null) tvDescription.setText(story.getDescription());
        if (tvAuthor != null) tvAuthor.setText(story.getAuthor());

        String imageUrl = story.getCoverImage();
        if (imageUrl != null && imageUrl.contains("http://localhost")) {
            imageUrl = imageUrl.replace("http://localhost", "http://10.0.2.2");
        }
        Glide.with(this)
                .load(imageUrl)
                // .placeholder(R.drawable.placeholder_cover)
                .into(imgCover);

        rcvComments = findViewById(R.id.rcvComments);
        // Thêm RecyclerView vào layout
        rcvComments.setLayoutManager(new LinearLayoutManager(this));
        commentAdapter = new CommentAdapter();
        rcvComments.setAdapter(commentAdapter);

        loadChaptersFromApi();
        loadCommentsFromApi();

        // TẮT SCROLL
        // rcvChapters.setNestedScrollingEnabled(false);

        // ================== SỬA NÚT "ĐỌC NGAY" ==================
        btnReadNow.setOnClickListener(v -> {
            if (chapters == null || chapters.isEmpty()) {
                Toast.makeText(this, "Truyện chưa có chương nào để đọc!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Lấy chương đầu tiên (sau khi đã sort chương mới nhất lên đầu)
            Chapter firstChapter = chapters.get(0);

            Intent intent = new Intent(ComicDetailActivity.this, ReadComicActivity.class);
            intent.putExtra("storyTitle", story.getTitle());
            intent.putExtra("storyId", story.getId());  // ← THÊM DÒNG NÀY
            intent.putExtra("chapterId", firstChapter.getId());
            intent.putExtra("chapterNumber", firstChapter.getChapterNumber());
            intent.putExtra("chapterTitle", firstChapter.getTitle() != null ? firstChapter.getTitle() : "");
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // ================== SỬA NÚT "XEM TẤT CẢ CHƯƠNG" ==================
        tvViewAllChapters.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChapterListActivity.class);
            intent.putExtra("storyId", story.getId());
            intent.putExtra("storyTitle", story.getTitle());  // ← THÊM DÒNG NÀY ĐỂ TRUYỀN TÊN TRUYỆN
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        });

        // ================== GIỮ NGUYÊN TOÀN BỘ CODE CŨ TỪ ĐÂY TRỞ XUỐNG ==================
        btnFavorite.setOnClickListener(v -> {
            if (isLoadingFavorite) return; // đang xử lý, không cho click tiếp
            if (story == null || story.getId() == null) {
                Toast.makeText(this, "Không có thông tin truyện!", Toast.LENGTH_SHORT).show();
                return;
            }
            isLoadingFavorite = true;
            String token = "Bearer " + SessionManager.getToken(this);
            FavoriteApi api = RetrofitClient.getInstance().create(FavoriteApi.class);
            if (!isFavorite) {
                // Đang thêm vào yêu thích
                FavoriteRequest request = new FavoriteRequest(story.getId());
                api.addFavorite(token, request).enqueue(new retrofit2.Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        isLoadingFavorite = false;
                        if (response.isSuccessful()) {
                            isFavorite = true;
                            updateFavoriteButton();
                            Toast.makeText(ComicDetailActivity.this, "Đã thêm vào yêu thích ❤️", Toast.LENGTH_SHORT).show();
                            animateHeart(v);
                        } else {
                            Toast.makeText(ComicDetailActivity.this, "Thêm yêu thích thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        isLoadingFavorite = false;
                        Toast.makeText(ComicDetailActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // Đang bỏ yêu thích
                api.removeFavorite(token, story.getId()).enqueue(new retrofit2.Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        isLoadingFavorite = false;
                        if (response.isSuccessful()) {
                            isFavorite = false;
                            updateFavoriteButton();
                            Toast.makeText(ComicDetailActivity.this, "Đã bỏ yêu thích", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ComicDetailActivity.this, "Bỏ yêu thích thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        isLoadingFavorite = false;
                        Toast.makeText(ComicDetailActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
                    }
                });
            }
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
            String token = "Bearer " + SessionManager.getToken(this);
            CommentApi api = RetrofitClient.getInstance().create(CommentApi.class);
            CommentRequest request = new CommentRequest(story.getId(), content);
            api.postComment(token, request).enqueue(new Callback<Comment>() {
                @Override
                public void onResponse(Call<Comment> call, Response<Comment> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Comment newComment = response.body();
                        newComment.setUsername("Bạn"); // Vì backend chưa trả tên
                        commentAdapter.addComment(newComment);
                        commentCount++;
                        tvCommentTitle.setText("Bình Luận (" + commentCount + ")");
                        Toast.makeText(ComicDetailActivity.this, "Đã gửi bình luận!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ComicDetailActivity.this, "Gửi thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Comment> call, Throwable t) {
                    Toast.makeText(ComicDetailActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                }
            });
            edtComment.setText("");
            layoutInputComment.setVisibility(View.GONE);
        });
    }

    // ================== GIỮ NGUYÊN CÁC METHOD KHÁC ==================
    private void loadCommentsFromApi() {
        if (story == null || story.getId() == null) return;
        String token = "Bearer " + SessionManager.getToken(this);
        CommentApi api = RetrofitClient.getInstance().create(CommentApi.class);
        api.getCommentsByStoryId(token, story.getId()).enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Comment> commentList = response.body();
                    if (commentList.size() > 3) {
                        commentList = commentList.subList(0, 3);
                    }
                    commentAdapter.setComments(commentList);
                    int commentCount = commentList.size();
                    tvCommentTitle.setText("Bình Luận (" + commentCount + ")");
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Toast.makeText(ComicDetailActivity.this, "Lỗi tải bình luận", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkFavoriteStatus() {
        String token = "Bearer " + SessionManager.getToken(this);
        FavoriteApi api = RetrofitClient.getInstance().create(FavoriteApi.class);
        api.isFavorite(token, story.getId()).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    isFavorite = response.body();
                    updateFavoriteButton();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                // Không làm gì, giữ trạng thái mặc định
            }
        });
    }

    private void updateFavoriteButton() {
        btnFavorite.setImageResource(isFavorite ? R.mipmap.ic_heart_filled : R.mipmap.ic_heart_outline);
    }

    private void animateHeart(View v) {
        v.animate()
                .scaleX(1.3f).scaleY(1.3f)
                .setDuration(150)
                .withEndAction(() -> v.animate()
                        .scaleX(1f).scaleY(1f)
                        .setDuration(100))
                .start();
    }

    // Trong ComicDetailActivity.java
    private void loadChaptersFromApi() {
        if (story == null || story.getId() == null) {
            Toast.makeText(this, "Không có thông tin truyện!", Toast.LENGTH_SHORT).show();
            return;
        }
        String token = "Bearer " + SessionManager.getToken(this);
        ChapterApi chapterApi = RetrofitClient.getInstance().create(ChapterApi.class);
        chapterApi.getChaptersByStoryId(token, story.getId()).enqueue(new Callback<List<Chapter>>() {
            @Override
            public void onResponse(Call<List<Chapter>> call, Response<List<Chapter>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Chapter> allChapters = response.body();
                    // Sắp xếp: chương mới nhất lên đầu (chapterNumber giảm dần)
                    allChapters.sort((c1, c2) -> Integer.compare(c2.getChapterNumber(), c1.getChapterNumber()));

                    // Lưu lại toàn bộ chapters để dùng cho nút "Đọc ngay"
                    chapters = new ArrayList<>(allChapters);

                    // Chỉ hiển thị tối đa 3 chương mới nhất
                    List<Chapter> displayChapters = allChapters.size() > 3 ? allChapters.subList(0, 3) : allChapters;
                    setupChapterRecyclerView(displayChapters);
                } else {
                    Toast.makeText(ComicDetailActivity.this, "Không tải được chương", Toast.LENGTH_SHORT).show();
                    // Fallback: dùng dummy
                    setupDummyChapters();
                }
            }

            @Override
            public void onFailure(Call<List<Chapter>> call, Throwable t) {
                Toast.makeText(ComicDetailActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
                setupDummyChapters();
            }
        });
    }

    private void setupChapterRecyclerView(List<Chapter> chapters) {
        RecyclerView rcvChapters = findViewById(R.id.rcvChapters);
        rcvChapters.setLayoutManager(new LinearLayoutManager(this));
        ChapterAdapterUser adapter = new ChapterAdapterUser(this, chapters, story);
        rcvChapters.setAdapter(adapter);
    }

    private void setupDummyChapters() {
        List<Chapter> dummy = new ArrayList<>();
        for (int i = 1; i <= 15; i++) {
            Chapter c = new Chapter();
            c.setTitle("Chương " + i + ": Tiêu đề chương");
            c.setChapterNumber(i);
            c.setLocked(i > 3);
            dummy.add(c);
        }
        // Đảo ngược để chương cao nhất lên đầu
        Collections.reverse(dummy);
        setupChapterRecyclerView(dummy);
    }
}