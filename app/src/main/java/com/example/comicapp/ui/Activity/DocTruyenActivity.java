package com.example.comicapp.ui.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.comicapp.R;

public class DocTruyenActivity extends AppCompatActivity {

    private CardView layoutPrediction;
    private GridLayout gridOptions;
    private LinearLayout layoutUserChoice;
    private TextView tvQuestion, tvUserChoice, tvRank;
    private Button btnPrediction;   // nút mới dưới cùng

    private boolean isPredictionVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_truyen);

        // Các nút cũ
        Button btnMusic = findViewById(R.id.btnMusic);
        ImageView btnBack = findViewById(R.id.btnBack);
        btnPrediction = findViewById(R.id.btnPrediction);   // ← nút mới

        // Các view trong box dự đoán
        layoutPrediction = findViewById(R.id.layout_prediction);
        tvQuestion = findViewById(R.id.tv_question);
        gridOptions = findViewById(R.id.grid_options);
        layoutUserChoice = findViewById(R.id.layout_user_choice);
        tvUserChoice = findViewById(R.id.tv_user_choice);
        tvRank = findViewById(R.id.tv_rank);

        // Sự kiện các nút cũ
        btnMusic.setOnClickListener(v -> startActivity(new Intent(this, ChonNhacActivity.class)));
        btnBack.setOnClickListener(v -> onBackPressed());

        // ========== NÚT DỰ ĐOÁN – CHỈ BẤM LÀ HIỆN/ẨN BOX ==========
        btnPrediction.setOnClickListener(v -> {
            if (isPredictionVisible) {
                layoutPrediction.setVisibility(View.GONE);
                isPredictionVisible = false;
            } else {
                layoutPrediction.setVisibility(View.VISIBLE);
                isPredictionVisible = true;
                if (gridOptions.getChildCount() == 0) {
                    loadPrediction();   // load lần đầu
                }
            }
        });

        // Nếu muốn chap nào không có dự đoán thì ẩn luôn nút
        // btnPrediction.setVisibility(View.GONE);
    }

    private void loadPrediction() {
        // TODO: Sau này lấy từ server. Giờ demo
        tvQuestion.setText("Chap sau nam chính sẽ chọn ai để cứu?");
        String[] options = {"A. Tiểu Vân", "B. Bạch Liên hoa", "C. Cả hai", "D. Chết luôn"};
        int[] colors = {0xFFe74c3c, 0xFF3498db, 0xFF2ecc71, 0xFFf39c12};

        for (int i = 0; i < options.length; i++) {
            Button btn = new Button(this);
            btn.setText(options[i]);
            btn.setTextColor(Color.WHITE);
            btn.setBackgroundColor(colors[i]);
            btn.setAllCaps(false);
            btn.setTypeface(null, Typeface.BOLD);
            btn.setTextSize(15);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(i % 2, 1f);
            params.rowSpec = GridLayout.spec(i / 2);
            params.setMargins(12, 12, 12, 12);
            btn.setLayoutParams(params);

            final int index = i;
            btn.setOnClickListener(v -> {
                tvUserChoice.setText("Bạn đã chọn: " + options[index] + " Correct");
                tvRank.setText("Hạng #8");
                layoutUserChoice.setVisibility(View.VISIBLE);
                // TODO: Gửi lựa chọn lên server ở đây
            });

            gridOptions.addView(btn);
        }
    }
}