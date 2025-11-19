package com.example.comicapp.ui.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.comicapp.R;

public class ChonNhacActivity extends AppCompatActivity {

    private String[] nhacBinhYen = {"Lofi Mưa", "Tháng Năm Bình Yên", "Chờ Anh Về", "Gió Nhẹ Thôi", "Miên Man"};
    private String[] nhacBuon = {"Một Mình", "Nhớ Em", "Trời Mưa Buồn", "Cơn Gió Lạnh", "Xa"};
    private String[] nhacVui = {"Ngày Hôm Nay Vui Quá", "Đi Đu Đưa Đi", "Cười Lên Nào", "Bay Cùng Em", "Happy Day"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chon_nhac);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> onBackPressed());

        setupMood(R.id.btnBinhYen, R.id.listBinhYen, nhacBinhYen);
        setupMood(R.id.btnBuon, R.id.listBuon, nhacBuon);
        setupMood(R.id.btnVui, R.id.listVui, nhacVui);
    }

    private void setupMood(int buttonId, int listId, String[] songs) {
        Button btn = findViewById(buttonId);
        LinearLayout listLayout = findViewById(listId);

        btn.setOnClickListener(v -> {
            if (listLayout.getVisibility() == View.GONE) {
                listLayout.removeAllViews();
                for (String song : songs) {
                    Button songBtn = new Button(this);
                    songBtn.setText("🎵 " + song);
                    songBtn.setAllCaps(false);

                    listLayout.addView(songBtn);
                }
                listLayout.setVisibility(View.VISIBLE);
            } else {
                listLayout.setVisibility(View.GONE);
            }
        });
    }
}
