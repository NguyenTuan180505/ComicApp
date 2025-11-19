// ui/Activity/HomeActivity.java
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

        setupStoryLists();
        setupEmotionTabs();
        setupBottomNavigation(R.id.nav_home);
    }

    private void setupStoryLists() {
        List<Story> stories = getDummyStories();

        // Hot Stories
        rvHot.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        StoryAdapter hotAdapter = new StoryAdapter(stories);
        hotAdapter.setOnStoryClickListener(story -> openComicDetail(story));
        rvHot.setAdapter(hotAdapter);

        // New Stories
        rvNew.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        StoryAdapter newAdapter = new StoryAdapter(stories);
        newAdapter.setOnStoryClickListener(story -> openComicDetail(story));
        rvNew.setAdapter(newAdapter);
    }

    public void openComicDetail(Story story) {
        Intent intent = new Intent(this, ComicDetailActivity.class);
        intent.putExtra("story", story);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void setupEmotionTabs() {
        EmotionPagerAdapter pagerAdapter = new EmotionPagerAdapter(this);
        viewPagerEmotion.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabEmotion, viewPagerEmotion, (tab, position) ->
                tab.setText(pagerAdapter.getEmotionTitle(position))
        ).attach();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(R.id.nav_home);
        }
    }

    @Override
    protected int getCurrentNavItemId() {
        return R.id.nav_home;
    }

    private List<Story> getDummyStories() {
        List<Story> list = new ArrayList<>();
        list.add(new Story("Thám tử lừng danh Conan", "Aoyama Gosho", R.drawable.conan));
        list.add(new Story("One Piece", "Eiichiro Oda", R.drawable.onepiece));
        list.add(new Story("Naruto", "Masashi Kishimoto", R.drawable.naruto));
//        list.add(new Story("Attack on Titan", "Hajime Isayama", R.drawable.aot));
//        list.add(new Story("Dragon Ball", "Akira Toriyama", R.drawable.dragonball));
//        list.add(new Story("Demon Slayer", "Koyoharu Gotouge", R.drawable.kimetsu));
        return list;
    }
}