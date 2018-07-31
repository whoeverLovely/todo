package com.whoeverlovely.todo.statistics;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whoeverlovely.todo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StatisticsFragment extends Fragment implements StatisticsContract.View{
    @BindView(R.id.textView_statistics)
    TextView mStatistics;

    StatisticsContract.Presenter mPresenter;

    public StatisticsFragment() {
        // Required empty public constructor
    }

    public static StatisticsFragment newInstance() {
        return new StatisticsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(StatisticsContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showStatistics(int completedTask, int activatedTask) {
        mStatistics.setText("Completed Task: " + completedTask + "\n Activated Task: " + activatedTask );
    }

    @Override
    public void showNoStatisticsMessage() {
        Snackbar.make(mStatistics, R.string.no_statistics_message, Snackbar.LENGTH_LONG).show();
    }
}
