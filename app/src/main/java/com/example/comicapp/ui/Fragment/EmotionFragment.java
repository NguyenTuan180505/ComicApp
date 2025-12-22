// ui/Fragment/EmotionFragment.java
package com.example.comicapp.ui.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicapp.R;
import com.example.comicapp.api.EmotionApi;
import com.example.comicapp.network.RetrofitClient;
import com.example.comicapp.data.adapter.StoryAdapter;
import com.example.comicapp.data.model.Story;
import com.example.comicapp.ui.ActivityUser.HomeActivity;
import com.example.comicapp.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmotionFragment extends Fragment {

    private static final String ARG_EMOTION_ID = "emotion_id";
    private Long emotionId;

    private RecyclerView recyclerView;
    private StoryAdapter adapter;
    private List<Story> storyList = new ArrayList<>();

    public static EmotionFragment newInstance(Long emotionId) {
        EmotionFragment fragment = new EmotionFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_EMOTION_ID, emotionId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            emotionId = getArguments().getLong(ARG_EMOTION_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_emotion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.rvEmotionStories);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        adapter = new StoryAdapter(storyList);
        adapter.setOnStoryClickListener(story -> {
            if (getActivity() instanceof HomeActivity) {
                ((HomeActivity) getActivity()).openComicDetail(story);
            }
        });
        recyclerView.setAdapter(adapter);

        loadStories();
    }

    private void loadStories() {
        if (emotionId == null) {
            Toast.makeText(getContext(), "Không xác định được cảm xúc", Toast.LENGTH_SHORT).show();
            return;
        }

        String token = "Bearer " + SessionManager.getToken(getContext());

        EmotionApi api = RetrofitClient.getInstance().create(EmotionApi.class);
        api.getStoriesByEmotionId(token, emotionId).enqueue(new Callback<List<Story>>() {
            @Override
            public void onResponse(Call<List<Story>> call, Response<List<Story>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    storyList.clear();
                    storyList.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    // Nếu không có truyện nào
                    if (storyList.isEmpty()) {
                        Toast.makeText(getContext(), "Chưa có truyện cho cảm xúc này", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(), "Không tải được truyện", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Story>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}