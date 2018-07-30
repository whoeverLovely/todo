package com.whoeverlovely.todo.tasks;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.whoeverlovely.todo.R;
import com.whoeverlovely.todo.data.TaskDataRepository;
import com.whoeverlovely.todo.data.TaskDataSource;
import com.whoeverlovely.todo.data.local.LocalTaskDataSource;
import com.whoeverlovely.todo.data.local.TodoDatabase;
import com.whoeverlovely.todo.data.remote.RemoteTaskDataSource;
import com.whoeverlovely.todo.util.AppExecutors;

public class TasksActivity extends AppCompatActivity {

    private TasksPresenter mTasksPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        // Add fragment
        TasksFragment tasksFragment = (TasksFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content_frame);
        if (tasksFragment == null)
            tasksFragment = TasksFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.content_frame, tasksFragment);
        transaction.commit();

        // Create presenter
        TaskDataSource localTaskDataSource = LocalTaskDataSource
                .getInstance(TodoDatabase.getInstance(this).taskDao(), new AppExecutors());
        TaskDataSource remoteTaskDataSource = RemoteTaskDataSource.getInstance();
        mTasksPresenter = new TasksPresenter(TaskDataRepository
                .getInstance(localTaskDataSource, remoteTaskDataSource), tasksFragment);
    }
}
