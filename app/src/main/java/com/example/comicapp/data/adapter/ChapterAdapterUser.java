// ChapterAdapterUser.java
package com.example.comicapp.data.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicapp.R;
import com.example.comicapp.data.model.Chapter;
import com.example.comicapp.data.model.Story;
import com.example.comicapp.ui.ActivityUser.ReadComicActivity;

import java.util.List;

public class ChapterAdapterUser extends RecyclerView.Adapter<ChapterAdapterUser.ViewHolder> {

    private final List<Chapter> chapterList;
    private final Context context;
    private final Story story;

    public ChapterAdapterUser(Context context, List<Chapter> chapters, Story story) {
        this.context = context;
        this.chapterList = chapters;
        this.story = story;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context)
                .inflate(R.layout.item_chapter, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chapter chapter = chapterList.get(position);

        // ===== 1️⃣ Tạo title + Free / VIP =====
        String title = null;

        if (chapter.getTitle() != null && !chapter.getTitle().isEmpty()) {
            title = chapter.getTitle();
        }

        // Hiển thị Free / VIP
        title += chapter.isLocked() ? " (Chưa mở)" : " (Đã mở)";

        holder.tvChapterName.setText(title);

        // ===== 2️⃣ Làm mờ chương bị khóa =====
        holder.itemView.setAlpha(chapter.isLocked() ? 0.5f : 1f);

        // ===== 3️⃣ Ngày tạo =====
        holder.tvUploadDate.setText(chapter.getCreatedAt());

        // Ẩn pageCount nếu không dùng
        holder.tvPageCount.setVisibility(View.GONE);

        // ===== 4️⃣ Click vào item =====
        holder.itemView.setOnClickListener(v -> {

            // 👉 Click chương khóa → Toast
            if (chapter.isLocked()) {
                Toast.makeText(context,
                        "Chương này cần mở khóa",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // 👉 Chương mở → đọc truyện
            Intent intent = new Intent(context, ReadComicActivity.class);
            intent.putExtra("storyId", story.getId());
            intent.putExtra("chapterNumber", chapter.getChapterNumber());
            context.startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return chapterList != null ? chapterList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvChapterName, tvPageCount, tvUploadDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChapterName = itemView.findViewById(R.id.tvChapterName);
            tvPageCount = itemView.findViewById(R.id.tvPageCount);
            tvUploadDate = itemView.findViewById(R.id.tvUploadDate);
        }
    }
}
