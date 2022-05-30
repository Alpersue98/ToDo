package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.List;
import java.util.Objects;

public class TaskDetailActivity extends AppCompatActivity {

    public static final String EXTRA_TASK_NAME = "EXTRA_TASK_NAME";

    TextView textView;
    EditText editName;
    EditText editDesc;
    CheckBox doneBox;

    //Create list of tasks
    TaskRepositoryInMemoryImpl taskRepo = new TaskRepositoryInMemoryImpl();
    List<Task> taskList = taskRepo.loadTasks();


    Task currentTask;
    int currentTaskPos;
    boolean addTaskMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String extraTaskName = intent.getStringExtra("EXTRA_TASK_NAME");

        TextView lastTaskView = (TextView) findViewById(R.id.LastTask);
        lastTaskView.append("ExtraName: " + extraTaskName + "\n");


        if (Objects.equals(extraTaskName, null)){
            currentTask = new Task("");
            currentTaskPos = taskList.size();
            addTaskMode = true;
        }
        else {
            for (int i = 0; i < taskList.size(); i++){
                Task tempTask = taskList.get(i);
                lastTaskView.append("tempTask: " + tempTask.getShortName() + "\n");
                //TODO: Problems with same task name, should use ID instead
                if (Objects.equals(extraTaskName, tempTask.getShortName())){
                    lastTaskView.append("tempTaskID: " + tempTask.getShortName() + "\n");
                    currentTask = tempTask;
                    currentTaskPos = i;

                }
            }
        }


        //TODO: Adjust font size so text always fits in views

        //Task Name
        textView = (TextView) findViewById(R.id.nameView);
        textView.setText("Task");
        editName = (EditText) findViewById(R.id.editName);
        //Get and display task name
        editName.setText(currentTask.getShortName());

        //Description
        textView = (TextView) findViewById(R.id.descView);
        textView.setText("Description");
        editDesc = (EditText) findViewById(R.id.editDescription);
        //Get and display task description
        editDesc.setText(currentTask.getDescription());

        //CreationDate
        textView = (TextView) findViewById(R.id.dateView);
        textView.setText("Creation Date");
        textView = (TextView) findViewById(R.id.text_Date);
        //Get and display creation date
        textView.setText(DateFormat.getDateInstance().format(currentTask.getCreationDate()));

        //Done
        textView = (TextView) findViewById(R.id.doneView);
        textView.setText("Done");
        doneBox = (CheckBox) findViewById(R.id.checkBox);
        //Set box as checked is task is saved as done
        doneBox.setChecked(currentTask.isDone());


    }

    //Save changes to current task
    //Called by onClick of saveButton
    public void saveTask(View view) {

        //create new Task with attributes from layout
        currentTask.setShortName(editName.getText().toString());
        currentTask.setDescription(editDesc.getText().toString());
        currentTask.setDone(doneBox.isChecked());

        if(addTaskMode == true){
            taskList.add(currentTaskPos, currentTask);
        }
        else {
            taskList.set(currentTaskPos, currentTask);
        }


        //Show attributes of last task in list (for testing)
        TextView lastTaskView = (TextView) findViewById(R.id.LastTask);
        lastTaskView.setText("Current Task: \n");
        Task lastTask = (Task) taskList.get(currentTaskPos);
        lastTaskView.append("Name: " + lastTask.getShortName() + "\n");
        lastTaskView.append("Desc: " + lastTask.getDescription() + "\n");
        lastTaskView.append("Done: " + lastTask.isDone() + "\n");
    }
}