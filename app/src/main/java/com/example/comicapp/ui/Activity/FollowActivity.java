// FollowActivity.java
package com.example.comicapp.ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicapp.R;
import com.example.comicapp.data.adapter.StoryAdapter;
import com.example.comicapp.data.model.Story;
import com.example.comicapp.ui.Activity.BaseNavigationActivity;

import java.util.ArrayList;
import java.util.List;

public class FollowActivity extends BaseNavigationActivity {
    RecyclerView rvFavStories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_follow);
        rvFavStories = findViewById(R.id.rvFavStories);
        // KHÔNG SET padding bottom nữa
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.followLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        setupBottomNavigation(R.id.nav_fav);
        setupStoryLists();
    }
    private void setupStoryLists() {
        List<Story> stories = getDummyStories();

        // Hot Stories
        rvFavStories.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        StoryAdapter favAdapter = new StoryAdapter(stories);
        favAdapter.setOnStoryClickListener(story -> openComicDetail(story));
        rvFavStories.setAdapter(favAdapter);

    }
    public void openComicDetail(Story story) {
        Intent intent = new Intent(this, ComicDetailActivity.class);
        intent.putExtra("story", story);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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


    @Override
    protected int getCurrentNavItemId() {
        return R.id.nav_fav; // ĐÚNG!
    }
}