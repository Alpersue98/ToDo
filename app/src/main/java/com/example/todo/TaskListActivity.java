package com.example.todo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.todo.databinding.ActivityTaskListBinding;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import com.example.todo.model.Task;
import com.example.todo.model.TaskRepositoryInMemoryImpl;
import com.example.todo.showTaskDetail.TaskDetailFragment;
import com.example.todo.showTaskList.TaskListFragment;


public class  TaskListActivity extends AppCompatActivity
        implements TaskListAdapter.TaskSelectionListener, TaskListFragment.TaskListFragmentCallbacks {

    private ActivityTaskListBinding binding;
    private TaskListFragment tlf;
    private TaskDetailFragment tdf;
    private boolean tabletMode = false;


    private TaskListAdapter adapter;
    private List<Task> tasks;
    private TaskRepositoryInMemoryImpl repository;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTaskListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FragmentManager fm = getSupportFragmentManager();
        tlf = (TaskListFragment) fm.findFragmentById(R.id.taskListFragment);

        // Running on a tablet: only if taskDetailContainer view is included in layout
        if (binding.taskDetailContainer != null) {
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadTaskList();


    }


    @Override
    public void addNewTask() {
        if (tabletMode) {
            tdf.clearTask();
            tdf.addTaskMode = true;
            tdf.tabletMode = true;
        }
        else{
            Intent intent = new Intent(this, TaskDetailActivity.class);
            Bundle extras = new Bundle();
            extras.putInt("EXTRA_TASK_ID", -1);
            extras.putBoolean("ADD_TASK_MODE", true);
            intent.putExtras(extras);
            startActivity(intent);
        }
    }

    @Override
    public void onCheckBoxClick(boolean isChecked, Task task) {

        task.setDone(isChecked);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            repository = TaskRepositoryInMemoryImpl.getInstance();
            repository.updateTask(task);

            handler.post(() -> {
                if(tabletMode){
                    tdf.showTask(task);
                }
            });
        });
    }

    @Override
    public void onTaskSelected(Task task) {
        if (tabletMode) {
            tdf.showTask(task);
            tdf.addTaskMode = false;
            tdf.tabletMode = true;
        } else {
            Intent intent = new Intent(this, TaskDetailActivity.class);
            Bundle extras = new Bundle();
            //Pass task id and addtaskmode to detail activity
            extras.putInt("EXTRA_TASK_ID", task.getId());
            extras.putBoolean("ADDTASKMODE", false);
            intent.putExtras(extras);
            startActivity(intent);
        }
    }


    public void loadTaskList() {
        // Asynchronous execution of db statement
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            repository = TaskRepositoryInMemoryImpl.getInstance();
            tasks = repository.loadTasks();

            handler.post(() -> {
                // UI thread work here
                tlf.setTasks(tasks);
            });
        });

    }


}