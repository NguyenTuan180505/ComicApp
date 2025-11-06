package com.example.comicapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ComicViewHolder> {

    private Context context;
    private ArrayList<Comic> comicList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onAddChapterClick(int position);
        void onEditChapterClick(int position);
        void onDeleteChapterClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ComicAdapter(Context context, ArrayList<Comic> comicList) {
        this.context = context;
        this.comicList = comicList;
    }

    @NonNull
    @Override
    public ComicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comic, parent, false);
        return new ComicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComicViewHolder holder, int position) {
        Comic comic = comicList.get(position);
        holder.tvTitle.setText("Truyện: " + comic.getTitle());
        holder.tvChapters.setText("Chap: " + comic.getChapters());
        int imageId = context.getResources().getIdentifier(comic.getThumbnail(), "drawable", context.getPackageName());
        holder.ivThumbnail.setImageResource(imageId);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ComicDetailActivity.class);
            intent.putExtra("title", comic.getTitle());
            intent.putExtra("chapters", comic.getChapters());
            intent.putExtra("thumbnail", comic.getThumbnail());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return comicList.size();
    }

    public static class ComicViewHolder extends RecyclerView.ViewHolder {
        ImageView ivThumbnail;
        TextView tvTitle, tvChapters;


        public ComicViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvChapters = itemView.findViewById(R.id.tvChapters);

        }
    }
}