package com.example.comicapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
        holder.btnComplete.setEnabled(!task.isCompleted());
        holder.btnComplete.setOnClickListener(v -> listener.onTaskCompleted(task));
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
