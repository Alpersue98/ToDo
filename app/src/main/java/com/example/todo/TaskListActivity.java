package com.example.todo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.databinding.ActivityTaskListBinding;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;


import com.example.todo.databinding.ActivityTaskListBinding;


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
        //Snackbar.make(findViewById(android.R.id.content).getRootView(), "New Task has been added! ", Snackbar.LENGTH_SHORT).show();
        Intent intent = new Intent(this, TaskDetailActivity.class);
        intent.putExtra(TaskDetailActivity.EXTRA_TASK_NAME, (String) null);
        startActivity(intent);
    }


    @Override
    public void onTaskSelected(Task task) {
        //Für impliziten Intent: Siehe Contactpicker,
        Intent intent = new Intent(this, TaskDetailActivity.class);
        Bundle extras = new Bundle();
        extras.putString(TaskDetailActivity.EXTRA_TASK_NAME, task.getShortName());
        extras.putSerializable("taskRepo", repository);
        intent.putExtras(extras);
        startActivity(intent);

    }
}