package com.whoeverlovely.todo.data.remote;

import android.os.Handler;
import android.os.Looper;

import com.whoeverlovely.todo.data.Task;
import com.whoeverlovely.todo.data.TaskDataSource;

import java.util.LinkedList;
import java.util.List;

public class RemoteTaskDataSource implements TaskDataSource {

    private static RemoteTaskDataSource instance;

    private static List<Task> mTasks;

    public static synchronized RemoteTaskDataSource getInstance() {
        if (instance == null)
            instance = new RemoteTaskDataSource();
        return instance;
    }

    @Override
    public void getTasks(final GetTasksCallback getTasksCallback) {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mTasks == null)
                    fakeRemoteDataInit();
                if (mTasks.isEmpty())
                    getTasksCallback.onDataNotAvailable();
                else
                    getTasksCallback.onTasksLoaded(mTasks);
            }
        }, 5000);

    }

    @Override
    public void getTask(final int taskId, final GetTaskCallback getTaskCallback) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mTasks == null)
                    fakeRemoteDataInit();
                Task task = getFakeRemoteDataById(taskId);
                if (task == null)
                    getTaskCallback.onDataNotAvailable();
                else
                    getTaskCallback.onTaskLoaded(task);
            }
        }, 5000);

    }

    @Override
    public void saveTask(final Task task) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mTasks == null)
                    fakeRemoteDataInit();
                mTasks.add(task);
            }
        }, 5000);
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

    void fakeRemoteDataInit() {
        mTasks = new LinkedList<>();

        Task task1 = new Task(100, "fake remote task 1 title", "fake remote task 1 description", false);
        Task task2 = new Task(101, "fake remote task 2 title", "fake remote task 2 description", false);
        Task task3 = new Task(102, "fake remote task 3 title", "fake remote task 3 description", false);

        mTasks.add(task1);
        mTasks.add(task2);
        mTasks.add(task3);

    }

    Task getFakeRemoteDataById(int taskId) {
        for (Task task : mTasks) {
            if (task.getId() == taskId)
                return task;
        }
        return null;
    }

}
