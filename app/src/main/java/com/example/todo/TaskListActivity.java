package com.example.todo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.databinding.ActivityTaskListBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


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

    TaskRepositoryInMemoryImpl repository = new TaskRepositoryInMemoryImpl();



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

        repository = TaskRepositoryInMemoryImpl.getInstance();

        // Asynchronous execution of db statement
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            // Background thread work here:
//            repository.createTestData();
            tasks = repository.loadTasks();

            handler.post(() -> {
                // UI thread work here
                tlf.setTasks(tasks);
            });
        });

    }


    @Override
    public void addNewTask() {
        Intent intent = new Intent(this, TaskDetailActivity.class);
        Bundle extras = new Bundle();
        extras.putString("EXTRA_TASK_NAME", "");
        extras.putSerializable("taskRepo", repository);
        intent.putExtras(extras);
        startActivity(intent);
    }

    @Override
    public void onTaskSelected(Task task) {
        //Für impliziten Intent: Siehe Contactpicker
        Intent intent = new Intent(this, TaskDetailActivity.class);
        Bundle extras = new Bundle();
        extras.putString("EXTRA_TASK_NAME", task.getShortName());
        extras.putSerializable("taskRepo", repository);
        intent.putExtras(extras);
        startActivity(intent);

    }
}