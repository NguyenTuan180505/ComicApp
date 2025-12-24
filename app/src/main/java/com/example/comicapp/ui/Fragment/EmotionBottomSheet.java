package com.example.comicapp.ui.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.comicapp.R;
import com.example.comicapp.api.EmotionApi;
import com.example.comicapp.data.model.Emotion;
import com.example.comicapp.utils.SessionManager;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmotionBottomSheet extends BottomSheetDialogFragment {

    public interface OnEmotionSelectedListener {
        void onEmotionSelected(Long emotionId);  // Thay đổi: truyền ID thay vì emoji
    }

    private OnEmotionSelectedListener listener;
    private Map<String, Long> nameToIdMap = new HashMap<>();  // Map tên → id

    public EmotionBottomSheet() {}

    public EmotionBottomSheet(OnEmotionSelectedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.emotion_frame, container, false);

        // Load danh sách cảm xúc từ API trước
        loadEmotionsAndSetupViews(view);

        return view;
    }

    private void loadEmotionsAndSetupViews(View view) {
        String token = "Bearer " + SessionManager.getToken(getContext());

        EmotionApi api = com.example.comicapp.network.RetrofitClient.getInstance().create(EmotionApi.class);
        api.getAllEmotions(token).enqueue(new Callback<List<Emotion>>() {
            @Override
            public void onResponse(Call<List<Emotion>> call, Response<List<Emotion>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Emotion> emotions = response.body();

                    // Tạo map: name → id
                    nameToIdMap.clear();
                    for (Emotion e : emotions) {
                        nameToIdMap.put(e.getName(), e.getId());
                    }

                    // Gán emoji + click listener cho các TextView trong layout
                    setupEmotionViews(view, emotions);
                } else {
                    Toast.makeText(getContext(), "Không tải được cảm xúc", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<Emotion>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }

    private void setupEmotionViews(View view, List<Emotion> emotions) {
        // Giả sử trong emotion_frame.xml bạn có các TextView với id:
        // tvSad, tvHappy, tvLonely, tvJoyful, ...

        // Ví dụ: bạn có 4 TextView cố định
        setEmotionView(view, R.id.tvSad, "Buồn", emotions);
        setEmotionView(view, R.id.tvHappy, "Vui vẻ", emotions);
        setEmotionView(view, R.id.tvLonely, "Cô đơn", emotions);
        setEmotionView(view, R.id.tvJoyful, "Hạnh phúc", emotions);
        // Thêm các cái khác nếu có
    }

    private void setEmotionView(View parent, int textViewId, String emotionName, List<Emotion> emotions) {
        TextView tv = parent.findViewById(textViewId);
        if (tv == null) return;

        // Tìm emotion theo tên
        for (Emotion e : emotions) {
            if (e.getName().equals(emotionName)) {
                // Dùng hàm map emoji giống trong PagerAdapter
                String emoji = getEmojiForName(e.getName());
                tv.setText(emoji);  // Chỉ hiện emoji

                tv.setOnClickListener(v -> {
                    Long emotionId = nameToIdMap.get(e.getName());
                    if (listener != null && emotionId != null) {
                        listener.onEmotionSelected(emotionId);
                    }
                    dismiss();
                });
                return;
            }
        }

        // Nếu không tìm thấy
        tv.setVisibility(View.GONE);
    }

    private String getEmojiForName(String name) {
        switch (name) {
            case "Buồn": return "😔";
            case "Vui vẻ": return "😀";
            case "Cô đơn": return "😢";
            case "Hạnh phúc": return "🥰";
            default: return "📖";
        }
    }
}