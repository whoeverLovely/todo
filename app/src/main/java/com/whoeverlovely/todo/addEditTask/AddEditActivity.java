package com.whoeverlovely.todo.addEditTask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.whoeverlovely.todo.R;

public class AddEditActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_TASK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
    }
}
