package com.example.comicapp.ui.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.comicapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class EmotionBottomSheet extends BottomSheetDialogFragment {

    public EmotionBottomSheet() {}
    public interface OnEmotionSelectedListener {
        void onEmotionSelected(String emoji);
    }

    private OnEmotionSelectedListener listener;

    public EmotionBottomSheet(OnEmotionSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.emotion_frame, container, false);

        LinearLayout emotionLayout = view.findViewById(R.id.layoutEmotions);

        for (int i = 0; i < emotionLayout.getChildCount(); i++) {
            View child = emotionLayout.getChildAt(i);
            child.setOnClickListener(v -> {
                TextView tv = (TextView) v;
                String emoji = tv.getText().toString();

                if (listener != null) listener.onEmotionSelected(emoji);

                dismiss();
            });

        }

        return view;
    }
}
