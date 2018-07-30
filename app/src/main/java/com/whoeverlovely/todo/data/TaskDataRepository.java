package com.whoeverlovely.todo.data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TaskDataRepository implements TaskDataSource {

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
    public void getTask(final int taskId, final GetTaskCallback getTaskCallback) {
        if (mCachedTasks != null) {
            Task task = mCachedTasks.get(taskId);
            if (task != null)
                // Get task from cache
                getTaskCallback.onTaskLoaded(task);
            else
                // Get task from local database when it's not available in cache
                mLocalTaskDataSource.getTask(taskId, new GetTaskCallback() {
                    @Override
                    public void onTaskLoaded(Task task) {
                        // Cache the task loaded from local database if the task exists in local database
                        if (mCachedTasks == null)
                            mCachedTasks = new LinkedHashMap<>();
                        mCachedTasks.put(task.getId(), task);
                        getTaskCallback.onTaskLoaded(task);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        // Query remote data source if the task doesn't exist in local database
                        mRemoteTaskDataSource.getTask(taskId, new GetTaskCallback() {
                            @Override
                            public void onTaskLoaded(Task task) {
                                // Save the task loaded from remote data source to local database and memory cache
                                mLocalTaskDataSource.saveTask(task);
                                if (mCachedTasks == null)
                                    mCachedTasks = new LinkedHashMap<>();
                                mCachedTasks.put(task.getId(), task);
                                getTaskCallback.onTaskLoaded(task);
                            }

                            @Override
                            public void onDataNotAvailable() {
                                getTaskCallback.onDataNotAvailable();

                            }
                        });
                    }
                });
        }
    }

    @Override
    public void saveTask(Task task) {
        mRemoteTaskDataSource.saveTask(task);
        mLocalTaskDataSource.saveTask(task);

        if (mCachedTasks == null)
            mCachedTasks = new LinkedHashMap<>();
        mCachedTasks.put(task.getId(), task);
    }

    @Override
    public void refreshTasks() {
        mCacheIsDirty = true;
    }

    @Override
    public void completeTask(final int taskId) {
        mRemoteTaskDataSource.completeTask(taskId);
        mLocalTaskDataSource.completeTask(taskId);

        if (mCachedTasks != null) {
            Task task = mCachedTasks.get(taskId);
            task.setCompleted(true);
        } else {
            getTask(taskId, new GetTaskCallback() {
                @Override
                public void onTaskLoaded(Task task) {
                    completeTask(task.getId());
                }

                @Override
                public void onDataNotAvailable() {

                }
            });
        }
    }

    @Override
    public void activateTask(int taskId) {
        mRemoteTaskDataSource.activateTask(taskId);
        mLocalTaskDataSource.activateTask(taskId);

        if (mCachedTasks != null) {
            Task task = mCachedTasks.get(taskId);
            task.setCompleted(false);
        } else {
            getTask(taskId, new GetTaskCallback() {
                @Override
                public void onTaskLoaded(Task task) {
                    activateTask(task.getId());
                }

                @Override
                public void onDataNotAvailable() {

                }
            });
        }
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
        for (Task task : tasks) {
            mCachedTasks.put(task.getId(), task);
        }

        mCacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<Task> tasks) {
        for (Task task : tasks) {
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
