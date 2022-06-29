package com.example.todo.showTaskDetail;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.todo.R;
import com.example.todo.TaskDetailActivity;
import com.example.todo.TaskListActivity;
import com.example.todo.model.Task;
import com.example.todo.model.TaskRepositoryInMemoryImpl;
import com.example.todo.databinding.FragmentTaskDetailBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskDetailFragment extends Fragment{

    private final static String LOG_TAG = TaskDetailFragment.class.getSimpleName();


    TextView textView;
    EditText editName;
    EditText editDesc;
    CheckBox doneBox;


    //Create calendar and date format for date picker
    public Calendar dueCalendar= Calendar.getInstance();
    String format="dd.MM.yy";
    SimpleDateFormat df =new SimpleDateFormat(format);

    Task currentTask;

    public boolean addTaskMode = false;
    public boolean tabletMode = false;

    private FragmentTaskDetailBinding binding;

    public TaskDetailFragment(){

    }

    public static TaskDetailFragment newInstance(){
        TaskDetailFragment fragment = new TaskDetailFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentTaskDetailBinding.inflate(inflater, container, false);

        //Assign listener to save Button
        binding.saveButton.setOnClickListener(v -> {
            saveTask();

        });

        //Assign listener for date picker
        DatePickerDialog.OnDateSetListener date = (view, year, month, day) -> {
            dueCalendar.set(Calendar.YEAR, year);
            dueCalendar.set(Calendar.MONTH, month);
            dueCalendar.set(Calendar.DAY_OF_MONTH, day);
            binding.textDate.setText("");
            updateLabel();
        };

        //Use current date as default due date
        binding.textDate.setText(df.format(dueCalendar.getTime()));

        //Set OnClick Listener for date textfield
        binding.textDate.setOnClickListener( v -> {
            DatePickerDialog dateDialog = new DatePickerDialog(getContext(), date, dueCalendar.get(Calendar.YEAR), dueCalendar.get(Calendar.MONTH), dueCalendar.get(Calendar.DAY_OF_MONTH));
            dateDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            dateDialog.show();
        });
        return binding.getRoot();

    }

    private void updateLabel(){

        //binding.textDate.setText(df.format(dueCalendar.getTime()));
        TextView dateView = (TextView) getView().findViewById(R.id.text_Date);
        dateView.setText(df.format(dueCalendar.getTime()));
    }

    public void showTask(Task task){

            currentTask = task;

            //Task Name
            editName = (EditText) getView().findViewById(R.id.editName);
            //Get and display task name
            editName.setText(currentTask.getShortName());

            //Description
            editDesc = (EditText) getView().findViewById(R.id.editDescription);
            //Get and display task description
            editDesc.setText(currentTask.getDescription());

            //Due Date
            textView = (TextView) getView().findViewById(R.id.text_Date);
            //Get and display due date
            textView.setText(currentTask.getDueDate());

            //Done
            doneBox = (CheckBox) getView().findViewById(R.id.checkBox);
            //Set box as checked is task is saved as done
            doneBox.setChecked(currentTask.isDone());
        }


    public void saveTask() {

        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            TaskRepositoryInMemoryImpl taskRepo = TaskRepositoryInMemoryImpl.getInstance();

            try {
                //create new Task with attributes from layout
                editName = (EditText) getView().findViewById(R.id.editName);
                editDesc = (EditText) getView().findViewById(R.id.editDescription);
                doneBox = (CheckBox) getView().findViewById(R.id.checkBox);

                //check if editName field was left empty
                if(!Objects.equals(editName.getText().toString(), "")){

                    //if a new task is being added
                    if (addTaskMode) {
                        Task tempTask = new Task(editName.getText().toString());
                        tempTask.setDescription(editDesc.getText().toString());
                        tempTask.setDone(doneBox.isChecked());
                        tempTask.setDueDate(df.format(dueCalendar.getTime()));

                        taskRepo.addTask(tempTask);

                        //Refresh task list in tablet mode
                        if (tabletMode){
                            clearTask();
                            ((TaskListActivity)getActivity()).loadTaskList();

                        }
                        else{
                            //Go back to taskList Activity (outside of tablet mode)
                            try{
                                Intent intent = new Intent(((TaskDetailActivity)getActivity()), TaskListActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                            //In case tabletMode is set incorrectly
                            catch (ClassCastException ccE){
                                tabletMode = true;
                                ccE.printStackTrace();
                            }
                        }
                    }

                    //If task is being updated
                    else {
                        //get Task info from layout and update in repo
                        currentTask.setShortName(editName.getText().toString());
                        currentTask.setDescription(editDesc.getText().toString());
                        currentTask.setDone(doneBox.isChecked());
                        currentTask.setDueDate(df.format(dueCalendar.getTime()));

                        taskRepo.updateTask(currentTask);

                        if (tabletMode){
                            //update task list in tablet mode
                            clearTask();
                            ((TaskListActivity)getActivity()).loadTaskList();

                        }
                        else{
                            try{
                                //Go back to taskList Activity (outside of tablet mode)
                                Intent intent = new Intent(getActivity(), TaskListActivity.class);
                                startActivity(intent);
                                getActivity().finish();

                            }
                            //In case tablet mode is set incorrectly
                            catch (ClassCastException ccE){
                                tabletMode = true;
                                ccE.printStackTrace();
                            }
                        }
                    }
                }

            }
            catch (NullPointerException nE){
               nE.printStackTrace();
            }
        });
    }


    //Clear task editTexts (if addTask button is pressed in tablet mode or save button is pressed)
    public void clearTask() {
        Handler handler = new Handler(Looper.getMainLooper());

        handler.post(() -> {

            //Task Name
            editName = (EditText) getView().findViewById(R.id.editName);
            //Get and display task name
            editName.setText("");
            //Description
            editDesc = (EditText) getView().findViewById(R.id.editDescription);
            //Get and display task description
            editDesc.setText("");
            //CreationDate
            textView = (TextView) getView().findViewById(R.id.text_Date);
            //Get and display creation date
            textView.setText("");
            //Done
            doneBox = (CheckBox) getView().findViewById(R.id.checkBox);
            //Set box as checked is task is saved as done
            doneBox.setChecked(false);
        });


    }
}
