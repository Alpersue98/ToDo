package com.example.todo;

import android.app.Application;

import androidx.room.Room;

import com.example.todo.database.AppDatabase;

public class ToDoApp extends Application {

    public static AppDatabase appDatabase;

    @Override
    public void onCreate() {
        super.onCreate();

        appDatabase = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "tasks-db")
                .build();
    }
}
