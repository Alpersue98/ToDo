package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.List;

public class TaskDetailActivity extends AppCompatActivity {

    TextView textView;
    EditText editName;
    EditText editDesc;
    CheckBox doneBox;

    //Create list of tasks
    TaskRepositoryInMemoryImpl taskRepo = new TaskRepositoryInMemoryImpl();
    List<Task> taskList = taskRepo.loadTasks();
    //Get last task from list
    Task currentTask = (Task) taskList.get(taskList.size()-1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        Task newTask = new Task(editName.getText().toString());
        newTask.setDescription(editDesc.getText().toString());
        newTask.setDone(doneBox.isChecked());
        //!TODO Creation date should be unchanged when editing tasks

        //remove last task in list
        taskList.remove(taskList.size()-1);
        //replace with updated task by appending to list
        taskList.add(newTask);

        //Show attributes of last task in list (for testing)
        TextView lastTaskView = (TextView) findViewById(R.id.LastTask);
        lastTaskView.setText("Last Task in List: \n");
        Task lastTask = (Task) taskList.get(taskList.size()-1);
        lastTaskView.append("Name: " + lastTask.getShortName() + "\n");
        lastTaskView.append("Desc: " + lastTask.getDescription() + "\n");
        lastTaskView.append("Done: " + lastTask.isDone() + "\n");
    }
}