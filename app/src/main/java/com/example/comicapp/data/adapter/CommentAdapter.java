// data/adapter/CommentAdapter.java
package com.example.comicapp.data.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicapp.R;
import com.example.comicapp.data.model.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    // Adapter tự quản lý list → KHÔNG nhận reference bên ngoài
    private final List<Comment> comments = new ArrayList<>();

    public CommentAdapter() {
    }

    public void setComments(List<Comment> newComments) {
        comments.clear();
        if (newComments != null) {
            comments.addAll(newComments);
        }
        notifyDataSetChanged();
    }

    public void addComment(Comment comment) {
        if (comment == null) return;
        comments.add(0, comment);
        notifyItemInserted(0);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("CommentAdapter", "Binding item at position: " + position);
        Comment comment = comments.get(position);
        holder.tvUsername.setText(comment.getUsername());
        holder.tvContent.setText(comment.getContent());
        holder.tvTime.setText(formatDate(comment.getCreatedAt()));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername, tvContent, tvTime;

        ViewHolder(View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
    private String formatDate(String isoDate) {
        if (isoDate == null) return "Vừa xong";
        try {
            String[] parts = isoDate.split("T");
            String date = parts[0];
            String[] dateParts = date.split("-");
            return dateParts[2] + "/" + dateParts[1] + "/" + dateParts[0];
        } catch (Exception e) {
            return "Vừa xong";
        }
    }
}
