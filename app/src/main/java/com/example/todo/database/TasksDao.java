package com.example.todo.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todo.model.Task;

import java.util.List;


@Dao
public interface TasksDao {

    @Insert
    void addTask(Task task);

    @Update
    void updateTask(Task task);

    @Query("select * from tasks")
    List<Task> getAllTasks();

    @Query("delete from tasks")
    void deleteAllTasks();

    @Insert
    void insertTasks(List<Task> tasks);
}