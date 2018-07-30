package com.whoeverlovely.todo.data.local;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.whoeverlovely.todo.data.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTask(Task task);

    @Delete()
    void deleteTask(Task task);

    @Query("UPDATE tasks SET completed = :completed WHERE id = :taskId")
    void updateTaskCompleted(int taskId, boolean completed);

    @Query("SELECT * FROM tasks")
    List<Task> getTasks();

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    Task getTaskById(int taskId);
}
