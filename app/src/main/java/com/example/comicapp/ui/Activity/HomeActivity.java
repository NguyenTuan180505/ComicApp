// src/main/java/com/example/comicapp/ui/home/HomeActivity.java
package com.example.comicapp.ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.comicapp.R;
import com.example.comicapp.data.adapter.EmotionPagerAdapter;
import com.example.comicapp.data.adapter.StoryAdapter;
import com.example.comicapp.data.model.Story;
import com.example.comicapp.ui.Activity.TaskActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseNavigationActivity {

    RecyclerView rvHot, rvNew;
    TabLayout tabEmotion;
    ViewPager2 viewPagerEmotion;
    EditText edtSearch;
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Ánh xạ view
        rvHot = findViewById(R.id.rvHotStories);
        rvNew = findViewById(R.id.rvNewStories);
        tabEmotion = findViewById(R.id.tabEmotion);
        viewPagerEmotion = findViewById(R.id.viewPagerEmotion);
        edtSearch = findViewById(R.id.edtSearch);
        bottomNav = findViewById(R.id.bottomNav);

        // Setup Hot Stories
        rvHot.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rvHot.setAdapter(new StoryAdapter(getDummyStories()));

        // Setup New Stories
        rvNew.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL, false));
        rvNew.setAdapter(new StoryAdapter(getDummyStories()));

        // Setup ViewPager2 + TabLayout cho cảm xúc
        EmotionPagerAdapter pagerAdapter = new EmotionPagerAdapter(this);
        viewPagerEmotion.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabEmotion, viewPagerEmotion, (tab, position) ->
                tab.setText(pagerAdapter.getEmotionTitle(position))
        ).attach();

        // Sự kiện chọn tab
        tabEmotion.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String emotion = pagerAdapter.getEmotionTitle(tab.getPosition());
                // TODO: Gọi API load truyện theo cảm xúc
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Bottom Navigation
        setupBottomNavigation(R.id.nav_home);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNav.setSelectedItemId(R.id.nav_home);
    }

    @Override
    protected int getCurrentNavItemId() {
        return 0;
    }

//    private void setupBottomNavigation() {
//        bottomNav.setOnItemSelectedListener(item -> {
//            int id = item.getItemId();
//            if (id == R.id.nav_home) return true;
//
//            if (id == R.id.nav_tasks) {
//                startActivity(new Intent(this, TaskActivity.class)
//                        .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
//                overridePendingTransition(0, 0);
//                return true;
//            }
//            if (id == R.id.nav_account) {
//                startActivity(new Intent(this, AccountActivity.class)
//                        .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
//                overridePendingTransition(0, 0);
//                return true;
//            }
//            if (id == R.id.nav_fav) {
//                startActivity(new Intent(this, FollowActivity.class)
//                        .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
//                overridePendingTransition(0, 0);
//                return true;
//            }
//            return false;
//        });
//    }

    public List<Story> getDummyStories() {
        List<Story> list = new ArrayList<>();
        list.add(new Story("Thám tử lừng danh Conan", "Aoyama Gosho", R.drawable.conan));
        list.add(new Story("One Piece", "Eiichiro Oda", R.drawable.onepiece));
        list.add(new Story("Naruto", "Masashi Kishimoto", R.drawable.naruto));
        return list;
    }
}