package com.whoeverlovely.todo.taskDetails;

import com.whoeverlovely.todo.BasePresenter;
import com.whoeverlovely.todo.BaseView;
import com.whoeverlovely.todo.data.Task;

public interface TaskDetailsContract {
    interface View extends BaseView<Presenter> {
        void showTaskDetails(Task task);
        void showTaskNotAvailableMessage();
        boolean isActive();
        void showCompletionStatus(Task task);
        void showTaskCompletedMessage();
        void showTaskActivatedMessage();

    }

    interface Presenter extends BasePresenter {
        void completeTask(Task task);
        void activateTask(Task task);
    }
}
