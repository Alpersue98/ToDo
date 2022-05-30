package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;


import com.example.todo.databinding.ActivityTaskListBinding;
import com.google.android.material.snackbar.Snackbar;


public class  TaskListActivity extends AppCompatActivity implements TaskListAdapter.TaskSelectionListener {

    private ActivityTaskListBinding binding;
    private TaskListAdapter adapter;

    private ArrayList<Task> tasks;
    TaskRepositoryInMemoryImpl repository = new TaskRepositoryInMemoryImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTaskListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Define reaction to button
        findViewById(R.id.addTaskButton).setOnClickListener(v -> addNewTask());
        tasks = (ArrayList<Task>) repository.loadTasks();



        // RecyclerView
        RecyclerView recyclerView = binding.taskRecycler;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TaskListAdapter(this);
        adapter.setTasks(tasks);
        recyclerView.setAdapter(adapter);
    }

    public void addNewTask() {

        //String taskName = binding.editTextTaskName.getText().toString();
        Snackbar.make(findViewById(android.R.id.content).getRootView(), "New Task has been added! ", Snackbar.LENGTH_SHORT).show();
        //tasks.add(new Task(taskName));
        //adapter.notifyItemInserted(tasks.size()-1);
        //binding.editTextTaskName.setText("");
    }


    @Override
    public void onTaskSelected(Task task) {
        Intent intent = new Intent(this, TaskDetailActivity.class);
        intent.putExtra(TaskDetailActivity.EXTRA_TASK_ID, task.getId());
        startActivity(intent);

    }
}