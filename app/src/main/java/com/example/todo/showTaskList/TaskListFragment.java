package com.example.todo.showTaskList;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.R;
import com.example.todo.model.Task;
import com.example.todo.TaskListActivity;
import com.example.todo.TaskListAdapter;
import com.example.todo.databinding.FragmentTaskListBinding;
import com.example.todo.model.TaskRepositoryInMemoryImpl;
import com.example.todo.showTaskDetail.TaskDetailFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskListFragment extends Fragment implements TaskListAdapter.TaskSelectionListener, AdapterView.OnItemSelectedListener {


    public interface TaskListFragmentCallbacks {

        void addNewTask();
        void onTaskSelected(Task task);

        void onCheckBoxClick(boolean isChecked,Task task);
    }

    private FragmentTaskListBinding binding;
    private TaskListAdapter adapter;
    private TaskListFragmentCallbacks listener;


    public TaskListFragment(){
    }


    public static TaskListFragment newInstance(){
        TaskListFragment fragment = new TaskListFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentTaskListBinding.inflate(inflater, container, false);

        RecyclerView listView = binding.taskRecycler;
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TaskListAdapter(this);
        listView.setAdapter(adapter);

        //Set listener for add task button
        binding.addTaskButton.setOnClickListener(v -> {
            addNewTask();
        });

        //Create adapter for filter spinner
        Spinner dropdown = binding.FilterSpinner;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.dropdown_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);

        return binding.getRoot();
    }

    //Selection listener for filter spinner
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        //Set Spinner text as empty
        try{
            ((TextView)view).setText(null);
        }
        //Throws NullPointerException when rotating
        catch (NullPointerException nE){
            nE.printStackTrace();
        }

        switch(pos){
            //Showing all tasks
            case 0:
                showFilteredTasks(true);
                break;
            //Showing unfinished tasks
            case 1:
                showFilteredTasks(false);
                break;
            //deleting finished tasks
            case 2:
                deleteFinishedTasks();
                break;
        }

    }

    //Required method for spinner adapter
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //Method for showing all/unfinished tasks
    public void showFilteredTasks(boolean showAll) {

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            TaskRepositoryInMemoryImpl repository = TaskRepositoryInMemoryImpl.getInstance();
            List<Task> tasks = repository.loadTasks();

            //If only unfinished task are supposed to be shown
            if(showAll) {
                handler.post(() -> {
                    setTasks(tasks);
                });
            }

            //if all tasks are supposed to be shown
            else {
                List<Task> finishedTasks = repository.getFinishedTasks(tasks);
                handler.post(() -> {
                    setTasks(finishedTasks);
                });
            }
        });
    }

    //Delete finished tasks from repo
    private void deleteFinishedTasks() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            TaskRepositoryInMemoryImpl repository = TaskRepositoryInMemoryImpl.getInstance();
            List<Task> tasks = repository.loadTasks();
            List <Task> unfinishedTasks = repository.deleteFinishedTasks(tasks);

            handler.post(() -> {
                setTasks(unfinishedTasks);
            });
        });
    }

    //Set tasks in list via list adapter
    public void setTasks(List<Task> tasks) {
        adapter.setTasks((ArrayList<Task>) tasks);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (TaskListFragmentCallbacks) context;
        } catch (ClassCastException ccE) {
            ccE.printStackTrace();;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }


    @Override
    public void onTaskSelected(Task task) {
        listener.onTaskSelected(task);
    }

    public void addNewTask() {
        ((TaskListActivity)getActivity()).addNewTask();
    }

    @Override
    //If checkbox of a task in list is checked
    public void onCheckBoxClick(boolean isChecked, Task task) {
        listener.onCheckBoxClick(isChecked, task);
    }
}
