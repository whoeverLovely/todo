package com.whoeverlovely.todo.tasks;

import com.whoeverlovely.todo.BasePresenter;
import com.whoeverlovely.todo.BaseView;
import com.whoeverlovely.todo.data.Task;

import java.util.List;

public interface TasksContract {

    interface View extends BaseView<Presenter> {
        void setLoadingIndicator(boolean visible);
        void showTasks(List<Task> tasks);
        void showAddTask();
        void showTaskDetails(int taskId);
        void showNoTasks();
        void showTasksLoadingError();
        void showSuccessfulSavedMessage();
        void showTaskCompleted();
        void showTaskActivated();

    }

    interface Presenter extends BasePresenter {
        void result(int requestCode, int resultCode);
        void loadTasks(boolean forceUpdate);
        void addNewTask();
        void openTaskDetails(Task requestedTask);
        void completeTask(Task completedTask);
        void activatedTask(Task activatedTask);
    }
}
