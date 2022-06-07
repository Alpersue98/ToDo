package com.example.todo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by thorsten on 23.03.20.
 * Interface for Task Lists
 */


public interface TaskRepository {
    //soll auf TaskRepoitoryInMemoryImpl zugreifen, von beiden Aktivitäten verwendet werden
    //Intentfilter in Android.xml


    List<Task> loadTasks();

    void deleteFinishedTasks();

    void addTask(Task task);

    void updateTask(Task task, int position);

}
