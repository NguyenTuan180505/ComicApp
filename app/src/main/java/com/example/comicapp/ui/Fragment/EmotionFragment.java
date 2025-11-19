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

import java.util.ArrayList;
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

        List<Story> stories = getDummyStoriesByEmotion(emotion);

        StoryAdapter adapter = new StoryAdapter(stories);

        // THÊM DÒNG NÀY – QUAN TRỌNG NHẤT!!!
        adapter.setOnStoryClickListener(story -> {
            if (getActivity() instanceof HomeActivity) {
                ((HomeActivity) getActivity()).openComicDetail(story);
            }
        });

        recyclerView.setAdapter(adapter);
    }

    private List<Story> getDummyStoriesByEmotion(String emotion) {
        List<Story> stories = new ArrayList<>();

        // Dựa vào emotion để trả về danh sách truyện phù hợp
        switch (emotion.toLowerCase()) {
            case "vui":
            case "hạnh phúc":
            case "happy":
                stories.add(new Story("One Piece", "Eiichiro Oda", R.drawable.onepiece));
                stories.add(new Story("Doraemon", "Fujiko F. Fujio", R.drawable.doraemon));
                stories.add(new Story("Gintama", "Hideaki Sorachi", R.drawable.gintama));
                stories.add(new Story("Kaguya-sama", "Aka Akasaka", R.drawable.kaguya));
                break;

            case "buồn":
            case "sad":
                stories.add(new Story("Your Lie in April", "Naoshi Arakawa", R.drawable.yourlie));
                stories.add(new Story("Clannad", "Key", R.drawable.clannad));
                stories.add(new Story("I Want to Eat Your Pancreas", "Yoru Sumino", R.drawable.doraemon));
                stories.add(new Story("Anohana", "Choheiwa Busters", R.drawable.kaguya));
                break;

            case "hài hước":
            case "hài":
            case "funny":
                stories.add(new Story("Grand Blue", "Kenji Inoue", R.drawable.gintama));
                stories.add(new Story("Saiki Kusuo", "Shuichi Aso", R.drawable.onepiece));
                stories.add(new Story("Nichijou", "Keiichi Arawi", R.drawable.yourlie));
                stories.add(new Story("Asobi Asobase", "Rin Suzukawa", R.drawable.clannad));
                break;

            case "hành động":
            case "action":
                stories.add(new Story("Attack on Titan", "Hajime Isayama", R.drawable.doraemon));
                stories.add(new Story("Demon Slayer", "Koyoharu Gotouge", R.drawable.clannad));
                stories.add(new Story("Jujutsu Kaisen", "Gege Akutami", R.drawable.yourlie));
                stories.add(new Story("Tokyo Revengers", "Ken Wakui", R.drawable.naruto));
                break;

            case "lãng mạn":
            case "romance":
                stories.add(new Story("Horimiya", "HERO", R.drawable.naruto));
                stories.add(new Story("Fruits Basket", "Natsuki Takaya", R.drawable.doraemon));
                stories.add(new Story("My Teen Romantic Comedy", "Wataru Watari", R.drawable.clannad));
                break;

            case "kinh dị":
            case "horror":
                stories.add(new Story("Tokyo Ghoul", "Sui Ishida", R.drawable.yourlie));
                stories.add(new Story("Another", "Yukito Ayatsuji", R.drawable.onepiece));
                stories.add(new Story("Parasyte", "Hitoshi Iwaaki", R.drawable.doraemon));
                break;

            default:
                // Nếu không khớp, trả về vài truyện phổ biến
                stories.add(new Story("Naruto", "Masashi Kishimoto", R.drawable.naruto));
                stories.add(new Story("Dragon Ball", "Akira Toriyama", R.drawable.doraemon));
                break;
        }

        return stories;
    }
}
