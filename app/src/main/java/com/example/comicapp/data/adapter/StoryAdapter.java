// data/adapter/StoryAdapter.java
package com.example.comicapp.data.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.comicapp.R;
import com.example.comicapp.data.model.Story;

import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder> {

    private List<Story> stories;
    private OnStoryClickListener listener;

    public interface OnStoryClickListener {
        void onStoryClick(Story story);
    }

    public StoryAdapter(List<Story> stories) {
        this.stories = stories;
    }

    public void setOnStoryClickListener(OnStoryClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_story, parent, false);
        return new StoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        Story story = stories.get(position);

        holder.txtTitle.setText(story.getTitle());
        holder.txtAuthor.setText("Tác giả: " + story.getAuthor());
        String imageUrl = story.getCoverImage();

        if (imageUrl != null && imageUrl.contains("http://localhost")) {
            imageUrl = imageUrl.replace("http://localhost", "http://10.0.2.2");
        }

        // Load ảnh từ URL bằng Glide
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
//                .placeholder(R.drawable.placeholder_image)  // tạo 1 drawable placeholder
//                .error(R.drawable.error_image)              // ảnh lỗi nếu load fail
                .into(holder.imgStory);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onStoryClick(story);
            }
        });

    }

    @Override
    public int getItemCount() {
        return stories != null ? stories.size() : 0;
    }

    static class StoryViewHolder extends RecyclerView.ViewHolder {
        ImageView imgStory;
        TextView txtTitle, txtAuthor;

        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            imgStory = itemView.findViewById(R.id.imgStory);
            txtTitle = itemView.findViewById(R.id.txtStoryTitle);
            txtAuthor = itemView.findViewById(R.id.txtStoryAuthor);
        }
    }

}