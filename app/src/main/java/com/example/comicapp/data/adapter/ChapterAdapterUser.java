// ChapterAdapterUser.java
package com.example.comicapp.data.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.comicapp.R;
import com.example.comicapp.data.model.Chapter;
import com.example.comicapp.ui.ActivityUser.ReadComicActivity;
import com.example.comicapp.data.model.Story;
import java.util.List;

public class ChapterAdapterUser extends RecyclerView.Adapter<ChapterAdapterUser.ViewHolder> {

    private List<Chapter> chapterList;
    private Context context;
    private Story story;

    public ChapterAdapterUser(Context context, List<Chapter> chapters, Story story) {
        this.context = context;
        this.chapterList = chapters;
        this.story = story;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_chapter, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chapter chapter = chapterList.get(position);
        holder.tvChapterName.setText("Chương " + chapter.getNumber() + (chapter.getName().isEmpty() ? "" : ": " + chapter.getName()));
        holder.tvPageCount.setText(chapter.getPageCount() + " trang");
        holder.tvUploadDate.setText(chapter.getUploadDate());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReadComicActivity.class);
            intent.putExtra("story", story);
            intent.putExtra("chapterNumber", chapter.getNumber());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return chapterList.size();
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