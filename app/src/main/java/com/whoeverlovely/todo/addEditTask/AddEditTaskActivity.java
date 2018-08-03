package com.whoeverlovely.todo.addEditTask;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.whoeverlovely.todo.R;
import com.whoeverlovely.todo.data.TaskDataRepository;
import com.whoeverlovely.todo.data.TaskDataSource;
import com.whoeverlovely.todo.data.local.LocalTaskDataSource;
import com.whoeverlovely.todo.data.local.TodoDatabase;
import com.whoeverlovely.todo.data.remote.RemoteTaskDataSource;
import com.whoeverlovely.todo.util.AppExecutors;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class AddEditTaskActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_TASK = 1;
    public static final int TASK_DEFAULT_ID = -1;
    public static final String EXTRA_TASK_ID = "task_id";

    private AddEditTaskContract.Presenter mPresent;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        ButterKnife.bind(this);

        // Retrieve taskId from extra
        Intent intent = getIntent();
        int taskId = intent.getIntExtra(EXTRA_TASK_ID, TASK_DEFAULT_ID);

        // Set up ToolBar
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

            if (taskId == TASK_DEFAULT_ID)
                actionBar.setTitle("Add New Task");
            else
                actionBar.setTitle("Edit Task");
        }

        // Add fragment
        AddEditTaskFragment fragment = (AddEditTaskFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment == null) {
            fragment = AddEditTaskFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.content_frame, fragment);
            transaction.commit();
        }

        // Create presenter

        TaskDataSource localTaskDataSource = LocalTaskDataSource
                .getInstance(TodoDatabase.getInstance(this).taskDao(), new AppExecutors());
        TaskDataSource remoteTaskDataSource = RemoteTaskDataSource.getInstance();
        mPresent = new AddEditTaskPresenter(fragment, TaskDataRepository
                .getInstance(localTaskDataSource, remoteTaskDataSource), taskId);

    }
}
