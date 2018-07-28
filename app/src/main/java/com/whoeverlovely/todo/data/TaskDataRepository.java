package com.whoeverlovely.todo.data;

public class TaskDataRepository implements TaskDataSource{

    private TaskDataSource mLocalTaskDataSource;
    private TaskDataSource mRemoteTaskDataSource;
    private static TaskDataRepository instance;


    private TaskDataRepository(TaskDataSource localTaskDataSource, TaskDataSource remoteTaskDataSource) {
        mLocalTaskDataSource = localTaskDataSource;
        mRemoteTaskDataSource = remoteTaskDataSource;
    }

    public static synchronized TaskDataRepository getInstance(TaskDataSource localTaskDataSource, TaskDataSource remoteTaskDataSource) {
        if (instance == null)
            instance = new TaskDataRepository(localTaskDataSource, remoteTaskDataSource);
        return instance;
    }

    @Override
    public void getTasks(GetTasksCallback getTasksCallback) {

    }

    @Override
    public void getTask(int taskId, GetTaskCallback getTaskCallback) {

    }
}
