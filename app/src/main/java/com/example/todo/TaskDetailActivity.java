package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.todo.databinding.ActivityTaskDetailBinding;
import com.example.todo.databinding.ActivityTaskDetailBinding;
import com.example.todo.showTaskDetail.TaskDetailFragment;

import java.text.DateFormat;
import java.util.List;
import java.util.Objects;

public class TaskDetailActivity extends AppCompatActivity {

    public static final String EXTRA_TASK_NAME = "EXTRA_TASK_NAME";


    TaskRepositoryInMemoryImpl taskRepo;

    //Create list of tasks
    List<Task> taskList;

    private TaskDetailFragment tdf;


    Task currentTask;
    int currentTaskPos;
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

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        String extraTaskName = extras.getString("EXTRA_TASK_NAME");


        TextView lastTaskView = (TextView) findViewById(R.id.LastTask);
        lastTaskView.append("ExtraName: " + extraTaskName + "\n");


        if (extraTaskName == null){
            currentTask = new Task("");
            currentTaskPos = taskList.size();
            addTaskMode = true;
        }
        else {
            taskRepo = (TaskRepositoryInMemoryImpl) extras.getSerializable("taskRepo");
            taskList = taskRepo.loadTasks();

            for (int i = 0; i < taskList.size(); i++){
                Task tempTask = taskList.get(i);
                lastTaskView.append("tempTask: " + tempTask.getShortName() + "\n");
                if (Objects.equals(extraTaskName, tempTask.getShortName())){
                    lastTaskView.append("tempTaskID: " + tempTask.getId() + "\n");
                    currentTask = tempTask;
                    currentTaskPos = i;

                }
            }
        }

        tdf.showTask(currentTask, currentTaskPos);
    }

}