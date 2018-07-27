package com.whoeverlovely.todo;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.whoeverlovely.todo.data.Task;
import com.whoeverlovely.todo.data.local.TodoDatabase;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.whoeverlovely.todo", appContext.getPackageName());
    }

    @Test
    public void roomInsert() {
        Context appContext = InstrumentationRegistry.getTargetContext();
        TodoDatabase db = TodoDatabase.getInstance(appContext);

        Task task = new Task("test task title", "test task description", false);
        db.taskDao().insertTask(task);

    }
}
