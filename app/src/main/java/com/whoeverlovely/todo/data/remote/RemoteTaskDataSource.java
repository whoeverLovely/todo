package com.whoeverlovely.todo.data.remote;

import android.os.Handler;
import android.os.Looper;

import com.whoeverlovely.todo.data.Task;
import com.whoeverlovely.todo.data.TaskDataSource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class RemoteTaskDataSource implements TaskDataSource {

    private static RemoteTaskDataSource instance;

    private static HashMap<Integer,Task> mTasks;

    public static synchronized RemoteTaskDataSource getInstance() {
        if (instance == null) {
            instance = new RemoteTaskDataSource();
            fakeRemoteDataInit();
        }
        return instance;
    }

    @Override
    public void getTasks(final GetTasksCallback getTasksCallback) {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mTasks.isEmpty())
                    getTasksCallback.onDataNotAvailable();
                else
                    getTasksCallback.onTasksLoaded(new ArrayList<Task>(mTasks.values()));
            }
        }, 2000);

    }

    @Override
    public void getTask(final int taskId, final GetTaskCallback getTaskCallback) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Task task = getFakeRemoteDataById(taskId);
                if (task == null)
                    getTaskCallback.onDataNotAvailable();
                else
                    getTaskCallback.onTaskLoaded(task);
            }
        }, 1000);

    }

    @Override
    public void saveTask(final Task task) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTasks.put(task.getId(), task);
            }
        }, 1000);
    }

    @Override
    public void refreshTasks() {
        // TaskDataRepository will handle the logic for refreshTasks

    }

    @Override
    public void completeTask(int taskId) {
        Task task = getFakeRemoteDataById(taskId);
        if (task != null)
            task.setCompleted(true);
    }

    @Override
    public void activateTask(int taskId) {
        Task task = getFakeRemoteDataById(taskId);
        if (task != null)
            task.setCompleted(false);
    }

    static void fakeRemoteDataInit() {
        mTasks = new LinkedHashMap<>();

        Task task1 = new Task(100, "fake remote task 1 title", "fake remote task 1 description", false);
        Task task2 = new Task(101, "fake remote task 2 title", "fake remote task 2 description", false);
        Task task3 = new Task(102, "fake remote task 3 title", "fake remote task 3 description", false);

        mTasks.put(task1.getId(), task1);
        mTasks.put(task2.getId(), task2);
        mTasks.put(task3.getId(), task3);
    }

    Task getFakeRemoteDataById(int taskId) {
        return mTasks.get(taskId);
    }

}
