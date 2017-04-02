package com.redli.tmvpsimple.model;

import android.content.Context;
import android.support.annotation.NonNull;

import com.redli.tmvpsimple.Constants;
import com.redli.tmvpsimple.base.ActivityLifeCycleEvent;
import com.redli.tmvpsimple.base.BaseModel;
import com.redli.tmvpsimple.bean.Subject;
import com.redli.tmvpsimple.http.Api;
import com.redli.tmvpsimple.http.HttpUtil;
import com.redli.tmvpsimple.http.ProgressSubscriber;

import java.util.List;

import rx.subjects.PublishSubject;

/**
 * Created by redli on 2017/4/1.
 */

public class CombineModel extends BaseModel {

    private Context mContext;
    private PublishSubject mLifeCycle;

    public CombineModel(Context mContext, PublishSubject mLifeCycle) {
        this.mContext = mContext;
        this.mLifeCycle = mLifeCycle;
    }

    public void getMovies(@NonNull int start, @NonNull int count, @NonNull final CombineModel
            .InfoHint infoHint) {
        if (infoHint == null)
            throw new RuntimeException("InfoHint不能为空");

        HttpUtil.getInstance().toSubscribe(Api.getDefault().getTopMovie(start, count), new
                ProgressSubscriber<List<Subject>>(mContext) {

            @Override
            protected void _onNext(List<Subject> list) {
                infoHint.successInfo(list);
            }

            @Override
            protected void _onError(String message) {
                infoHint.failInfo(message);
            }
        }, Constants.Cache_Key_Movie, ActivityLifeCycleEvent.DESTROY, mLifeCycle,
                true, false);
    }


    public interface InfoHint {
        void successInfo(List<Subject> list);

        void failInfo(String str);
    }
}
