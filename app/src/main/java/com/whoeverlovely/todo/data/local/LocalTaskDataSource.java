package com.whoeverlovely.todo.data.local;

import com.whoeverlovely.todo.data.Task;
import com.whoeverlovely.todo.data.TaskDataSource;
import com.whoeverlovely.todo.util.AppExecutors;

import java.util.List;

public class LocalTaskDataSource implements TaskDataSource {

    private static LocalTaskDataSource instance;

    private TaskDao mTaskDao;
    private AppExecutors mAppExecutors;

    private LocalTaskDataSource(TaskDao taskDao, AppExecutors appExecutors) {
        mTaskDao = taskDao;
        mAppExecutors = appExecutors;
    }

    public static synchronized LocalTaskDataSource getInstance(TaskDao taskDao, AppExecutors appExecutors) {

        if (instance == null) {
            instance = new LocalTaskDataSource(taskDao, appExecutors);
        }
        return instance;
    }

    @Override
    public void getTasks(final GetTasksCallback getTasksCallback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final List<Task> tasks = mTaskDao.getTasks();

                mAppExecutors.getMainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (tasks.isEmpty())
                            getTasksCallback.onDataNotAvailable();
                        else
                            getTasksCallback.onTasksLoaded(tasks);
                    }
                });
            }
        };
        mAppExecutors.getDiskIO().execute(runnable);
    }

    @Override
    public void getTask(final int taskId, final GetTaskCallback getTaskCallback) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final Task task = mTaskDao.getTaskById(taskId);

                mAppExecutors.getMainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (task == null)
                            getTaskCallback.onDataNotAvailable();
                        else
                            getTaskCallback.onTaskLoaded(task);
                    }
                });
            }
        };

        mAppExecutors.getDiskIO().execute(runnable);
    }

    @Override
    public void saveTask(final Task task) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mTaskDao.insertTask(task);
            }
        };

        mAppExecutors.getDiskIO().execute(runnable);
    }

    @Override
    public void refreshTasks() {
        // TaskDataRepository will handle the logic for refreshTasks
    }

    @Override
    public void completeTask(final int taskId) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mTaskDao.updateTaskCompleted(taskId, true);
            }
        };

        mAppExecutors.getDiskIO().execute(runnable);
    }

    @Override
    public void activateTask(final int taskId) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mTaskDao.updateTaskCompleted(taskId, false);
            }
        };

        mAppExecutors.getDiskIO().execute(runnable);
    }
}
