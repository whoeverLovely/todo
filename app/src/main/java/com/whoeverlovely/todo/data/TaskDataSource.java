package com.whoeverlovely.todo.data;

import java.util.List;

public interface TaskDataSource {

    interface GetTasksCallback {
        void onTasksLoaded(List<Task> tasks);
        void onDataNotAvailable();
    }

    interface GetTaskCallback {
        void onTaskLoaded(Task task);
        void onDataNotAvailable();
    }

    void getTasks(GetTasksCallback getTasksCallback);
    void getTask(int taskId, GetTaskCallback getTaskCallback);
    void saveTask(Task task);
    void refreshTasks();
    void completeTask(int taskId);
    void activateTask(int taskId);

}
