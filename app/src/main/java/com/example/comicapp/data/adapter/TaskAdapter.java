package com.example.comicapp.data.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.comicapp.R;
import com.example.comicapp.data.model.Task;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> tasks;
    private OnTaskCompletedListener listener;

    public interface OnTaskCompletedListener {
        void onTaskCompleted(Task task);
    }

    public TaskAdapter(List<Task> tasks, OnTaskCompletedListener listener) {
        this.tasks = tasks;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.txtName.setText(task.getName());
        holder.txtDesc.setText(task.getDesc());
        holder.txtReward.setText("🎁 " + task.getReward() + " điểm");
        holder.txtStatus.setText(task.isCompleted() ? "✅ Hoàn thành" : "⏳ Chưa hoàn thành");
        
        // Clear listener trước để tránh listener cũ
        holder.btnComplete.setOnClickListener(null);
        
        // Cập nhật button dựa trên trạng thái hoàn thành
        if (task.isCompleted()) {
            holder.btnComplete.setText("Đã hoàn thành nhiệm vụ");
            holder.btnComplete.setEnabled(false);
            holder.btnComplete.setAlpha(0.6f); // Làm mờ button khi đã hoàn thành
        } else {
            holder.btnComplete.setText("Đánh dấu hoàn thành");
            holder.btnComplete.setEnabled(true);
            holder.btnComplete.setAlpha(1.0f); // Bình thường khi chưa hoàn thành
            holder.btnComplete.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onTaskCompleted(task);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtDesc, txtReward, txtStatus;
        Button btnComplete;
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtTaskName);
            txtDesc = itemView.findViewById(R.id.txtTaskDesc);
            txtReward = itemView.findViewById(R.id.txtReward);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            btnComplete = itemView.findViewById(R.id.btnComplete);
        }
    }
}
