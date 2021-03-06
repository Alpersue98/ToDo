package com.example.todo;

import static java.security.AccessController.getContext;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.todo.databinding.TaskListItemBinding;
import com.example.todo.model.Task;

import java.util.ArrayList;


public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> {

    // Listener interface for callbacks from adapter to owner activity
    public interface TaskSelectionListener {
        void onTaskSelected(Task task);

        void addNewTask();

        void onCheckBoxClick(boolean isChecked,Task task);
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
    //inflate layout and create viewHolder
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TaskListItemBinding binding = TaskListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        TaskViewHolder viewHolder = new TaskViewHolder(binding);
        return viewHolder;
    }

    @Override
    //Display task info and set listeners
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.taskTextView.setText(task.getShortName());
        holder.taskCheckBox.setChecked(task.isDone());

        holder.itemView.setOnClickListener(v -> {
            listener.onTaskSelected(task);
        });

        holder.taskCheckBox.setOnClickListener( v -> {
            listener.onCheckBoxClick(holder.taskCheckBox.isChecked(), task);
        });
    }

    @Override
    public int getItemCount() {
        try{
            return tasks.size();
        }
        catch (NullPointerException nE){
            nE.printStackTrace();
            return 0;
        }


    }


    public static class TaskViewHolder extends RecyclerView.ViewHolder {

        public TextView taskTextView;
        public CheckBox taskCheckBox;

        public TaskViewHolder(@NonNull TaskListItemBinding binding) {
            super(binding.getRoot());
            taskTextView = binding.taskTextView;
            taskCheckBox = binding.taskCheckBox;
        }
    }
}
