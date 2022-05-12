package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import java.util.List;

public class TaskListActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);



        //Create list of tasks
        TaskRepositoryInMemoryImpl taskRepo = new TaskRepositoryInMemoryImpl();
        List<Task> taskList = taskRepo.loadTasks();
        //Get last task from list
        Task currentTask = (Task) taskList.get(taskList.size()-1);


        ArrayAdapter<Task> adapter
                = new ArrayAdapter<Task>(this,
                android.R.layout.simple_list_item_1,
                taskList);

        // set up the RecyclerView
        RecyclerView taskRecycler = findViewById(R.id.taskRecycler);
        taskRecycler.setAdapter(adapter);

        //adapter = new TaskListAdapter(this, taskList);
        //adapter.setClickListener(this);
        //taskRecycler.setAdapter(adapter);
    }
}