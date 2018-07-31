package com.whoeverlovely.todo.addEditTask;

import com.whoeverlovely.todo.data.Task;
import com.whoeverlovely.todo.data.TaskDataRepository;
import com.whoeverlovely.todo.data.TaskDataSource;

import timber.log.Timber;

public class AddEditTaskPresenter implements AddEditTaskContract.Presenter {

    private AddEditTaskContract.View mAddEditTaskView;
    private TaskDataRepository mTaskDataRepository;

    private int mTaskId;

    public AddEditTaskPresenter(AddEditTaskContract.View addEditTaskView, TaskDataRepository taskDataRepository, int taskId) {
        mAddEditTaskView = addEditTaskView;
        mTaskDataRepository = taskDataRepository;
        mTaskId = taskId;

        mAddEditTaskView.setPresenter(this);
    }

    @Override
    public void start() {
        if (!isNewTask())
            populateTask();
    }


    @Override
    public void saveTask(String title, String description) {
        if (isNewTask())
            addNewTask(title, description);
        else
            editTask(title, description);
    }

    @Override
    public void populateTask() {
        mTaskDataRepository.getTask(mTaskId, new TaskDataSource.GetTaskCallback() {
            @Override
            public void onTaskLoaded(Task task) {
                mAddEditTaskView.showTask(task);
            }

            @Override
            public void onDataNotAvailable() {
                mAddEditTaskView.showTaskNotAvailableMessage();
            }
        });
    }

    private boolean isNewTask() {
        return mTaskId == AddEditTaskActivity.TASK_DEFAULT_ID;
    }

    private void addNewTask(String title, String description) {
        Task task = new Task(title, description, false);
        if (task.isEmpty())
            mAddEditTaskView.showTaskEmptyMessage();
        else {
            mTaskDataRepository.saveTask(task);
            mAddEditTaskView.showTasks();
        }
    }

    private void editTask(String title, String description) {
        Timber.d("the taskid in editTask is " + mTaskId);
        Task task = new Task(mTaskId, title, description, false);
        if (task.isEmpty())
            mAddEditTaskView.showTaskEmptyMessage();
        else {
            mTaskDataRepository.saveTask(task);
            mAddEditTaskView.showTasks();
        }
    }


}
