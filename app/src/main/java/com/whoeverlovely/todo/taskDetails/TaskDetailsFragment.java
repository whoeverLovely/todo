package com.whoeverlovely.todo.taskDetails;

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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.whoeverlovely.todo.R;
import com.whoeverlovely.todo.addEditTask.AddEditTaskActivity;
import com.whoeverlovely.todo.data.Task;
import com.whoeverlovely.todo.tasks.TasksContract;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class TaskDetailsFragment extends Fragment implements TaskDetailsContract.View {

    public static final String BUNDLE_TASK_ID = "task_id";

    @BindView(R.id.checkBox_taskDetails)
    CheckBox mTaskTitle;

    @BindView(R.id.textView_taskDescription_taskDetails)
    TextView mTaskDescription;

    @BindView(R.id.fab_edit)
    FloatingActionButton mEditFab;

    TaskDetailsContract.Presenter mPresenter;

    public TaskDetailsFragment() {
        // Required empty public constructor
    }

    public static TaskDetailsFragment newInstance(int taskId) {
        TaskDetailsFragment fragment = new TaskDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_TASK_ID, taskId);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_task_details, container, false);
        ButterKnife.bind(this, view);

        mEditFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.editTask();
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
    public void showTaskDetails(Task task) {
        mTaskTitle.setText(task.getTitle());
        mTaskDescription.setText(task.getDescription());
    }

    @Override
    public void showCompletionStatus(final Task task) {
        Timber.d("showCompletionStatus isCompleted" + task.isCompleted());
        mTaskTitle.setChecked(task.isCompleted());
        mTaskTitle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    mPresenter.completeTask(task);
                else
                    mPresenter.activateTask(task);
            }
        });
    }

    @Override
    public void showTaskCompletedMessage() {
        Snackbar.make(mTaskTitle, R.string.task_completed_message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showTaskActivatedMessage() {
        Snackbar.make(mTaskTitle, R.string.task_activated_message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showTaskNotAvailableMessage() {
        Snackbar.make(mTaskTitle, R.string.task_not_available, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void setPresenter(TaskDetailsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showAddEditTask(int taskId) {
        Intent intent = new Intent(getActivity(), AddEditTaskActivity.class);
        intent.putExtra(AddEditTaskActivity.EXTRA_TASK_ID, taskId);
        startActivityForResult(intent, AddEditTaskActivity.REQUEST_ADD_TASK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AddEditTaskActivity.REQUEST_ADD_TASK) {
            if (resultCode == Activity.RESULT_OK)
                getActivity().finish();
        }
    }
}
