package com.example.todo.showTaskList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.Task;
import com.example.todo.TaskDetailActivity;
import com.example.todo.TaskListAdapter;
import com.example.todo.databinding.FragmentTaskListBinding;

import java.util.ArrayList;
import java.util.List;

public class TaskListFragment extends Fragment implements TaskListAdapter.TaskSelectionListener {


    public interface TaskListFragmentCallbacks {

        void addNewTask();
        void onTaskSelected(Task task);

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

        return binding.getRoot();
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
        Intent intent = new Intent(getActivity(), TaskDetailActivity.class);
        intent.putExtra("EXTRA_TASK_NAME", (Bundle) null);
        startActivity(intent);
    }
}
