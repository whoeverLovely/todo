package com.whoeverlovely.todo.taskDetails;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.whoeverlovely.todo.R;

public class TaskDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_TASK_ID = "task_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
    }
}
