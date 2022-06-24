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

    public static final String EXTRA_TASK_NAME = "EXTRA_TASK_NAME";


    TaskRepositoryInMemoryImpl taskRepo = TaskRepositoryInMemoryImpl.getInstance();

    //Create list of tasks
    //List<Task> taskList;

    private TaskDetailFragment tdf;


    Task currentTask;
    boolean addTaskMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityTaskDetailBinding binding = ActivityTaskDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FragmentManager fm = getSupportFragmentManager();
        if (savedInstanceState == null) {
            FragmentTransaction t = fm.beginTransaction();
            tdf = TaskDetailFragment.newInstance();
            t.add(R.id.taskDetailContainer, tdf);
            t.commit();
        } else {
            tdf = (TaskDetailFragment) fm.findFragmentById(R.id.taskDetailContainer);
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        addTaskMode = false;

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String extraTaskName = extras.getString("EXTRA_TASK_NAME");
        boolean addTaskModeExtra = extras.getBoolean("ADD_TASK_MODE");
        addTaskMode = addTaskModeExtra;

        //TextView lastTaskView = (TextView) findViewById(R.id.LastTask);
        //lastTaskView.append("ExtraName: " + extraTaskName + "\n");


        if (addTaskMode == true){
            currentTask = new Task("");
        }
        else {

            // Asynchronous execution of db statement
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());

            executor.execute(() -> {
                // Background thread work here:
//            repository.createTestData();

                List<Task> taskList = taskRepo.loadTasks();

                handler.post(() -> {
                    for (int i = 0; i < taskList.size(); i++){
                        Task tempTask = taskList.get(i);
                        //lastTaskView.append("tempTask: " + tempTask.getShortName() + "\n");
                        if (Objects.equals(extraTaskName, tempTask.getShortName())){
                            //lastTaskView.append("tempTaskID: " + tempTask.getId() + "\n");
                            currentTask = tempTask;

                        }
                    }
                });
            });
            //tdf.showTask(currentTask);

        }
        //TODO: showTask sollte bei addTaskMode == false ausgeführt werden
        //if (addTaskMode == false){
        if (addTaskMode == true){
            tdf.showTask(currentTask);
        }

    }

}