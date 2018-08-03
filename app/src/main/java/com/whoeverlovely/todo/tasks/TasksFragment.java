package com.whoeverlovely.todo.tasks;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.whoeverlovely.todo.R;
import com.whoeverlovely.todo.addEditTask.AddEditTaskActivity;
import com.whoeverlovely.todo.data.Task;
import com.whoeverlovely.todo.taskDetails.TaskDetailsActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TasksFragment extends Fragment implements TasksContract.View {

    private TasksContract.Presenter mPresenter;
    TasksAdapter mTaskAdapter;

    @BindView(R.id.rv_tasks)
    RecyclerView mTasksRecyclerView;

    @BindView(R.id.swipeRefreshLayout_tasks)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.progressbar_loadingtasks)
    ProgressBar mProgressBar;

    public TasksFragment() {
        // Required empty public constructor
    }

    public static TasksFragment newInstance() {
        return new TasksFragment();
    }

    private TasksItemListener mTasksItemListener = new TasksItemListener() {
        @Override
        public void onTaskClick(Task clickedTask) {
            mPresenter.openTaskDetails(clickedTask);
        }

        @Override
        public void onCompletedTaskClick(Task completedTask) {
            mPresenter.completeTask(completedTask);
        }

        @Override
        public void onActivateTaskClick(Task activatedTask) {
            mPresenter.activatedTask(activatedTask);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTaskAdapter = new TasksAdapter(mTasksItemListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        ButterKnife.bind(this, view);

        mTasksRecyclerView.setAdapter(mTaskAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mTasksRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mTasksRecyclerView.getContext(),
                LinearLayout.VERTICAL);
        mTasksRecyclerView.addItemDecoration(dividerItemDecoration);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Disable the progressbar in swipefreshLayout
                mSwipeRefreshLayout.setRefreshing(false);

                mPresenter.loadTasks(true);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.result(requestCode, resultCode);
    }

    @Override
    public void setLoadingIndicator(boolean visible) {
        if (visible)
            mProgressBar.setVisibility(View.VISIBLE);
        else
            mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showTasks(List<Task> tasks) {
        mTaskAdapter.swapData(tasks);
    }

    @Override
    public void showAddTask() {
        Intent intent = new Intent(getContext(), AddEditTaskActivity.class);
        startActivityForResult(intent, AddEditTaskActivity.REQUEST_ADD_TASK);
    }

    @Override
    public void showTaskDetails(int taskId) {
        Intent intent = new Intent(getContext(), TaskDetailsActivity.class);
        intent.putExtra(TaskDetailsActivity.EXTRA_TASK_ID, taskId);
        startActivity(intent);
    }

    @Override
    public void showNoTasks() {
        Snackbar.make(mTasksRecyclerView, R.string.no_task_message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showTasksLoadingError() {
        Snackbar.make(mTasksRecyclerView, R.string.task_loading_error_message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showSuccessfulSavedMessage() {
        Snackbar.make(mTasksRecyclerView, R.string.successful_save_task_message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showTaskCompleted() {
        Snackbar.make(mTasksRecyclerView, R.string.task_completed_message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showTaskActivated() {
        Snackbar.make(mTasksRecyclerView, R.string.task_activated_message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void setPresenter(TasksContract.Presenter presenter) {
        mPresenter = presenter;
    }

    class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {
        private List<Task> mTasks;
        private TasksItemListener mTasksItemListener;

        public TasksAdapter(TasksItemListener tasksItemListener) {
            mTasksItemListener = tasksItemListener;
        }

        public void swapData(List<Task> tasks) {
            mTasks = tasks;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext())
                    .inflate(R.layout.rvitem_tasks, parent, false);
            ButterKnife.bind(this, view);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Task task = mTasks.get(position);
            holder.mTextView.setText(task.getTitle());
            holder.mCheckBox.setChecked(task.isCompleted());

            if (task.isCompleted())
                holder.itemView.setBackgroundColor(Color.LTGRAY);
            else
                holder.itemView.setBackgroundColor(getResources().getColor(android.R.color.background_light));
        }

        @Override
        public int getItemCount() {
            if (mTasks == null)
                return 0;

            return mTasks.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            CheckBox mCheckBox;
            TextView mTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                mCheckBox = itemView.findViewById(R.id.checkBox);
                mTextView = itemView.findViewById(R.id.textView);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Task task = mTasks.get(getAdapterPosition());
                        mTasksItemListener.onTaskClick(task);
                    }
                });

                mCheckBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Task task = mTasks.get(getAdapterPosition());
                        if (!task.isCompleted())
                            mTasksItemListener.onCompletedTaskClick(task);
                        else
                            mTasksItemListener.onActivateTaskClick(task);
                    }
                });
            }
        }
    }

    interface TasksItemListener {
        void onTaskClick(Task clickedTask);

        void onCompletedTaskClick(Task completedTask);

        void onActivateTaskClick(Task activatedTask);
    }
}
