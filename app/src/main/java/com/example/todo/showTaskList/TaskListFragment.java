package com.example.todo.showTaskList;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.model.Task;
import com.example.todo.TaskListActivity;
import com.example.todo.TaskListAdapter;
import com.example.todo.databinding.FragmentTaskListBinding;
import com.example.todo.model.TaskRepositoryInMemoryImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskListFragment extends Fragment implements TaskListAdapter.TaskSelectionListener {


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

        //TODO: Button only works after second press
        binding.DeleteFinishedButton.setOnClickListener( v -> {
            deleteFinishedTasks();
        });

        binding.showFinishedSwitch.setChecked(true);
        binding.showFinishedSwitch.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                showFilteredTasks();
            }
        });
        return binding.getRoot();
    }

    public void showFilteredTasks() {

        boolean isChecked = binding.showFinishedSwitch.isChecked();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {

            TaskRepositoryInMemoryImpl repository = TaskRepositoryInMemoryImpl.getInstance();
            List<Task> tasks = repository.loadTasks();

            //If only unfinished task are supposed to be shown
            if(isChecked) {
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
    public void onCheckBoxClick(boolean isChecked, Task task) {
        listener.onCheckBoxClick(isChecked, task);
    }
}
