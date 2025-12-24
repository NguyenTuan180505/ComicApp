// ui/Activity/HomeActivity.java
package com.example.comicapp.ui.ActivityUser;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.EditText;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.comicapp.R;
import com.example.comicapp.api.EmotionApi;
import com.example.comicapp.api.StoryApi;
import com.example.comicapp.data.adapter.EmotionPagerAdapter;
import com.example.comicapp.data.adapter.StoryAdapter;
import com.example.comicapp.data.model.Emotion;
import com.example.comicapp.data.model.Story;
import com.example.comicapp.network.RetrofitClient;
import com.example.comicapp.ui.Fragment.EmotionBottomSheet;
import com.example.comicapp.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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

//        setupStoryLists();
//        loadEmotionsAndSetupTabs();
//        loadStoriesFromApi();
        setupBottomNavigation(R.id.nav_home);

        setupSearchInHome();

        // Trong onCreate()
        EmotionBottomSheet sheet = new EmotionBottomSheet(emotionId -> {
            selectEmotionTabById(emotionId);
        });

        sheet.show(getSupportFragmentManager(), "EmotionSheet");

    }
    private void selectEmotionTabById(Long emotionId) {
        if (emotionId == null || allEmotions.isEmpty()) {
            viewPagerEmotion.setCurrentItem(0, true);
            return;
        }

        for (int i = 0; i < allEmotions.size(); i++) {
            if (allEmotions.get(i).getId().equals(emotionId)) {
                viewPagerEmotion.setCurrentItem(i, true);
                return;
            }
        }

        // Không tìm thấy → về tab đầu
        viewPagerEmotion.setCurrentItem(0, true);
    }


    // Trong HomeActivity.java
    private void loadStoriesFromApi() {
        String token = "Bearer " + SessionManager.getToken(this);

        StoryApi storyApi = RetrofitClient.getInstance().create(StoryApi.class);
        storyApi.getAllStories(token).enqueue(new Callback<List<Story>>() {
            @Override
            public void onResponse(Call<List<Story>> call, Response<List<Story>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Story> allStories = response.body();

                    // Hot Stories: hiện tại dùng tất cả (sau này có API riêng thì thay)
                    setupRecyclerView(rvHot, allStories);

                    // New Stories: sắp xếp theo createdAt giảm dần (mới nhất trước)
                    List<Story> newStories = new ArrayList<>(allStories);
                    newStories.sort((s1, s2) -> {
                        if (s2.getCreatedAt() == null) return -1;
                        if (s1.getCreatedAt() == null) return 1;
                        return s2.getCreatedAt().compareTo(s1.getCreatedAt());
                    });

                    // Giới hạn 10 truyện mới nhất (tùy chỉnh)
                    List<Story> latestStories = newStories.size() > 10 ?
                            newStories.subList(0, 10) : newStories;

                    setupRecyclerView(rvNew, latestStories);

                } else {
                    Toast.makeText(HomeActivity.this, "Không tải được danh sách truyện", Toast.LENGTH_SHORT).show();
                    fallbackToDummy();
                }
            }

            @Override
            public void onFailure(Call<List<Story>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                fallbackToDummy();
            }
        });
    }

    private void setupRecyclerView(RecyclerView recyclerView, List<Story> stories) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        StoryAdapter adapter = new StoryAdapter(stories);
        adapter.setOnStoryClickListener(story -> openComicDetail(story));
        recyclerView.setAdapter(adapter);
    }
    private void setupSearchInHome() {
        edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String query = edtSearch.getText().toString().trim();
                if (!query.isEmpty()) {
                    performSearch(query);
                } else {
                    Toast.makeText(this, "Vui lòng nhập từ khóa tìm kiếm", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            return false;
        });
    }

    private void performSearch(String query) {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("query", query);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        // Ẩn bàn phím sau khi tìm
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(edtSearch.getWindowToken(), 0);
        }
    }
    private void fallbackToDummy() {
        // Giữ lại tạm để test khi không có mạng
        List<Story> dummy = getDummyStories();
        setupRecyclerView(rvHot, dummy);
        setupRecyclerView(rvNew, dummy);
    }

    public void openComicDetail(Story story) {
        Intent intent = new Intent(this, ComicDetailActivity.class);
        intent.putExtra("story", (Parcelable) story);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    // Trong HomeActivity.java
    private List<Emotion> allEmotions = new ArrayList<>();

    private void loadEmotionsAndSetupTabs() {
        String token = "Bearer " + SessionManager.getToken(this);

        EmotionApi api = RetrofitClient.getInstance().create(EmotionApi.class);
        api.getAllEmotions(token).enqueue(new Callback<List<Emotion>>() {
            @Override
            public void onResponse(Call<List<Emotion>> call, Response<List<Emotion>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allEmotions = response.body();

                    EmotionPagerAdapter pagerAdapter = new EmotionPagerAdapter(HomeActivity.this);
                    pagerAdapter.setEmotions(allEmotions);
                    viewPagerEmotion.setAdapter(pagerAdapter);

                    new TabLayoutMediator(tabEmotion, viewPagerEmotion, (tab, position) ->
                            tab.setText(pagerAdapter.getEmotionTitle(position))
                    ).attach();

                } else {
                    Toast.makeText(HomeActivity.this, "Không tải được danh sách cảm xúc", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Emotion>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Đánh dấu lại tab Home
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(R.id.nav_home);
        }

        // GỌI LẠI API → lấy dữ liệu mới nhất
        loadStoriesFromApi();
        loadEmotionsAndSetupTabs();
    }


    @Override
    protected int getCurrentNavItemId() {
        return R.id.nav_home;
    }

    private List<Story> getDummyStories() {
        List<Story> list = new ArrayList<>();
//        list.add(new Story("Thám tử lừng danh Conan", "Aoyama Gosho", R.drawable.conan));
//        list.add(new Story("One Piece", "Eiichiro Oda", R.drawable.onepiece));
//        list.add(new Story("Naruto", "Masashi Kishimoto", R.drawable.naruto));
//        list.add(new Story("Attack on Titan", "Hajime Isayama", R.drawable.aot));
//        list.add(new Story("Dragon Ball", "Akira Toriyama", R.drawable.dragonball));
//        list.add(new Story("Demon Slayer", "Koyoharu Gotouge", R.drawable.kimetsu));
        return list;
    }
}