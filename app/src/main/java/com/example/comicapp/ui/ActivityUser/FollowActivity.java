// FollowActivity.java
package com.example.comicapp.ui.ActivityUser;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicapp.R;
import com.example.comicapp.api.FavoriteApi;
import com.example.comicapp.data.adapter.StoryAdapter;
import com.example.comicapp.data.model.Story;
import com.example.comicapp.dto.response.FavoriteResponse;
import com.example.comicapp.network.RetrofitClient;
import com.example.comicapp.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
//        setupStoryLists();
    }
    public void openComicDetail(Story story) {
        Intent intent = new Intent(this, ComicDetailActivity.class);
        intent.putExtra("story", (Parcelable) story);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    /**
     * GỌI API LẤY DANH SÁCH TRUYỆN YÊU THÍCH
     */
    private void setupStoryLists() {
        FavoriteApi favoriteApi = RetrofitClient.getInstance().create(FavoriteApi.class);
        String token = "Bearer " + SessionManager.getToken(this);

        favoriteApi.getFavoriteStories(token).enqueue(new Callback<List<FavoriteResponse>>() {
            @Override
            public void onResponse(Call<List<FavoriteResponse>> call, Response<List<FavoriteResponse>> response) {
                Log.d("FavoriteAPI", "getFavoriteStories response code: " + response.code());

                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(FollowActivity.this, "Không tải được danh sách yêu thích", Toast.LENGTH_SHORT).show();
                    displayStories(new ArrayList<>());
                    return;
                }

                List<FavoriteResponse> favorites = response.body();
                Log.d("FavoriteAPI", "Nhận được " + favorites.size() + " bản ghi yêu thích");

                // Lấy danh sách storyId từ favorites
                List<Long> favoriteStoryIds = new ArrayList<>();
                for (FavoriteResponse fav : favorites) {
                    if (fav.getStoryId() != null) {
                        favoriteStoryIds.add(fav.getStoryId());
                        Log.d("FavoriteAPI", "Favorite storyId: " + fav.getStoryId());
                    }
                }

                if (favoriteStoryIds.isEmpty()) {
                    Toast.makeText(FollowActivity.this, "Bạn chưa có truyện yêu thích nào", Toast.LENGTH_SHORT).show();
                    displayStories(new ArrayList<>());
                    return;
                }

                // Lấy tất cả stories để tìm những cái có trong danh sách yêu thích
                favoriteApi.getAllStories(token).enqueue(new Callback<List<Story>>() {
                    @Override
                    public void onResponse(Call<List<Story>> call, Response<List<Story>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Story> allStories = response.body();
                            List<Story> favoriteStories = new ArrayList<>();

                            for (Story story : allStories) {
                                if (story.getId() != null && favoriteStoryIds.contains(story.getId())) {
                                    favoriteStories.add(story);
                                }
                            }

                            Log.d("FavoriteAPI", "Đã match được " + favoriteStories.size() + " truyện yêu thích");
                            displayStories(favoriteStories);
                        } else {
                            Toast.makeText(FollowActivity.this, "Không tải được danh sách truyện", Toast.LENGTH_SHORT).show();
                            displayStories(new ArrayList<>());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Story>> call, Throwable t) {
                        Toast.makeText(FollowActivity.this, "Lỗi tải truyện: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        displayStories(new ArrayList<>());
                    }
                });
            }

            @Override
            public void onFailure(Call<List<FavoriteResponse>> call, Throwable t) {
                Toast.makeText(FollowActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                displayStories(new ArrayList<>());
            }
        });
    }

    /**
     * Hiển thị danh sách stories lên RecyclerView
     */
    private void displayStories(List<Story> stories) {
        Log.d("FavoriteAPI", "Displaying " + stories.size() + " favorite stories");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(FollowActivity.this, 2);
        rvFavStories.setLayoutManager(gridLayoutManager);
        StoryAdapter favAdapter = new StoryAdapter(stories);
        favAdapter.setOnStoryClickListener(story -> openComicDetail(story));
        rvFavStories.setAdapter(favAdapter);

        if (stories.isEmpty()) {
            Toast.makeText(this, "Không có truyện yêu thích nào", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected int getCurrentNavItemId() {
        return R.id.nav_fav; // ĐÚNG!

    }
    @Override
    protected void onResume() {
        super.onResume();

        // GỌI API LẠI MỖI KHI QUAY LẠI TRANG
        setupStoryLists();

        // Đánh dấu lại tab
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(R.id.nav_fav);
        }
    }

}