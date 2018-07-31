package com.whoeverlovely.todo.taskDetails;

import com.whoeverlovely.todo.data.Task;
import com.whoeverlovely.todo.data.TaskDataRepository;
import com.whoeverlovely.todo.data.TaskDataSource;

public class TaskDetailsPresenter implements TaskDetailsContract.Presenter {

    private TaskDetailsContract.View mTaskDetailsView;
    private TaskDataRepository mTaskDataRepository;

    private int mTaskId;

    public TaskDetailsPresenter(TaskDetailsContract.View taskDetailsView,
                                TaskDataRepository taskDataRepository, int taskId) {
        mTaskDataRepository = taskDataRepository;
        mTaskDetailsView = taskDetailsView;
        mTaskId = taskId;

        mTaskDetailsView.setPresenter(this);
    }

    private void loadTaskDetails(int taskId) {
        mTaskDataRepository.getTask(taskId, new TaskDataSource.GetTaskCallback() {
            @Override
            public void onTaskLoaded(Task task) {
                if (!mTaskDetailsView.isActive())
                    return;

                if (task == null)
                    mTaskDetailsView.showTaskNotAvailableMessage();
                else {
                    mTaskDetailsView.showTaskDetails(task);
                    mTaskDetailsView.showCompletionStatus(task);
                }
            }

            @Override
            public void onDataNotAvailable() {
                if (!mTaskDetailsView.isActive())
                    return;

                mTaskDetailsView.showTaskNotAvailableMessage();
            }
        });
    }

    @Override
    public void start() {
        loadTaskDetails(mTaskId);
    }

    @Override
    public void completeTask(Task task) {
        mTaskDetailsView.showTaskCompletedMessage();
        mTaskDataRepository.completeTask(task.getId());
    }

    @Override
    public void activateTask(Task task) {
        mTaskDetailsView.showTaskActivatedMessage();
        mTaskDataRepository.activateTask(task.getId());
    }

    @Override
    public void editTask() {
        mTaskDetailsView.showAddEditTask(mTaskId);
    }
}
