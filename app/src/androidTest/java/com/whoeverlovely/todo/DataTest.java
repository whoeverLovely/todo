package com.whoeverlovely.todo;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.whoeverlovely.todo.data.Task;
import com.whoeverlovely.todo.data.TaskDataSource;
import com.whoeverlovely.todo.data.local.LocalTaskDataSource;
import com.whoeverlovely.todo.data.local.TaskDao;
import com.whoeverlovely.todo.data.local.TodoDatabase;
import com.whoeverlovely.todo.util.AppExecutors;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import timber.log.Timber;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class DataTest {

    private final static String TAG = DataTest.class.getSimpleName();
    private Context appContext;
    private TaskDao taskDao;
    private AppExecutors executors;

    @Test
    public void init() {
        appContext = InstrumentationRegistry.getTargetContext();
        taskDao = TodoDatabase.getInstance(appContext).taskDao();
        executors = new AppExecutors();
    }

    public void roomInsert() {
        init();

        Task task = new Task("test task title", "test task description", false);
        taskDao.insertTask(task);
    }

    @Test
    public void localTaskDataSouece() {
        init();

        LocalTaskDataSource.getInstance(taskDao, executors).getTasks(new TaskDataSource.GetTasksCallback() {

            @Override
            public void onTasksLoaded(List<Task> tasks) {
                Log.d(TAG, "Tasks loaded");

                    int id = tasks.get(0).getId();
                    LocalTaskDataSource.getInstance(taskDao, executors).getTask(id, new TaskDataSource.GetTaskCallback() {
                        @Override
                        public void onTaskLoaded(Task task) {
                            Log.d(TAG, "Task loaded description" + task.getDescription());
                        }

                        @Override
                        public void onDataNotAvailable() {
                            Log.d(TAG,"Task loading failed!");

                        }
                    });

            }

            @Override
            public void onDataNotAvailable() {
                Log.d(TAG,"Tasks not available");
            }
        });


    }
}
