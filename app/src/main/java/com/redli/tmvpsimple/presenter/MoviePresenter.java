package com.redli.tmvpsimple.presenter;

import com.redli.tmvpsimple.base.BasePresenter;
import com.redli.tmvpsimple.bean.MovieBean;
import com.redli.tmvpsimple.bean.MovieItemBean;
import com.redli.tmvpsimple.contract.MovieContract;
import com.redli.tmvpsimple.model.MovieModel;

import java.util.List;

/**
 * Created by redli on 2017/3/15.
 */

public class MoviePresenter extends BasePresenter<MovieContract.View> implements MovieContract.Presenter{

    @Override
    public void getMovie(int start, int count) {
        new MovieModel().getMovies(start, count, new MovieModel.InfoHint() {
            @Override
            public void successInfo(List<MovieItemBean> list) {
                mView.getMovieSuccess(list);
            }

            @Override
            public void failInfo(String str) {
                mView.getMovieFail(str);
            }
        });
    }
}
