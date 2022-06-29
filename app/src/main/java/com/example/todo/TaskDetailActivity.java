package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.example.todo.databinding.ActivityTaskDetailBinding;
import com.example.todo.model.Task;
import com.example.todo.model.TaskRepositoryInMemoryImpl;
import com.example.todo.showTaskDetail.TaskDetailFragment;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskDetailActivity extends AppCompatActivity {

    private TaskDetailFragment tdf;

    Task currentTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityTaskDetailBinding binding = ActivityTaskDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FragmentManager fm = getSupportFragmentManager();

        //Create taskdetailfragment if Activity hasn't been accessed before
        if (savedInstanceState == null) {
            FragmentTransaction t = fm.beginTransaction();
            tdf = TaskDetailFragment.newInstance();
            t.add(R.id.taskDetailContainer, tdf);
            t.commit();
        } else {
            //find taskdetailfragment if one has already been instantiated
            tdf = (TaskDetailFragment) fm.findFragmentById(R.id.taskDetailContainer);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        //Get taskname and addtaskmode boolean from intent extras
        Bundle extras = intent.getExtras();
        int extraTaskID = extras.getInt("EXTRA_TASK_ID");
        boolean addTaskModeExtra = extras.getBoolean("ADD_TASK_MODE");


        if (addTaskModeExtra){
            //create empty task to be added
            currentTask = new Task("");
            tdf.addTaskMode = true;
        }
        else {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());

            executor.execute(() -> {

                TaskRepositoryInMemoryImpl taskRepo = TaskRepositoryInMemoryImpl.getInstance();
                List<Task> taskList = taskRepo.loadTasks();

                handler.post(() -> {
                    for (int i = 0; i < taskList.size(); i++){
                        Task tempTask = taskList.get(i);
                        if (Objects.equals(extraTaskID, tempTask.getId())){
                            tdf.showTask(tempTask);
                        }
                    }
                });
            });


        }

    }

}