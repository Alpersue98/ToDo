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
import com.example.todo.Task;
import com.example.todo.TaskRepositoryInMemoryImpl;
import com.example.todo.databinding.FragmentTaskDetailBinding;

import java.text.DateFormat;

public class TaskDetailFragment extends Fragment  implements View.OnClickListener{

    private final static String LOG_TAG = TaskDetailFragment.class.getSimpleName();

    TextView textView;
    EditText editName;
    EditText editDesc;
    CheckBox doneBox;

    Task currentTask;
    int currentTaskPos;
    boolean addTaskMode = false;

    TaskRepositoryInMemoryImpl taskRepo = new TaskRepositoryInMemoryImpl();

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

        return binding.getRoot();

    }

    public void showTask(Task task, int taskPos){

        currentTask = task;
        currentTaskPos = taskPos;

        //Task Name
        textView = (TextView) getView().findViewById(R.id.nameView);
        textView.setText("Task");
        editName = (EditText) getView().findViewById(R.id.editName);
        //Get and display task name
        editName.setText(currentTask.getShortName());

        //Description
        textView = (TextView) getView().findViewById(R.id.descView);
        textView.setText("Description");
        editDesc = (EditText) getView().findViewById(R.id.editDescription);
        //Get and display task description
        editDesc.setText(currentTask.getDescription());

        //CreationDate
        textView = (TextView) getView().findViewById(R.id.dateView);
        textView.setText("Creation Date");
        textView = (TextView) getView().findViewById(R.id.text_Date);
        //Get and display creation date
        textView.setText(DateFormat.getDateInstance().format(currentTask.getCreationDate()));

        //Done
        textView = (TextView) getView().findViewById(R.id.doneView);
        textView.setText("Done");
        doneBox = (CheckBox) getView().findViewById(R.id.checkBox);
        //Set box as checked is task is saved as done
        doneBox.setChecked( currentTask.isDone());
    }

    public void saveTask(View view) {

        //create new Task with attributes from layout
        currentTask.setShortName(editName.getText().toString());
        currentTask.setDescription(editDesc.getText().toString());
        currentTask.setDone(doneBox.isChecked());

        if(addTaskMode == true){
            taskRepo.addTask(currentTask);
        }
        else {
            taskRepo.updateTask(currentTask, currentTaskPos);
        }


        //Show attributes of last task in list (for testing)
        TextView lastTaskView = (TextView) getView().findViewById(R.id.LastTask);
        lastTaskView.setText("Current Task: \n");
        /*Task lastTask = (Task) taskList.get(currentTaskPos);
        lastTaskView.append("Name: " + lastTask.getShortName() + "\n");
        lastTaskView.append("Desc: " + lastTask.getDescription() + "\n");
        lastTaskView.append("Done: " + lastTask.isDone() + "\n");*/
    }

    @Override
    public void onClick(View view) {

    }
}
