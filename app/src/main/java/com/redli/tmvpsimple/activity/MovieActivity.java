package com.redli.tmvpsimple.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.redli.tmvpsimple.MovieAdapter;
import com.redli.tmvpsimple.R;
import com.redli.tmvpsimple.base.BaseActivity;
import com.redli.tmvpsimple.bean.MovieBean;
import com.redli.tmvpsimple.bean.MovieItemBean;
import com.redli.tmvpsimple.contract.MovieContract;
import com.redli.tmvpsimple.presenter.MoviePresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by redli on 2017/3/15.
 */

public class MovieActivity extends BaseActivity<MovieContract.View, MoviePresenter> implements
        MovieContract.View {


    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private MovieAdapter movieAdapter;
//    private List<MovieItemBean> mList;
    private boolean isRefresh = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);
        ButterKnife.bind(this);
        initLayout();
        initData();
    }

    private void initData() {
        movieAdapter = new MovieAdapter(this);
        mPresenter.getMovie(0, 10);
    }

    private void initLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                mPresenter.getMovie(10, 10);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));
    }

    @Override
    public void getMovieSuccess(List<MovieItemBean> list) {
        mSwipeRefreshLayout.setRefreshing(false);
        movieAdapter.setmList(list);
        if (isRefresh){
            movieAdapter.notifyDataSetChanged();
        }else {
            mRecyclerView.setAdapter(movieAdapter);
        }
    }

    @Override
    public void getMovieFail(String str) {
        mSwipeRefreshLayout.setRefreshing(false);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
