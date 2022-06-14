package com.example.todo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Placeholder;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.databinding.ActivityTaskListBinding;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;


import com.example.todo.databinding.ActivityTaskListBinding;
import com.example.todo.showTaskDetail.TaskDetailFragment;
import com.example.todo.showTaskList.TaskListFragment;


public class  TaskListActivity extends AppCompatActivity
        implements TaskListAdapter.TaskSelectionListener, TaskListFragment.TaskListFragmentCallbacks {


    private TaskListFragment tlf;
    private TaskDetailFragment tdf;
    private boolean tabletMode = false;

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
/*
        FragmentManager fm = getSupportFragmentManager();
        tlf = (TaskListFragment) fm.findFragmentById(R.id.taskListFragment);

        // Running on a tablet: only if taskDetailContainer view is included in layout
        if (binding.TaskDetailContainer != null) {
            tabletMode = true;

            // Add instance of TaskDetailFragment to layout, if not already present.
            tdf = (TaskDetailFragment) fm.findFragmentById(R.id.taskDetailContainer);
            if (tdf == null) {
                FragmentTransaction t = fm.beginTransaction();
                tdf = TaskDetailFragment.newInstance();
                t.add(R.id.taskDetailContainer, tdf);
                t.commit();
            }
        }
*/
    }

    public void addNewTask() {
        //Snackbar.make(findViewById(android.R.id.content).getRootView(), "New Task has been added! ", Snackbar.LENGTH_SHORT).show();
        Intent intent = new Intent(this, TaskDetailActivity.class);
        intent.putExtra("EXTRA_TASK_ID", -1);
        startActivity(intent);
    }


    @Override
    public void onTaskSelected(Task task) {
        //Für impliziten Intent: Siehe Contactpicker
        Intent intent = new Intent(this, TaskDetailActivity.class);
        Bundle extras = new Bundle();
        extras.putInt("EXTRA_TASK_ID", task.getId());
        extras.putSerializable("taskRepo", repository);
        intent.putExtras(extras);
        startActivity(intent);

    }
}