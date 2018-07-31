package com.whoeverlovely.todo.statistics;

import com.whoeverlovely.todo.data.Task;
import com.whoeverlovely.todo.data.TaskDataRepository;
import com.whoeverlovely.todo.data.TaskDataSource;

import java.util.List;

public class StatisticsPresenter implements StatisticsContract.Presenter {

    private StatisticsContract.View mStatisticsView;
    private TaskDataRepository mTaskDataRepository;

    public StatisticsPresenter(StatisticsContract.View statisticsView, TaskDataRepository dataRepository) {
        mStatisticsView = statisticsView;
        mTaskDataRepository = dataRepository;

        mStatisticsView.setPresenter(this);
    }

    @Override
    public void start() {
        loadStatistics();
    }

    private void loadStatistics() {
        mTaskDataRepository.getTasks(new TaskDataSource.GetTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                int completed = 0;
                int activated = 0;

                for (Task task : tasks) {
                    if (task.isCompleted())
                        completed++;
                    else
                        activated++;
                }

                mStatisticsView.showStatistics(completed, activated);
            }

            @Override
            public void onDataNotAvailable() {
                mStatisticsView.showNoStatisticsMessage();

            }
        });

    }
}
