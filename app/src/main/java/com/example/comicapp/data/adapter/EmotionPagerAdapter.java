package com.example.comicapp.data.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.comicapp.ui.Fragment.EmotionFragment;

public class EmotionPagerAdapter extends FragmentStateAdapter {

    private final String[] emotions = {"Vui", "Buồn", "Tức Giận", "Bình Thường"};

    public EmotionPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return EmotionFragment.newInstance(emotions[position]);
    }

    @Override
    public int getItemCount() {
        return emotions.length;
    }

    public String getEmotionTitle(int position) {
        return emotions[position];
    }
}
