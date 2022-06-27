package com.example.todo.model;

import com.example.todo.model.Task;

import java.util.List;

/**
 * Created by thorsten on 23.03.20.
 * Interface for Task Lists
 */


public interface TaskRepository {


    List<Task> loadTasks();

    List<Task> getFinishedTasks(List<Task> taskList);

    List<Task> deleteFinishedTasks(List<Task> tasks);

    void addTask(Task task);

    void updateTask(Task task);

}
