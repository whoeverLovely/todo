package com.whoeverlovely.todo.taskDetails;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.whoeverlovely.todo.R;
import com.whoeverlovely.todo.data.TaskDataRepository;
import com.whoeverlovely.todo.data.TaskDataSource;
import com.whoeverlovely.todo.data.local.LocalTaskDataSource;
import com.whoeverlovely.todo.data.local.TodoDatabase;
import com.whoeverlovely.todo.data.remote.RemoteTaskDataSource;
import com.whoeverlovely.todo.util.AppExecutors;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_TASK_ID = "task_id";
    public static final int TASK_DEFAULT_ID = -1;

    TaskDetailsContract.Presenter mPresenter;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        ButterKnife.bind(this);

        // Set up ToolBar
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

            actionBar.setTitle("Task Details");
        }

        // Retrieve taskId from extra
        Intent intent = getIntent();
        int taskId = intent.getIntExtra(EXTRA_TASK_ID, TASK_DEFAULT_ID);

        // Add fragment
        TaskDetailsFragment fragment = (TaskDetailsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content_frame);
        if (fragment == null) {
            fragment = TaskDetailsFragment.newInstance(taskId);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.content_frame, fragment);
            transaction.commit();
        }

        // Create Presenter
        TaskDataSource localTaskDataSource = LocalTaskDataSource
                .getInstance(TodoDatabase.getInstance(this).taskDao(), new AppExecutors());
        TaskDataSource remoteTaskDataSource = RemoteTaskDataSource.getInstance();
        mPresenter = new TaskDetailsPresenter(fragment, TaskDataRepository
                .getInstance(localTaskDataSource, remoteTaskDataSource), taskId);
    }
}
