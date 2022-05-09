package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    TextView taskTest;
    EditText editName;
    EditText editDesc;
    CheckBox doneBox;
    Button saveButton;
    TaskRepositoryInMemoryImpl taskList = new TaskRepositoryInMemoryImpl();
    List myTaskList = taskList.loadTasks();
    Task myTask = (Task) myTaskList.get(myTaskList.size()-1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName = (EditText) findViewById(R.id.editName);
        editDesc = (EditText) findViewById(R.id.editDescription);
        doneBox = (CheckBox) findViewById(R.id.checkBox);

        //taskTest = (TextView) findViewById(R.id.taskTest);

        //Task Name
        textView = (TextView) findViewById(R.id.textView);
        textView.setText("Task");
        editName.setText(myTask.getShortName());

        //Description
        textView = (TextView) findViewById(R.id.textView2);
        textView.setText("Description");
        editDesc.setText(myTask.getDescription());

        //CreationDate
        textView = (TextView) findViewById(R.id.textView3);
        textView.setText("Creation Date");

        textView = (TextView) findViewById(R.id.text_Date);
        textView.setText(DateFormat.getDateInstance().format(myTask.getCreationDate()));

        //Done
        textView = (TextView) findViewById(R.id.textViewDone);
        textView.setText("Done");
        doneBox.setChecked(myTask.isDone());


    }

    public void saveTask(View view) {
        Task myTask = new Task(editName.getText().toString());
        myTask.setDescription(editDesc.getText().toString());
        myTask.setDone(doneBox.isChecked());

        /*taskTest.setText("");
        taskTest.append("Name: " + myTask.getShortName() + "\n");
        taskTest.append("Desc: " + myTask.getDescription() + "\n");
        taskTest.append("Done: " + myTask.isDone() + "\n");*/

        myTaskList.remove(myTaskList.size()-1);
        myTaskList.add(myTask);


        TextView lastTaskView = (TextView) findViewById(R.id.LastTask);
        lastTaskView.setText("Last Task in List: \n");
        Task lastTask = (Task) myTaskList.get(myTaskList.size()-1);
        lastTaskView.append("Name: " + lastTask.getShortName() + "\n");
        lastTaskView.append("Desc: " + lastTask.getDescription() + "\n");
        lastTaskView.append("Done: " + lastTask.isDone() + "\n");
    }
}