// data/adapter/EmotionPagerAdapter.java
package com.example.comicapp.data.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.comicapp.data.model.Emotion;
import com.example.comicapp.ui.Fragment.EmotionFragment;

import java.util.ArrayList;
import java.util.List;

public class EmotionPagerAdapter extends FragmentStateAdapter {

    private final List<Emotion> emotions = new ArrayList<>();

    public EmotionPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public void setEmotions(List<Emotion> emotions) {
        this.emotions.clear();
        this.emotions.addAll(emotions);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Emotion emotion = emotions.get(position);
        // Truyền emotionId thay vì tên để chính xác hơn
        return EmotionFragment.newInstance(emotion.getId());
    }

    @Override
    public int getItemCount() {
        return emotions.size();
    }

    public String getEmotionTitle(int position) {
        if (position >= emotions.size()) return "Cảm xúc";

        Emotion emotion = emotions.get(position);
        String emoji = getEmojiForName(emotion.getName());  // hoặc getEmojiForId(emotion.getId())

        return emoji + " " + emotion.getName();
    }

    public Emotion getEmotion(int position) {
        if (position < emotions.size()) {
            return emotions.get(position);
        }
        return null;
    }
    private String getEmojiForName(String name) {
        switch (name) {
            case "Buồn":
                return "😔";
            case "Vui vẻ":
                return "😀";
            case "Cô đơn":
                return "😢";
            case "Hạnh phúc":
                return "🥰";
            // Thêm các case khác nếu có
            default:
                return "📖"; // emoji mặc định
        }
    }
}