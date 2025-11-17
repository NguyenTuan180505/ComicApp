package com.example.comicapp.ui.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicapp.R;
import com.example.comicapp.data.adapter.StoryAdapter;
import com.example.comicapp.data.model.Story;
import com.example.comicapp.ui.Activity.HomeActivity;

import java.util.List;

public class EmotionFragment extends Fragment {

    private static final String ARG_EMOTION = "emotion";
    private String emotion;

    public static EmotionFragment newInstance(String emotion) {
        EmotionFragment fragment = new EmotionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_EMOTION, emotion);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            emotion = getArguments().getString(ARG_EMOTION);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_emotion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.rvEmotionStories);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));

        // TODO: Gọi API theo cảm xúc
        List<Story> stories = getDummyStoriesByEmotion(emotion);
        recyclerView.setAdapter(new StoryAdapter(stories));
    }

    private List<Story> getDummyStoriesByEmotion(String emotion) {
        // Tạm thời trả về dữ liệu giả theo cảm xúc
        List<Story> list = ((HomeActivity) requireActivity()).getDummyStories();
        // Có thể lọc theo cảm xúc ở đây nếu có dữ liệu
        return list;
    }
}
