package com.whoeverlovely.todo.statistics;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.whoeverlovely.todo.R;
import com.whoeverlovely.todo.data.TaskDataRepository;
import com.whoeverlovely.todo.data.TaskDataSource;
import com.whoeverlovely.todo.data.local.LocalTaskDataSource;
import com.whoeverlovely.todo.data.local.TodoDatabase;
import com.whoeverlovely.todo.data.remote.RemoteTaskDataSource;
import com.whoeverlovely.todo.tasks.TasksActivity;
import com.whoeverlovely.todo.util.AppExecutors;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatisticsActivity extends AppCompatActivity {

    private StatisticsContract.Presenter mPresenter;

    @BindView(R.id.navigationView)
    NavigationView mNavigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        ButterKnife.bind(this);

        // Set up ToolBar
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

            Drawable drawable = getResources().getDrawable(R.drawable.ic_menu_48px);
            drawable.mutate();
            drawable.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            actionBar.setHomeAsUpIndicator(drawable);
        }

        // Add Fragment
        StatisticsFragment fragment = (StatisticsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.content_frame);
        if (fragment == null) {
            fragment = StatisticsFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.content_frame, fragment);
            transaction.commit();
        }

        // Create Presenter
        TaskDataSource localTaskDataSource = LocalTaskDataSource
                .getInstance(TodoDatabase.getInstance(this).taskDao(), new AppExecutors());
        TaskDataSource remoteTaskDataSource = RemoteTaskDataSource.getInstance();
        mPresenter = new StatisticsPresenter(fragment,
                TaskDataRepository.getInstance(localTaskDataSource, remoteTaskDataSource));

        setNavigationListener();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setNavigationListener() {
        if (mNavigationView != null) {
            mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    // set item as selected to persist highlight
                    item.setChecked(true);
                    // close drawer when item is tapped
                    mDrawerLayout.closeDrawers();

                    switch (item.getItemId()) {
                        case R.id.drawer_menu_item_tasks:
                            Intent intent = new Intent(StatisticsActivity.this, TasksActivity.class);
                            startActivity(intent);
                            break;
                        case R.id.drawer_menu_item_statistics:
                            break;
                        default:
                            break;
                    }

                    return true;
                }
            });
        }
    }
}
