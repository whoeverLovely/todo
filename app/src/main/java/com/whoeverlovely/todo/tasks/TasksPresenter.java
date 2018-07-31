package com.whoeverlovely.todo.tasks;

import android.app.Activity;

import com.whoeverlovely.todo.BasePresenter;
import com.whoeverlovely.todo.addEditTask.AddEditActivity;
import com.whoeverlovely.todo.data.Task;
import com.whoeverlovely.todo.data.TaskDataRepository;
import com.whoeverlovely.todo.data.TaskDataSource;

import java.util.List;

public class TasksPresenter implements TasksContract.Presenter {

    private final TaskDataRepository mTaskDataRepository;
    private final TasksContract.View mTasksView;
    private boolean mFirstLoad = true;

    public TasksPresenter(TaskDataRepository taskDataRepository, TasksContract.View tasksView) {
        mTaskDataRepository = taskDataRepository;
        mTasksView = tasksView;

        mTasksView.setPresenter(this);
    }

    @Override
    public void start() {
        loadTasks(true, true);
    }

    @Override
    public void result(int requestCode, int resultCode) {
        if (requestCode == AddEditActivity.REQUEST_ADD_TASK && resultCode == Activity.RESULT_OK)
            mTasksView.showSuccessfulSavedMessage();
    }

    @Override
    public void loadTasks(boolean forceUpdate) {
        loadTasks(forceUpdate || mFirstLoad, true);
        mFirstLoad = false;

    }

    private void loadTasks(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI)
            mTasksView.setLoadingIndicator(true);
        if (forceUpdate)
            mTaskDataRepository.refreshTasks();

        mTaskDataRepository.getTasks(new TaskDataSource.GetTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                mTasksView.showTasks(tasks);

                if (showLoadingUI)
                    mTasksView.setLoadingIndicator(false);
            }

            @Override
            public void onDataNotAvailable() {
                mTasksView.showTasksLoadingError();
            }
        });
    }

    @Override
    public void addNewTask() {
        mTasksView.showAddTask();
    }

    @Override
    public void openTaskDetails(Task requestedTask) {
        mTasksView.showTaskDetails(requestedTask.getId());
    }

    @Override
    public void completeTask(Task completedTask) {
        mTasksView.showTaskCompleted();
        // Have to reload tasks to update background color
        loadTasks(false, false);
        mTaskDataRepository.completeTask(completedTask.getId());
    }

    @Override
    public void activatedTask(Task activatedTask) {
        mTasksView.showTaskActivated();
        // Have to reload tasks to update background color
        loadTasks(false, false);
        mTaskDataRepository.activateTask(activatedTask.getId());

    }
}
