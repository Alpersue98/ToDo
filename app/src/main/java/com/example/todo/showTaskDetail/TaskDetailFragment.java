package com.example.todo.showTaskDetail;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.todo.R;
import com.example.todo.TaskDetailActivity;
import com.example.todo.model.Task;
import com.example.todo.model.TaskRepositoryInMemoryImpl;
import com.example.todo.databinding.FragmentTaskDetailBinding;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskDetailFragment extends Fragment{

    private final static String LOG_TAG = TaskDetailFragment.class.getSimpleName();


    TextView textView;
    EditText editName;
    EditText editDesc;
    CheckBox doneBox;

    Task currentTask;
    public boolean addTaskMode = false;

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

        return binding.getRoot();

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

            //CreationDate
            textView = (TextView) getView().findViewById(R.id.text_Date);
            //Get and display creation date
            textView.setText(currentTask.getCreationDate());

            //Done
            doneBox = (CheckBox) getView().findViewById(R.id.checkBox);
            //Set box as checked is task is saved as done
            doneBox.setChecked(currentTask.isDone());
        }


    public void saveTask() {

        // Asynchronous execution of db statement
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

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


                    if (addTaskMode) {
                        Task tempTask = new Task(editName.getText().toString());

                        tempTask.setDescription(editDesc.getText().toString());
                        tempTask.setDone(doneBox.isChecked());

                        taskRepo.addTask(tempTask);
                        //TODO: Should load tasklist activity here (unless in tablet mode)
                        //TODO: Should update task list in tablet mode here
                    } else {
                        currentTask.setShortName(editName.getText().toString());
                        currentTask.setDone(doneBox.isChecked());

                        taskRepo.updateTask(currentTask);
                        //TODO: Should update task list in tablet mode here
                    }
                }

            }
            catch (NullPointerException nE){
                TextView lastTaskView = (TextView) getView().findViewById(R.id.LastTask);
                lastTaskView.append("NullPointer Exception!" +  nE.getMessage());
                return;
            }

            handler.post(() -> {

            });
        });

    }

    //Clear task info (if addTask button is pressed in tablet mode or save button is pressed)
    public void clearTask() {
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
    }
}
