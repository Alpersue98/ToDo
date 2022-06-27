package com.example.todo.model;
/*
import com.example.todo.database.AppDatabase;
import com.example.todo.database.TasksDao;
*/

import com.example.todo.ToDoApp;
import com.example.todo.database.AppDatabase;
import com.example.todo.database.TasksDao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by thorsten on 23.03.20.
 *
 */

public class TaskRepositoryInMemoryImpl implements TaskRepository, Serializable {

    private static TaskRepositoryInMemoryImpl instance;




    public AppDatabase db;
    public TasksDao dao;


    public static synchronized TaskRepositoryInMemoryImpl getInstance() {
        if (instance == null) {
            instance = new TaskRepositoryInMemoryImpl();
        }
        return instance;
    }




    public TaskRepositoryInMemoryImpl () {

        db = ToDoApp.appDatabase;
        dao = db.tasksDao();

        List<Task> mTasks = new ArrayList<>();

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

        dao.deleteAllTasks();
        dao.insertTasks(mTasks);
    }


    @Override
    public List<Task> loadTasks() {
        return dao.getAllTasks();
    }

    //Return only finished Tasks (for Filtering)
    @Override
    public List<Task> getFinishedTasks(List<Task> taskList){

        for (int i=0; i < taskList.size(); i++) {
            Task task = taskList.get(i);
            if (task.isDone()) {
                taskList.remove(task);
                i--;
            }
        }

        return taskList;
    }

    //Find and delete finished tasks from list, returns list of unfinished tasks
    @Override
    public List<Task> deleteFinishedTasks(List<Task> mTasks) {

        for (int i=0; i < mTasks.size(); i++) {
            Task task = mTasks.get(i);
            if (task.isDone()) {
                dao.deleteTask(task);
                mTasks.remove(task);
                i--;
            }
        }
        return mTasks;
    }

    @Override
    public void addTask(Task task) {
        dao.addTask(task);
    }

    @Override
    public void updateTask(Task task) {
        dao.updateTask(task);
    }
}
