package com.whoeverlovely.todo.data.remote;

import android.os.Handler;
import android.preference.ListPreference;

import com.whoeverlovely.todo.data.Task;
import com.whoeverlovely.todo.data.TaskDataSource;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.LogRecord;

public class RemoteTaskDataSource implements TaskDataSource {

    private static RemoteTaskDataSource instance;

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
                List<Task> tasks = getFakeRemoteData();
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
                Task task = getFakeRemoteData(taskId);
                if (task == null)
                    getTaskCallback.onDataNotAvailable();
                else
                    getTaskCallback.onTaskLoaded(task);
            }
        }, 5000);

    }

    private List<Task> getFakeRemoteData() {
        Task task1 = new Task("fake remote task 1 title", "fake remote task 1 description", false);
        Task task2 = new Task("fake remote task 2 title", "fake remote task 2 description", false);
        Task task3 = new Task("fake remote task 3 title", "fake remote task 3 description", false);

        List<Task> tasks = new LinkedList<>();
        tasks.add(task1);
        tasks.add(task2);
        tasks.add(task3);

        return tasks;
    }

    private Task getFakeRemoteData(int taskId) {
        List<Task> tasks = getFakeRemoteData();
        return tasks.get(taskId);
    }

}
