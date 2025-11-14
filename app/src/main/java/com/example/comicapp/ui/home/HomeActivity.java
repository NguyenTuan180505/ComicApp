package com.example.comicapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicapp.R;
import com.example.comicapp.data.model.Story;
import com.example.comicapp.data.adapter.StoryAdapter;
import com.example.comicapp.ui.task.TaskActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    RecyclerView rvHot, rvEmotion, rvNew;
    TabLayout tabEmotion;
    EditText edtSearch;
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        rvHot = findViewById(R.id.rvHotStories);
        rvEmotion = findViewById(R.id.rvEmotionStories);
        rvNew = findViewById(R.id.rvNewStories);
        tabEmotion = findViewById(R.id.tabEmotion);
        edtSearch = findViewById(R.id.edtSearch);
        bottomNav = findViewById(R.id.bottomNav);

        // Tabs cảm xúc
        String[] emotions = {"Vui", "Buồn", "Hồi hộp", "Lãng mạn"};
        for (String emo : emotions) {
            tabEmotion.addTab(tabEmotion.newTab().setText(emo));
        }

        // Adapter mẫu
        rvHot.setAdapter(new StoryAdapter(getDummyStories()));
        rvHot.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        rvEmotion.setAdapter(new StoryAdapter(getDummyStories()));
        rvEmotion.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        rvNew.setAdapter(new StoryAdapter(getDummyStories()));
        rvNew.setLayoutManager(new LinearLayoutManager(this));

        // Sự kiện chọn tab cảm xúc
        tabEmotion.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) {
                // TODO: Gọi API gợi ý theo cảm xúc
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Bottom Navigation
        setupBottomNavigation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Đánh dấu tab Home khi quay lại màn hình
        bottomNav.setSelectedItemId(R.id.nav_home);
    }

    private void setupBottomNavigation() {
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                // Đã ở màn hình Home
                return true;
            }
            else if (id == R.id.nav_tasks) {
                // Chuyển sang màn hình Nhiệm vụ
                Intent intent = new Intent(HomeActivity.this, TaskActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                overridePendingTransition(0, 0); // Không có animation
                return true;
            }
            // Bỏ comment khi có các Activity khác
            /*
            else if (id == R.id.nav_favorite) {
                Intent intent = new Intent(HomeActivity.this, FavoriteActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;
            }
            else if (id == R.id.nav_account) {
                Intent intent = new Intent(HomeActivity.this, AccountActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                overridePendingTransition(0, 0);
                return true;
            }
            */

            return false;
        });
    }

    private List<Story> getDummyStories() {
        ArrayList<Story> storyList = new ArrayList<>();
        storyList.add(new Story("Thám tử lừng danh Conan", "Aoyama Gosho", R.drawable.conan));
        storyList.add(new Story("One Piece", "Eiichiro Oda", R.drawable.onepiece));
        storyList.add(new Story("Naruto", "Masashi Kishimoto", R.drawable.naruto));
        return storyList;
    }
}