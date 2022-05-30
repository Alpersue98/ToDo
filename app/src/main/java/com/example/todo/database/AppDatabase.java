package com.example.todo.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.todo.Task;


/*
@Database(entities = {Task.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    // Database instance is created und made available
    // in Application subclass.
    //
    // Reason: Creation of Room database requires context parameter.
    // If repository would create database, it would depend on context,
    // thus reducing testability.

    public abstract TasksDao tasksDao();
}*/
