package com.redli.tmvpsimple.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.redli.tmvpsimple.base.BaseModel;
import com.redli.tmvpsimple.bean.MovieBean;
import com.redli.tmvpsimple.bean.MovieItemBean;
import com.redli.tmvpsimple.helper.HttpService;
import com.redli.tmvpsimple.utils.RetrofitUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by redli on 2017/3/15.
 */

public class MovieModel extends BaseModel {

    private List<MovieItemBean> mList = new ArrayList<MovieItemBean>();

    public void getMovies(@NonNull int start, @NonNull int count, @NonNull final InfoHint infoHint){
        if (infoHint == null)
            throw new RuntimeException("InfoHint不能为空");

        RetrofitUtil.getInstance().retrofit.create(HttpService.MovieService.class)
                .getTopMovie(start, count)
                .flatMap(new Func1<MovieBean, Observable<MovieBean.SubjectsBean>>() {
                    @Override
                    public Observable<MovieBean.SubjectsBean> call(MovieBean movieBean) {
                        return Observable.from(movieBean.getSubjects());
                    }
                }).map(new Func1<MovieBean.SubjectsBean, MovieItemBean>() {
                    @Override
                    public MovieItemBean call(MovieBean.SubjectsBean subjectsBean) {
                        MovieItemBean movieItemBean = new MovieItemBean();
                        movieItemBean.setTitle(subjectsBean.getTitle());
                        movieItemBean.setImageUrl(subjectsBean.getImages().getMedium());
                        return movieItemBean;
                    }
                })
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MovieItemBean>() {
                    @Override
                    public void onCompleted() {
                        Log.i("MovieModel", "获取电影成功");
                        infoHint.successInfo(mList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        infoHint.failInfo(e.getMessage());
                    }

                    @Override
                    public void onNext(MovieItemBean movieItemBean) {
                        mList.add(movieItemBean);
                    }
                });
    }

    public interface InfoHint{
        void successInfo(List<MovieItemBean> list);

        void failInfo(String str);
    }

}
