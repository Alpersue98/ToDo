package com.example.todo;
/*
import com.example.todo.database.AppDatabase;
import com.example.todo.database.TasksDao;
*/

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thorsten on 23.03.20.
 *
 */

public class TaskRepositoryInMemoryImpl implements TaskRepository, Serializable {

    private static TaskRepositoryInMemoryImpl instance;

    public static List<Task> mTasks;

    //public AppDatabase db;
    //public TasksDao dao;


    public static synchronized TaskRepositoryInMemoryImpl getInstance() {
        if (instance == null) {
            instance = new TaskRepositoryInMemoryImpl();
        }
        return instance;
    }


    public TaskRepositoryInMemoryImpl () {
        mTasks = new ArrayList<>();

        //Fill list with task
        Task myTask = new Task("Empty the trash");
        myTask.setDescription("Someone has to get the dirty jobs done...");
        myTask.setDone(true);
        mTasks.add(myTask);
        mTasks.add(new Task("Groceries"));
        mTasks.add(new Task("Call parents"));
        myTask = new Task("Do Android programming");
        myTask.setDescription("Nobody said it would be easy!");
        myTask.setDone(true);
        mTasks.add(myTask);

//        for (int i=0; i<40; i++)
//            mTasks.add(new Task("Task" + i));
    }

    //@Override
    public List<Task> loadTasks() {
        return mTasks;
    }

    //@Override
    //Find and delete finished tasks from list
    public void deleteFinishedTasks() {
        for (int i=0; i<mTasks.size(); i++) {
            Task task = mTasks.get(i);
            if (task.isDone()) {
                mTasks.remove(task);
                i--;
            }
        }
    }

    //@Override
    public void addTask(Task task) {
        mTasks.add(task);
    }

    //@Override
    public void updateTask(Task task, int position) {
        mTasks.set(position, task);
    }
}
