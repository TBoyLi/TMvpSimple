package com.redli.tmvpsimple.contract;

import com.redli.tmvpsimple.bean.Subject;
import com.redli.tmvpsimple.mvp.IView;

import java.util.List;

/**
 * Created by redli on 2017/4/1.
 */

public class CombineContract {
    public interface View extends IView {
        void getMovieSuccess(List<Subject> list);
        void getMovieFail(String str);
    }

    public interface Presenter {
        void getMovie(int start, int count);
    }
}
