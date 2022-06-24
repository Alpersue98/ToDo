package com.example.todo.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by thorsten on 21.03.20.
 * Task class containing Id, task Name, description, creation date, and completion status
 */

@Entity(tableName = "tasks")
public class Task implements Parcelable{

    @PrimaryKey(autoGenerate = true)
    private int mId;
    //private Long mId = null;

    private String mShortName;

    private String mDescription;

    private String mCreationDate; // zum Speichern in Room: Converter annotation

    private boolean mDone;

    public Task(String shortName) {
        this.mShortName = shortName;


        String pattern = "dd.MM.yyyy";
        DateFormat df = new SimpleDateFormat(pattern);

        Date creationDate = GregorianCalendar.getInstance().getTime();
        this.mCreationDate = df.format(creationDate);
    }

    //Getters and setters for task attributes
    public int getId() {
        return this.mId;
    }

    public void setId(int id) {this.mId = id;
    }

    public String getShortName() {
        return mShortName;
    }

    public void setShortName(String shortName) {
        this.mShortName = shortName;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

    public String getCreationDate() {
        return mCreationDate;
    }

    public void setCreationDate(String date) {
        this.mCreationDate = date;
    }

    public boolean isDone() {
        return mDone;
    }

    public void setDone(boolean done) {
        this.mDone = done;
    }

    @Override
    public String toString() {
        return mShortName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Task) {
            return this.getId() == ((Task) obj).getId();
        }
        return false;
    }

    protected Task(Parcel in) {
        mShortName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mShortName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //Parcelable interface methods
    public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}

