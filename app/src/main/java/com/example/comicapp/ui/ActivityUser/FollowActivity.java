// FollowActivity.java
package com.example.comicapp.ui.ActivityUser;

import android.content.Intent;
import android.os.Bundle;
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
        setupStoryLists();
    }
    public void openComicDetail(Story story) {
        Intent intent = new Intent(this, ComicDetailActivity.class);
        intent.putExtra("story", story);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    /**
     * GỌI API LẤY DANH SÁCH TRUYỆN YÊU THÍCH
     * Strategy: 
     * 1. Gọi getFavoriteStories để lấy list favorite IDs
     * 2. Gọi getAllStories để lấy tất cả stories với đầy đủ thông tin
     * 3. Match favorite IDs với story IDs để lấy những stories được favorite
     */
    private void setupStoryLists() {
        FavoriteApi favoriteApi = RetrofitClient.getInstance().create(FavoriteApi.class);
        // Lấy token gốc từ SessionManager
        String rawToken = SessionManager.getToken(this);
        if (rawToken == null) {
            Toast.makeText(this, "Vui lòng đăng nhập để xem truyện yêu thích", Toast.LENGTH_SHORT).show();
            return;
        }
        String token = "Bearer " + rawToken;

        // Bước 1: Lấy danh sách favorite IDs
        favoriteApi.getFavoriteStories(token).enqueue(new Callback<List<Story>>() {
            @Override
            public void onResponse(Call<List<Story>> call, Response<List<Story>> response) {
                Log.d("FavoriteAPI", "getFavoriteStories response code: " + response.code());
                if (response.isSuccessful() && response.body() != null) {
                    List<Story> favoriteIds = response.body();
                    Log.d("FavoriteAPI", "Received " + favoriteIds.size() + " favorite IDs");
                    
                    // Lấy danh sách IDs từ favorites
                    // Backend có thể trả về storyId hoặc id (của bảng favorites)
                    List<Long> favoriteStoryIds = new ArrayList<>();
                    for (Story fav : favoriteIds) {
                        // Ưu tiên dùng storyId, nếu không có thì dùng id
                        Long storyId = fav.getStoryId() != null ? fav.getStoryId() : fav.getId();
                        if (storyId != null) {
                            favoriteStoryIds.add(storyId);
                            Log.d("FavoriteAPI", "Favorite storyId: " + storyId + 
                                    " (from storyId=" + fav.getStoryId() + ", id=" + fav.getId() + ")");
                        }
                    }
                    
                    if (favoriteStoryIds.isEmpty()) {
                        Toast.makeText(FollowActivity.this, "Bạn chưa có truyện yêu thích nào", Toast.LENGTH_SHORT).show();
                        displayStories(new ArrayList<>());
                        return;
                    }
                    
                    // Bước 2: Lấy tất cả stories để match với favorites
                    favoriteApi.getAllStories(token).enqueue(new Callback<List<Story>>() {
                        @Override
                        public void onResponse(Call<List<Story>> call, Response<List<Story>> response) {
                            Log.d("FavoriteAPI", "getAllStories response code: " + response.code());
                            if (response.isSuccessful() && response.body() != null) {
                                List<Story> allStories = response.body();
                                Log.d("FavoriteAPI", "Received " + allStories.size() + " total stories");
                                
                                // Debug: Log tất cả story IDs từ getAllStories
                                Log.d("FavoriteAPI", "Favorite IDs to match: " + favoriteStoryIds);
                                for (Story story : allStories) {
                                    Log.d("FavoriteAPI", "AllStory ID: " + story.getId() + 
                                            ", title=" + story.getTitle());
                                }
                                
                                // Bước 3: Match favorite IDs với story IDs
                                List<Story> favoriteStories = new ArrayList<>();
                                for (Story story : allStories) {
                                    if (story.getId() != null && favoriteStoryIds.contains(story.getId())) {
                                        favoriteStories.add(story);
                                        Log.d("FavoriteAPI", "Matched story: id=" + story.getId() + 
                                                ", title=" + story.getTitle() + 
                                                ", author=" + story.getAuthor() + 
                                                ", coverImage=" + story.getCoverImage());
                                    } else if (story.getId() != null) {
                                        Log.d("FavoriteAPI", "Story ID " + story.getId() + " not in favorites list");
                                    }
                                }
                                
                                Log.d("FavoriteAPI", "Matched " + favoriteStories.size() + " stories out of " + favoriteStoryIds.size() + " favorites");
                                displayStories(favoriteStories);
                            } else {
                                Toast.makeText(FollowActivity.this, "Không tải được danh sách truyện", Toast.LENGTH_SHORT).show();
                            }
                        }
                        
                        @Override
                        public void onFailure(Call<List<Story>> call, Throwable t) {
                            Toast.makeText(FollowActivity.this, "Lỗi khi tải danh sách truyện: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    String errorMsg = "Không tải được danh sách yêu thích (code " + response.code() + ")";
                    if (response.errorBody() != null) {
                        try {
                            String errorBody = response.errorBody().string();
                            Log.e("FavoriteAPI", "Error body: " + errorBody);
                            errorMsg += "\n" + errorBody;
                        } catch (Exception e) {
                            Log.e("FavoriteAPI", "Error reading error body", e);
                        }
                    }
                    Toast.makeText(FollowActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<List<Story>> call, Throwable t) {
                Toast.makeText(FollowActivity.this, "Lỗi kết nối server: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
}