package com.whoeverlovely.todo.addEditTask;

import com.whoeverlovely.todo.BasePresenter;
import com.whoeverlovely.todo.BaseView;
import com.whoeverlovely.todo.data.Task;

public interface AddEditTaskContract {
    interface View extends BaseView<Presenter> {

        void showTaskEmptyMessage();

        void showTasks();

        void showTask(Task task);

        void showTaskNotAvailableMessage();
    }

    interface Presenter extends BasePresenter {
        void saveTask(String title, String description);
        void populateTask();

    }
}
