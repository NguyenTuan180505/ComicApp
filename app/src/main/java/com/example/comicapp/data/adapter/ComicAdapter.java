package com.example.comicapp.data.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.CheckBox;
import android.widget.TextView;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicapp.data.model.Comic;
import com.example.comicapp.R;
import com.example.comicapp.ui.ActivityAdmin.ComicDetailAdActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ComicViewHolder> {

    private Context context;
    private ArrayList<Comic> comicList;
    private OnItemClickListener listener;
    private boolean selectionMode = false;
    private final Set<Integer> selectedPositions = new HashSet<>();

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
        String thumb = comic.getThumbnail();
        if (thumb != null && !thumb.isEmpty()) {
            if (thumb.startsWith("content://") || thumb.startsWith("file://") || thumb.startsWith("/")) {
                holder.ivThumbnail.setImageURI(Uri.parse(thumb));
            } else {
                int imageId = context.getResources().getIdentifier(thumb, "drawable", context.getPackageName());
                if (imageId != 0) {
                    holder.ivThumbnail.setImageResource(imageId);
                } else {
                    try {
                        holder.ivThumbnail.setImageURI(Uri.parse(thumb));
                    } catch (Exception ignored) {}
                }
            }
        } else {
            holder.ivThumbnail.setImageDrawable(null);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ComicDetailAdActivity.class);
            intent.putExtra("title", comic.getTitle());
            intent.putExtra("chapters", comic.getChapters());
            intent.putExtra("thumbnail", comic.getThumbnail());
            context.startActivity(intent);
        });

        holder.cbSelect.setVisibility(selectionMode ? View.VISIBLE : View.GONE);
        holder.cbSelect.setOnCheckedChangeListener(null);
        holder.cbSelect.setChecked(selectedPositions.contains(position));
        holder.cbSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) selectedPositions.add(position); else selectedPositions.remove(position);
        });
    }

    @Override
    public int getItemCount() {
        return comicList.size();
    }

    public void setSelectionMode(boolean enabled) {
        selectionMode = enabled;
        if (!enabled) selectedPositions.clear();
        notifyDataSetChanged();
    }

    public ArrayList<Integer> getSelectedPositions() {
        return new ArrayList<>(selectedPositions);
    }

    public void removeAt(int position) {
        comicList.remove(position);
        notifyItemRemoved(position);
    }

    public static class ComicViewHolder extends RecyclerView.ViewHolder {
        ImageView ivThumbnail;
        TextView tvTitle, tvChapters;
        CheckBox cbSelect;


        public ComicViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumbnail = itemView.findViewById(R.id.ivThumbnail);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvChapters = itemView.findViewById(R.id.tvChapters);
            cbSelect = itemView.findViewById(R.id.cbSelect);

        }
    }
}
