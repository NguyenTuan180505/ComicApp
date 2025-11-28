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
import android.widget.TextView;          // ← THÊM DÒNG NÀY
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.ColorStateList; // ĐÃ CÓ RỒI – QUAN TRỌNG NHẤT

import com.example.comicapp.R;
import com.google.android.material.button.MaterialButton;

public class ReadComicActivity extends AppCompatActivity {


    public static final String EXTRA_COMIC_TITLE     = "extra_comic_title";
    public static final String EXTRA_CHAPTER_NUMBER  = "extra_chapter_number";
    public static final String EXTRA_CHAPTER_TITLE   = "extra_chapter_title";

    private MaterialButton btnReaction;
    private PopupWindow reactionPopup;
    private String currentReaction = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_comic);


        findViewById(R.id.btnMusic).setOnClickListener(v -> {
            Intent intent = new Intent(ReadComicActivity.this, SelectMusicActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Nút Menu → mở danh sách chương
        findViewById(R.id.btnMenu).setOnClickListener(v -> {
            Intent intent = new Intent(this, ChapterListActivity.class);

            TextView tvTitle = findViewById(R.id.tvTitle);
            String comicName = tvTitle.getText().toString();

            TextView tvChap = findViewById(R.id.tvChapterName);
            String chapText = tvChap.getText().toString();
            int currentChap = 1;
            try {
                currentChap = Integer.parseInt(chapText.replaceAll("\\D+", ""));
            } catch (Exception ignored) {}

            intent.putExtra(ChapterListActivity.EXTRA_COMIC_TITLE, comicName);
            intent.putExtra(ChapterListActivity.EXTRA_CURRENT_CHAPTER, currentChap);
            startActivity(intent);
        });

        // Nhận dữ liệu khi chọn chương từ danh sách
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(EXTRA_CHAPTER_TITLE)) {
                String title = intent.getStringExtra(EXTRA_CHAPTER_TITLE);
                ((TextView) findViewById(R.id.tvChapterName)).setText(title);
            }
            if (intent.hasExtra(EXTRA_COMIC_TITLE)) {
                String title = intent.getStringExtra(EXTRA_COMIC_TITLE);
                ((TextView) findViewById(R.id.tvTitle)).setText(title);
            }
        }

        initReactionButton();
    }

    private void initReactionButton() {
        btnReaction = findViewById(R.id.btn_reaction);


        View popupView = getLayoutInflater().inflate(R.layout._reaction_popup, null);
        reactionPopup = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        reactionPopup.setElevation(30f);
        reactionPopup.setAnimationStyle(android.R.style.Animation_Toast);

        String[] reactions = { "love", "haha", "wow", "sad", "angry", "care"};
        int[] bigIcons = {
                R.drawable.ic_heart_default,
                R.drawable.react_haha,
                R.drawable.react_wow,
                R.drawable.react_sad,
                R.drawable.react_angry,
                R.drawable.react_care,
        };

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
            // Đo chiều rộng của nút và popup để căn giữa
            btnReaction.post(() -> {
                int btnWidth = btnReaction.getWidth();
                int popupWidth = popupView.getMeasuredWidth();

                int offsetX = -(popupWidth / 2) + (btnWidth / 2);  // căn giữa hoàn hảo
                int offsetY = -250;  // đẩy lên trên nút

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

        // Hiệu ứng nhảy nhẹ khi chọn
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
            case "laugh": return "Cười lớn";
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

    private void animatePopupEntrance(View popupView) {
        popupView.setScaleX(0.7f);
        popupView.setScaleY(0.7f);
        popupView.setAlpha(0f);
        popupView.animate()
                .scaleX(1f).scaleY(1f)
                .alpha(1f)
                .setDuration(200)
                .start();
    }
}