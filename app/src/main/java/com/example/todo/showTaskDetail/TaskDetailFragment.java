package com.example.todo.showTaskDetail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.todo.R;
import com.example.todo.model.Task;
import com.example.todo.model.TaskRepositoryInMemoryImpl;
import com.example.todo.databinding.FragmentTaskDetailBinding;

public class TaskDetailFragment extends Fragment{

    private final static String LOG_TAG = TaskDetailFragment.class.getSimpleName();

    TextView textView;
    EditText editName;
    EditText editDesc;
    CheckBox doneBox;

    Task currentTask;
    //int currentTaskPos;
    boolean addTaskMode = false;

    //TaskRepositoryInMemoryImpl taskRepo = TaskRepositoryInMemoryImpl.getInstance();

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

        //TODO: Stürzt bei saveTask ab
        //create new Task with attributes from layout
        currentTask.setShortName(editName.getText().toString());
        currentTask.setDescription(editDesc.getText().toString());
        currentTask.setDone(doneBox.isChecked());

        if(addTaskMode == true){
            //taskRepo.addTask(currentTask);
        }
        else {
            //taskRepo.updateTask(currentTask);
        }


        //Show attributes of last task in list (for testing)
        TextView lastTaskView = (TextView) getView().findViewById(R.id.LastTask);
        lastTaskView.setText("Current Task: \n");
        lastTaskView.append("Name: " + currentTask.getShortName() + "\n");
        lastTaskView.append("Desc: " + currentTask.getDescription() + "\n");
        lastTaskView.append("Done: " + currentTask.isDone() + "\n");
    }

}
