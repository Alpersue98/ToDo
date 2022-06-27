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


    // TODO: Rename parameter arguments, choose names that match
   // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
    //private String mParam1;
   // private String mParam2;

    public TaskListFragment(){
    }

    public static TaskListFragment newInstance(/*String param1, String param2*/){
        TaskListFragment fragment = new TaskListFragment();
        //Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
       // fragment.setArguments(args);
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
        binding.addTaskButton.setOnClickListener(v -> {
            addNewTask();
        });

        Spinner dropdown = binding.FilterSpinner;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.dropdown_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);

        return binding.getRoot();
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        //Set text as empty
        try{
            ((TextView)view).setText(null);
        }
        //Throws NullPointerException when rotating
        catch (NullPointerException nE){
            return;
        }

        switch(pos){
            case 0:
                showFilteredTasks(true);
                break;
            case 1:
                showFilteredTasks(false);
                break;
            case 2:
                deleteFinishedTasks();
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

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
                //TODO: Sometimes doesn't update, creates lag in Tabletmode
                handler.post(() -> {
                    setTasks(finishedTasks);
                });
            }
        });
    }

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

    public void setTasks(List<Task> tasks) {
        adapter.setTasks((ArrayList<Task>) tasks);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (TaskListFragmentCallbacks) context;
        } catch (ClassCastException cce) {
            throw new ClassCastException(context.toString()
                    + " must implement TaskListFragmentCallbacks");
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
