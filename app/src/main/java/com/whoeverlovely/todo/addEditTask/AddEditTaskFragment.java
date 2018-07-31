package com.whoeverlovely.todo.addEditTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.whoeverlovely.todo.R;
import com.whoeverlovely.todo.data.Task;
import com.whoeverlovely.todo.tasks.TasksActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddEditTaskFragment extends Fragment implements AddEditTaskContract.View {

    @BindView(R.id.editText_title_addedit)
    EditText mTitleEditText;

    @BindView(R.id.editText_description_addedit)
    EditText mDescriptionEditText;

    @BindView(R.id.fab_done)
    FloatingActionButton mDoneFab;

    AddEditTaskContract.Presenter mPresenter;

    public AddEditTaskFragment() {
        // Required empty public constructor
    }

    public static AddEditTaskFragment newInstance() {
        return new AddEditTaskFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_edit_task, container, false);
        ButterKnife.bind(this, view);

        mDoneFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.saveTask(mTitleEditText.getText().toString(), mDescriptionEditText.getText().toString());
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(AddEditTaskContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showTaskEmptyMessage() {
        Snackbar.make(mTitleEditText, "Both task title and description can't be empty!", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showTasks() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void showTask(Task task) {
        mTitleEditText.setText(task.getTitle());
        mDescriptionEditText.setText(task.getDescription());
    }

    @Override
    public void showTaskNotAvailableMessage() {
        Snackbar.make(mTitleEditText, R.string.no_task_message, Snackbar.LENGTH_LONG).show();
    }
}
