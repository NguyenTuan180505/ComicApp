package com.example.comicapp.ui.ActivityUser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicapp.R;
import com.example.comicapp.api.StoryApi;
import com.example.comicapp.data.adapter.StoryAdapter;
import com.example.comicapp.data.model.Story;
import com.example.comicapp.network.RetrofitClient;
import com.example.comicapp.utils.SessionManager;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends BaseNavigationActivity {

    private RecyclerView rvSearchResults;
    private TextView tvNoResult, tvResultTitle;
    private EditText edtSearch;
    private String query;

    private StoryApi storyApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initViews();
        initApi();
        handleIntent();

        // Cho phép tìm kiếm lại ngay trên thanh search
        setupSearchListener();

        // Highlight tab Home (hoặc không, tùy ý)
        setupBottomNavigation(R.id.nav_home);

        // Tải kết quả tìm kiếm ban đầu
        performSearch(query);
    }

    private void initViews() {
        rvSearchResults = findViewById(R.id.rvSearchResults);
        tvNoResult = findViewById(R.id.tvNoResult);
        tvResultTitle = findViewById(R.id.tvResultTitle);
        edtSearch = findViewById(R.id.edtSearch);
    }

    private void initApi() {
        storyApi = RetrofitClient.getInstance().create(StoryApi.class);
    }

    private void handleIntent() {
        query = getIntent().getStringExtra("query");

        if (query == null || query.trim().isEmpty()) {
            Toast.makeText(this, "Không có từ khóa tìm kiếm", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        query = query.trim();
        edtSearch.setText(query);
        edtSearch.setSelection(query.length()); // Con trỏ ở cuối để dễ chỉnh sửa
        tvResultTitle.setText("Kết quả cho: \"" + query + "\"");
    }

    private void setupSearchListener() {
        edtSearch.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                String newQuery = textView.getText().toString().trim();
                if (newQuery.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập từ khóa tìm kiếm", Toast.LENGTH_SHORT).show();
                    return true;
                }

                if (!newQuery.equals(query)) {
                    query = newQuery;
                    tvResultTitle.setText("Kết quả cho: \"" + query + "\"");
                    performSearch(query);
                }

                // Ẩn bàn phím sau khi tìm
                hideKeyboard();
                return true;
            }
            return false;
        });
    }

    private void performSearch(String title) {
        String token = "Bearer " + SessionManager.getToken(this);

        // Hiển thị loading nếu muốn (có thể thêm ProgressBar sau)
        showLoadingState();

        storyApi.searchStories(token, title).enqueue(new Callback<List<Story>>() {
            @Override
            public void onResponse(Call<List<Story>> call, Response<List<Story>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Story> results = response.body();
                    if (results.isEmpty()) {
                        showEmptyState();
                    } else {
                        showResults(results);
                    }
                } else {
                    Toast.makeText(SearchActivity.this, "Không thể tải kết quả", Toast.LENGTH_SHORT).show();
                    showEmptyState();
                }
            }

            @Override
            public void onFailure(Call<List<Story>> call, Throwable t) {
                Toast.makeText(SearchActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                showEmptyState();
            }
        });
    }

    private void showLoadingState() {
        // Có thể thêm ProgressBar ở layout và hiện ở đây
        tvNoResult.setVisibility(View.GONE);
        rvSearchResults.setVisibility(View.GONE);
    }

    private void showEmptyState() {
        tvNoResult.setVisibility(View.VISIBLE);
        tvNoResult.setText("Không tìm thấy truyện nào phù hợp với \"" + query + "\"");
        rvSearchResults.setVisibility(View.GONE);
    }

    private void showResults(List<Story> stories) {
        tvNoResult.setVisibility(View.GONE);
        rvSearchResults.setVisibility(View.VISIBLE);

        rvSearchResults.setLayoutManager(new GridLayoutManager(this, 3));
        StoryAdapter adapter = new StoryAdapter(stories);
        adapter.setOnStoryClickListener(this::openComicDetail);
        rvSearchResults.setAdapter(adapter);
    }

    private void openComicDetail(Story story) {
        Intent intent = new Intent(this, ComicDetailActivity.class);
        intent.putExtra("story", story);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null && getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    protected int getCurrentNavItemId() {
        return R.id.nav_home; // Giữ highlight Home khi ở màn tìm kiếm
    }
}