package com.example.comicapp.ui.ActivityUser;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList;

import com.example.comicapp.R;
import com.example.comicapp.api.ChapterApi;
import com.example.comicapp.api.ChapterDetailApi;
import com.example.comicapp.data.model.Chapter;
import com.example.comicapp.data.model.ChapterDetail;
import com.example.comicapp.network.RetrofitClient;
import com.example.comicapp.utils.SessionManager;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReadComicActivity extends AppCompatActivity {

    public static final String EXTRA_COMIC_TITLE = "extra_comic_title";
    public static final String EXTRA_CHAPTER_NUMBER = "extra_chapter_number";
    public static final String EXTRA_CHAPTER_TITLE = "extra_chapter_title";

    private MaterialButton btnReaction;
    private PopupWindow reactionPopup;
    private String currentReaction = null;

    // Các view hiển thị dữ liệu
    private TextView tvTitle, tvChapterName, tvContent;

    // Dữ liệu hiện tại
    private String storyTitle;
    private Long currentChapterId;
    private int currentChapterNumber;
    private String currentChapterTitle;

    // Danh sách chương để biết chương trước/sau (sẽ load 1 lần)
    private List<ChapterDetail> allChaptersInStory = new ArrayList<>();
    private Long storyId;  // cần để mở ChapterList

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_comic);

        // Ánh xạ view
        tvTitle = findViewById(R.id.tvTitle);
        tvChapterName = findViewById(R.id.tvChapterName);
        tvContent = findViewById(R.id.tvContent);

        // Nhận dữ liệu từ Intent (từ ComicDetail hoặc ChapterList)
        Intent intent = getIntent();
        storyTitle = intent.getStringExtra("storyTitle");
        currentChapterId = intent.getLongExtra("chapterId", -1L);
        currentChapterNumber = intent.getIntExtra("chapterNumber", 1);
        currentChapterTitle = intent.getStringExtra("chapterTitle");
        storyId = intent.getLongExtra("storyId", -1L);  // nếu có truyền từ nơi khác

        // Hiển thị tên truyện ngay
        if (tvTitle != null && storyTitle != null) {
            tvTitle.setText(storyTitle);
        }

        // Hiển thị tên chương đẹp (tránh lặp "Chương 2: Chương 2")
        updateChapterTitleDisplay();

        // Nếu có chapterId → load nội dung chương hiện tại
        if (currentChapterId != -1L) {
            loadChapterContent(currentChapterId);
        } else {
            Toast.makeText(this, "Không có thông tin chương!", Toast.LENGTH_SHORT).show();
        }

        // ================== NÚT MENU (3 GẠCH) → MỞ DANH SÁCH CHƯƠNG ==================
        findViewById(R.id.btnMenu).setOnClickListener(v -> {
            Intent menuIntent = new Intent(ReadComicActivity.this, ChapterListActivity.class);
            menuIntent.putExtra("storyId", storyId);
            menuIntent.putExtra("storyTitle", storyTitle);
            // Có thể truyền chương hiện tại để highlight
            menuIntent.putExtra("currentChapterNumber", currentChapterNumber);
            startActivity(menuIntent);
        });

        // ================== NÚT LUI CHƯƠNG ==================
        findViewById(R.id.btnPrev).setOnClickListener(v -> goToPreviousChapter());

        // ================== NÚT TỚI CHƯƠNG ==================
        findViewById(R.id.btnNext).setOnClickListener(v -> goToNextChapter());

        // ================== GIỮ NGUYÊN CODE CŨ CỦA BẠN ==================
        findViewById(R.id.btnMusic).setOnClickListener(v -> {
            Intent musicIntent = new Intent(ReadComicActivity.this, SelectMusicActivity.class);
            startActivity(musicIntent);
        });

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        initReactionButton();
    }

    // Cập nhật tiêu đề chương đẹp: "Chương X: Tên chương" hoặc chỉ "Chương X" nếu không có tên
    private void updateChapterTitleDisplay() {
        if (tvChapterName == null) return;

        String displayTitle = "Chương " + currentChapterNumber;
        if (currentChapterTitle != null && !currentChapterTitle.trim().isEmpty()) {
            displayTitle += ": " + currentChapterTitle.trim();
        }
        tvChapterName.setText(displayTitle);
    }

    // Load nội dung chi tiết của một chương theo ID
    private void loadChapterContent(Long chapterId) {
        if (chapterId == null || chapterId == -1) {
            Toast.makeText(this, "Không có ID chương!", Toast.LENGTH_SHORT).show();
            return;
        }

        String token = "Bearer " + SessionManager.getToken(this);
        ChapterDetailApi api = RetrofitClient.getInstance().create(ChapterDetailApi.class);

        api.getChapterDetail(token, chapterId).enqueue(new Callback<ChapterDetail>() {
            @Override
            public void onResponse(Call<ChapterDetail> call, Response<ChapterDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ChapterDetail detail = response.body();

                    // Cập nhật dữ liệu hiện tại
                    currentChapterId = detail.getId();
                    currentChapterNumber = detail.getChapterNumber();
                    currentChapterTitle = detail.getTitle();

                    // Cập nhật giao diện
                    updateChapterTitleDisplay();

                    if (tvContent != null) {
                        String content = detail.getContent();
                        tvContent.setText(content != null && !content.isEmpty() ? content : "Chưa có nội dung chương này.");
                    }
                } else {
                    Toast.makeText(ReadComicActivity.this, "Không tải được nội dung chương", Toast.LENGTH_SHORT).show();
                    if (tvContent != null) tvContent.setText("Lỗi tải nội dung.");
                }
            }

            @Override
            public void onFailure(Call<ChapterDetail> call, Throwable t) {
                Toast.makeText(ReadComicActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_LONG).show();
                if (tvContent != null) tvContent.setText("Không kết nối được server.");
            }
        });
    }

    // Chuyển đến chương trước
    private void goToPreviousChapter() {
        if (currentChapterNumber <= 1) {
            Toast.makeText(this, "Đây là chương đầu tiên!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tìm chương có số nhỏ hơn 1 đơn vị
        Long prevChapterId = findChapterIdByNumber(currentChapterNumber - 1);
        if (prevChapterId != null) {
            loadChapterContent(prevChapterId);
        } else {
            Toast.makeText(this, "Không tìm thấy chương trước!", Toast.LENGTH_SHORT).show();
        }
    }

    // Chuyển đến chương sau
    private void goToNextChapter() {
        Long nextChapterId = findChapterIdByNumber(currentChapterNumber + 1);
        if (nextChapterId != null) {
            loadChapterContent(nextChapterId);
        } else {
            Toast.makeText(this, "Không còn chương tiếp theo hoặc chương bị khóa!", Toast.LENGTH_SHORT).show();
        }
    }

    // Tìm chapterId theo số chương (cần load trước danh sách chương của truyện)
    // Lưu ý: Để chính xác, bạn có thể load toàn bộ chương 1 lần ở onCreate nếu cần
    // Hiện tại mình dùng cách đơn giản: gọi API tìm chương theo số (giả sử backend hỗ trợ, nếu không thì load full list)
    private Long findChapterIdByNumber(int targetNumber) {
        // Cách đơn giản: gọi API lấy danh sách chương và tìm
        String token = "Bearer " + SessionManager.getToken(this);
        ChapterApi chapterApi = RetrofitClient.getInstance().create(ChapterApi.class);

        chapterApi.getChaptersByStoryId(token, storyId).enqueue(new Callback<List<Chapter>>() {
            @Override
            public void onResponse(Call<List<Chapter>> call, Response<List<Chapter>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Chapter ch : response.body()) {
                        if (ch.getChapterNumber() == targetNumber && !ch.isLocked()) {
                            loadChapterContent(ch.getId());
                            return;
                        }
                    }
                    Toast.makeText(ReadComicActivity.this, "Không tìm thấy chương phù hợp hoặc chương bị khóa!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Chapter>> call, Throwable t) {
                Toast.makeText(ReadComicActivity.this, "Lỗi tải danh sách chương", Toast.LENGTH_SHORT).show();
            }
        });

        return null; // tạm return null, logic thực tế xử lý trong callback
    }

    // ================== GIỮ NGUYÊN TOÀN BỘ PHẦN REACTION CỦA BẠN ==================
    private void initReactionButton() {
        btnReaction = findViewById(R.id.btn_reaction);
        View popupView = getLayoutInflater().inflate(R.layout._reaction_popup, null);
        reactionPopup = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        reactionPopup.setElevation(30f);
        reactionPopup.setAnimationStyle(android.R.style.Animation_Toast);

        String[] reactions = { "love", "haha", "wow", "sad", "angry", "care"};
        int[] bigIcons = { R.drawable.ic_heart_default, R.drawable.react_haha, R.drawable.react_wow, R.drawable.react_sad, R.drawable.react_angry, R.drawable.react_care };

        for (int i = 0; i < reactions.length; i++) {
            ImageView iv = popupView.findViewWithTag(reactions[i]);
            int index = i;
            iv.setOnClickListener(v -> {
                selectReaction(reactions[index], bigIcons[index]);
                reactionPopup.dismiss();
            });
            iv.setOnTouchListener((v, event) -> {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE) {
                    v.animate().scaleX(1.6f).scaleY(1.6f).translationY(-30f).setDuration(150).start();
                } else {
                    v.animate().scaleX(1f).scaleY(1f).translationY(0f).setDuration(150).start();
                }
                return false;
            });
        }

        btnReaction.setOnLongClickListener(v -> {
            btnReaction.post(() -> {
                int btnWidth = btnReaction.getWidth();
                int popupWidth = popupView.getMeasuredWidth();
                int offsetX = -(popupWidth / 2) + (btnWidth / 2);
                int offsetY = -250;
                reactionPopup.showAsDropDown(btnReaction, offsetX, offsetY);
                vibrate(50);
                popupView.setScaleX(0.6f);
                popupView.setScaleY(0.6f);
                popupView.animate().scaleX(1f).scaleY(1f).setDuration(200).start();
            });
            return true;
        });

        btnReaction.setOnClickListener(v -> {
            if (currentReaction == null || !"like".equals(currentReaction)) {
                selectReaction("like", R.drawable.react_like);
            } else {
                resetReaction();
            }
        });
    }

    private void selectReaction(String type, int bigIcon) {
        currentReaction = type;
        btnReaction.setText(getReactionText(type));
        btnReaction.setIconResource(bigIcon);
        btnReaction.setTextColor(Color.parseColor("#E74C3C"));
        btnReaction.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#E74C3C")));
        btnReaction.setIconTint(ColorStateList.valueOf(Color.parseColor("#E74C3C")));

        AnimatorSet set = new AnimatorSet();
        set.playTogether(
                ObjectAnimator.ofFloat(btnReaction, "scaleX", 1f, 1.3f, 1f),
                ObjectAnimator.ofFloat(btnReaction, "scaleY", 1f, 1.3f, 1f)
        );
        set.setDuration(300).start();

        Toast.makeText(this, "Đã thả " + getReactionText(type) + "!", Toast.LENGTH_SHORT).show();
    }

    private void resetReaction() {
        currentReaction = null;
        btnReaction.setText("Thả cảm xúc");
        btnReaction.setIconResource(R.drawable.ic_heart_default);
        btnReaction.setTextColor(Color.parseColor("#E74C3C"));
        btnReaction.setStrokeColor(ColorStateList.valueOf(Color.parseColor("#E74C3C")));
        btnReaction.setIconTint(ColorStateList.valueOf(Color.parseColor("#E74C3C")));
    }

    private String getReactionText(String type) {
        switch (type) {
            case "like": return "Thích";
            case "love": return "Yêu thích";
            case "haha": return "Haha";
            case "wow": return "Wow";
            case "sad": return "Buồn";
            case "angry": return "Phẫn nộ";
            case "care": return "Thương";
            default: return "Thả cảm xúc";
        }
    }

    private void vibrate(long ms) {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (v == null || !v.hasVibrator()) return;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(ms, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(ms);
        }
    }
}