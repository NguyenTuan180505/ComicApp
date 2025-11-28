package com.example.comicapp.data.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.comicapp.R;
import java.util.List;
public class ChapterAdapter extends RecyclerView.Adapter<ChapterAdapter.ChapterViewHolder> {

    public interface OnChapterClickListener {
        void onChapterClick(int chapterNumber);
        void onChapterDelete(int chapterNumber, int position);
    }

    private final Context context;
    private final List<Integer> chapters;
    private final OnChapterClickListener listener;

    public ChapterAdapter(Context context, List<Integer> chapters, OnChapterClickListener listener) {
        this.context = context;
        this.chapters = chapters;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chapter_ad, parent, false);
        return new ChapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChapterViewHolder holder, int position) {
        int chapter = chapters.get(position);
        holder.tvChapterTitle.setText("Chapter " + chapter);
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onChapterClick(chapter);
        });
        holder.btnDelete.setOnClickListener(v -> {
            if (listener != null) listener.onChapterDelete(chapter, position);
        });
        
    }

    @Override
    public int getItemCount() {
        return chapters.size();
    }

    static class ChapterViewHolder extends RecyclerView.ViewHolder {
        TextView tvChapterTitle;
        ImageView btnDelete;

        ChapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvChapterTitle = itemView.findViewById(R.id.tvChapterTitle);
            btnDelete = itemView.findViewById(R.id.btnDeleteChapter);
        }
    }
}
