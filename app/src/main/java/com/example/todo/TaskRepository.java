package com.example.todo;

import java.util.List;

/**
 * Created by thorsten on 23.03.20.
 * Interface for Task Lists
 */


public interface TaskRepository {

    List<Task> loadTasks();

    void deleteFinishedTasks();

    // TODO: add methods for adding new or updating existing tasks

}
