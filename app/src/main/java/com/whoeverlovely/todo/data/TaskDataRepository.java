package com.whoeverlovely.todo.data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TaskDataRepository implements TaskDataSource{

    private TaskDataSource mLocalTaskDataSource;
    private TaskDataSource mRemoteTaskDataSource;
    private static TaskDataRepository instance;

    boolean mCacheIsDirty;
    Map<Integer, Task> mCachedTasks;

    private TaskDataRepository(TaskDataSource localTaskDataSource, TaskDataSource remoteTaskDataSource) {
        mLocalTaskDataSource = localTaskDataSource;
        mRemoteTaskDataSource = remoteTaskDataSource;
    }

    public static synchronized TaskDataRepository getInstance(TaskDataSource localTaskDataSource, TaskDataSource remoteTaskDataSource) {
        if (instance == null)
            instance = new TaskDataRepository(localTaskDataSource, remoteTaskDataSource);
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }

    @Override
    public void getTasks(GetTasksCallback getTasksCallback) {
        if (!mCacheIsDirty && mCachedTasks != null) {
            // Load tasks from remote data source only cached tasks is dirty and exists
            getTasksCallback.onTasksLoaded(new ArrayList<Task>(mCachedTasks.values()));
            return;
        }

        if (mCacheIsDirty) {
            // Retrieve data from remote data source
            getTasksFromRemoteDataSource(getTasksCallback);
        } else {
            // Query the local database, if it's not available query remote data source
            getTasksFromLocalDataSource(getTasksCallback);
        }


    }



    @Override
    public void getTask(int taskId, GetTaskCallback getTaskCallback) {

    }

    @Override
    public void saveTask(Task tasks) {

    }

    private void getTasksFromRemoteDataSource(final GetTasksCallback getTasksCallback) {
        mRemoteTaskDataSource.getTasks(new GetTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                refreshCache(tasks);
                refreshLocalDataSource(tasks);
                getTasksCallback.onTasksLoaded(tasks);
            }

            @Override
            public void onDataNotAvailable() {
                getTasksCallback.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(List<Task> tasks) {
        if (mCachedTasks == null)
            mCachedTasks = new LinkedHashMap<>();

        mCachedTasks.clear();
        for(Task task:tasks) {
            mCachedTasks.put(task.getId(), task);
        }

        mCacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<Task> tasks) {
        for (Task task: tasks) {
            mLocalTaskDataSource.saveTask(task);
        }
    }

    private void getTasksFromLocalDataSource(final GetTasksCallback getTasksCallback) {
        mLocalTaskDataSource.getTasks(new GetTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                refreshCache(tasks);
                getTasksCallback.onTasksLoaded(tasks);
            }

            @Override
            public void onDataNotAvailable() {
                getTasksFromRemoteDataSource(getTasksCallback);

            }
        });
    }
}
