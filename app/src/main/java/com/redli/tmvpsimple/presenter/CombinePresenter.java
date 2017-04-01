package com.redli.tmvpsimple.presenter;

import com.redli.tmvpsimple.base.BasePresenter;
import com.redli.tmvpsimple.bean.Subject;
import com.redli.tmvpsimple.contract.CombineContract;
import com.redli.tmvpsimple.model.CombineModel;

import java.util.List;

/**
 * Created by redli on 2017/4/1.
 */

public class CombinePresenter extends BasePresenter<CombineContract.View> implements CombineContract.Presenter{


    @Override
    public void getMovie(int start, int count) {

        new CombineModel(mView.getContext(), mView.getLifeCycle()).getMovies(start, count, new CombineModel.InfoHint() {
            @Override
            public void successInfo(List<Subject> list) {
                mView.getMovieSuccess(list);
            }

            @Override
            public void failInfo(String str) {
                mView.getMovieFail(str);
            }
        });
    }
}
