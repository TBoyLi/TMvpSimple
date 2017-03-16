package com.redli.tmvpsimple.contract;

import com.redli.tmvpsimple.bean.MovieBean;
import com.redli.tmvpsimple.bean.MovieItemBean;
import com.redli.tmvpsimple.mvp.IView;

import java.util.List;

/**
 * Created by redli on 2017/3/15.
 */

public class MovieContract {
    public interface View extends IView {
        void getMovieSuccess(List<MovieItemBean> list);
        void getMovieFail(String str);
    }

    public interface Presenter {
        void getMovie(int start, int count);
    }
}
