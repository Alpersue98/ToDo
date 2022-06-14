package com.example.todo.showTaskList;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.Task;
import com.example.todo.databinding.TaskListItemBinding;

import java.util.ArrayList;


public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> {

    // Listener interface for callbacks from adapter to owner activity
    public interface TaskSelectionListener {
        void onTaskSelected(Task task);
    }

    private TaskSelectionListener listener;
    private ArrayList<Task> tasks;

    public TaskListAdapter(TaskSelectionListener listener) {
        this.listener = listener;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TaskListItemBinding binding = TaskListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        TaskViewHolder viewHolder = new TaskViewHolder(binding);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.taskTextView.setText(task.getShortName());

        holder.itemView.setOnClickListener(v -> {
            listener.onTaskSelected(task);
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }


    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        public TextView taskTextView;

        public TaskViewHolder(@NonNull TaskListItemBinding binding) {
            super(binding.getRoot());
            taskTextView = binding.taskTextView;
        }
    }
}
