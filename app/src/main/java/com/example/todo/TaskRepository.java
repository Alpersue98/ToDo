package com.example.todo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by thorsten on 23.03.20.
 * Interface for Task Lists
 */


public interface TaskRepository {


    List<Task> loadTasks();

    void deleteFinishedTasks();

    void addTask(Task task);

    void updateTask(Task task, int position);

}
