package com.whoeverlovely.todo.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import com.whoeverlovely.todo.data.Task;

@Database(entities = {Task.class}, version = 1)
public abstract class TodoDatabase extends RoomDatabase {

    private static TodoDatabase sInstance;
    private static final String DATABASE_NAME = "todo";
    private static final Object LOCK = new Object();

    public static TodoDatabase getInstance(Context context) {
        synchronized (LOCK) {
            if (sInstance == null) {

                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        TodoDatabase.class, DATABASE_NAME
                ).build();
            }


            return sInstance;
        }
    }

    public abstract TaskDao taskDao();

}
