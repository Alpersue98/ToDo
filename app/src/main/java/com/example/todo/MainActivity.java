package com.example.todo;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Task
        textView = (TextView) findViewById(R.id.textView);
        textView.setText("Task");

        //Description
        textView = (TextView) findViewById(R.id.textView2);
        textView.setText("Description");

        //CreationDate
        textView = (TextView) findViewById(R.id.textView3);
        textView.setText("Creation Date");

        //get current Date
        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance().format(calendar.getTime());

        //set current Date
        TextView textView_Date = findViewById(R.id.text_Date);
        textView_Date.setText(currentDate);

        //Done
        textView = (TextView) findViewById(R.id.textViewDone);
        textView.setText("Done");
    }
}