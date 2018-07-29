package com.whoeverlovely.todo.data.remote;

import android.os.Handler;

import com.whoeverlovely.todo.data.Task;
import com.whoeverlovely.todo.data.TaskDataSource;

import java.util.LinkedList;
import java.util.List;

public class RemoteTaskDataSource implements TaskDataSource {

    private static RemoteTaskDataSource instance;

    private static List<Task> tasks;

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
                if (tasks == null)
                    fakeRemoteDataInit();
                if (tasks.isEmpty())
                    getTasksCallback.onDataNotAvailable();
                else
                    getTasksCallback.onTasksLoaded(tasks);
            }
        }, 5000);

    }

    @Override
    public void getTask(final int taskId, final GetTaskCallback getTaskCallback) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (tasks == null)
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
                if (tasks == null)
                    fakeRemoteDataInit();
                tasks.add(task);
            }
        }, 5000);
    }

    private List<Task> fakeRemoteDataInit() {
        tasks = new LinkedList<>();

        Task task1 = new Task("fake remote task 1 title", "fake remote task 1 description", false);
        Task task2 = new Task("fake remote task 2 title", "fake remote task 2 description", false);
        Task task3 = new Task("fake remote task 3 title", "fake remote task 3 description", false);

        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);

        return tasks;
    }

    private Task getFakeRemoteDataById(int taskId) {
        List<Task> tasks = fakeRemoteDataInit();
        return tasks.get(taskId);
    }

}
