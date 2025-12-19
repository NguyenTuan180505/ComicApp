package com.example.comicapp.ui.ActivityUser;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.comicapp.R;
import com.example.comicapp.ui.Fragment.BannerFragment;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

public class WelcomeActivity extends AppCompatActivity {

    private ViewPager2 bannerPager;
    private WormDotsIndicator dotsIndicator;
    private Button btnStart, btnSignup;

    // Thay bằng tên ảnh thật của bạn
    private final int[] bannerImages = {
            R.drawable.comic_banner1,
            R.drawable.comic_banner2,
            R.drawable.comic_banner3
            // Thêm bao nhiêu ảnh cũng được
    };

    private final Handler autoSlideHandler = new Handler(Looper.getMainLooper());
    private final Runnable autoSlideRunnable = new Runnable() {
        int currentPage = 0;
        @Override
        public void run() {
            if (currentPage >= bannerImages.length) {
                currentPage = 0;
            }
            bannerPager.setCurrentItem(currentPage++, true);
            autoSlideHandler.postDelayed(this, 4000); // 4 giây đổi ảnh
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        bannerPager = findViewById(R.id.bannerPager);
        dotsIndicator = findViewById(R.id.dotsIndicator);
        btnStart = findViewById(R.id.btnStart);
        btnSignup = findViewById(R.id.btnSignup);

        // Setup ViewPager2 cho banner
        bannerPager.setAdapter(new androidx.viewpager2.adapter.FragmentStateAdapter(this) {
            @NonNull
            @Override
            public androidx.fragment.app.Fragment createFragment(int position) {
                return BannerFragment.newInstance(bannerImages[position]);
            }

            @Override
            public int getItemCount() {
                return bannerImages.length;
            }
        });

        // Gắn chấm tròn
        dotsIndicator.attachTo(bannerPager);

        // Tự động lướt
        bannerPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                autoSlideHandler.removeCallbacks(autoSlideRunnable);
                autoSlideHandler.postDelayed(autoSlideRunnable, 4000);
            }
        });

        // Bắt đầu tự động lướt
        autoSlideHandler.postDelayed(autoSlideRunnable, 4000);

        // Nút Bắt đầu
        btnStart.setOnClickListener(v -> {
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            finish();
        });

        // Nút Đăng ký
        btnSignup.setOnClickListener(v -> {
            startActivity(new Intent(WelcomeActivity.this, RegisterActivity.class));
        });
    }

    @Override
    protected void onDestroy() {
        autoSlideHandler.removeCallbacks(autoSlideRunnable);
        super.onDestroy();
    }
}
