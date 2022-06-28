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


    public Calendar dueCalendar= Calendar.getInstance();

    Task currentTask;
    public boolean addTaskMode = false;
    public boolean tabletMode = false;

    String format="dd.MM.yy";
    SimpleDateFormat df =new SimpleDateFormat(format);

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
        binding.saveButton.setOnClickListener(v -> {
            saveTask();

        });
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

        //Todo: text is overlapped with old date on mobile
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

        // Asynchronous execution of db statement
        ExecutorService executor = Executors.newSingleThreadExecutor();


        executor.execute(() -> {
            // Background thread work here:
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
                            catch (ClassCastException CCe){
                                tabletMode = true;
                                return;
                            }
                        }
                    }

                    //If task is being updated
                    else {
                        currentTask.setShortName(editName.getText().toString());
                        currentTask.setDescription(editDesc.getText().toString());
                        currentTask.setDone(doneBox.isChecked());
                        currentTask.setDueDate(df.format(dueCalendar.getTime()));


                        taskRepo.updateTask(currentTask);
                        if (tabletMode){
                            clearTask();
                            ((TaskListActivity)getActivity()).loadTaskList();

                        }
                        else{
                            try{
                                //TODO: On tablet the app sometimes crashes here if tabletMode is set incorrectly
                                //Go back to taskList Activity (outside of tablet mode)
                                Intent intent = new Intent(((TaskDetailActivity)getActivity()), TaskListActivity.class);
                                startActivity(intent);
                                getActivity().finish();

                            }
                            catch (ClassCastException CCe){
                                tabletMode = true;
                                return;
                            }
                        }
                    }
                }

            }
            catch (NullPointerException nE){
                //TextView lastTaskView = (TextView) getView().findViewById(R.id.LastTask);
                //lastTaskView.append("NullPointer Exception!" +  nE.getMessage());
                return;
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
