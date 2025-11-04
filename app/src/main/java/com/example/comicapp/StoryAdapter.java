package com.example.comicapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.StoryViewHolder> {

    private List<Story> stories;

    public StoryAdapter(List<Story> stories) {
        this.stories = stories;
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

        // Gán dữ liệu vào các view
        holder.txtTitle.setText(story.getTitle());
        holder.txtAuthor.setText("Tác giả: " + story.getAuthor());
        holder.imgStory.setImageResource(story.getImageResId());
    }

    @Override
    public int getItemCount() {
        return stories.size();
    }

    // ViewHolder class
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
