package com.redli.tmvpsimple.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.redli.tmvpsimple.mvp.IView;

import java.lang.reflect.ParameterizedType;

import rx.subjects.PublishSubject;

public abstract class BaseActivity<V extends IView,T extends BasePresenter<V>> extends AppCompatActivity implements IView {
    public T mPresenter;

    /**
     *  基本的网络请求都是向服务器请求数据，客户端拿到数据后更新UI。但也不排除意外情况，比如请求回数据途中Activity已经不在了，这个时候就应该取消网络请求。
     *  要实现上面的功能其实很简单，两部分
     *
     *  随时监听Activity(Fragment)的生命周期并对外发射出去； 在我们的网络请求中，接收生命周期
     *  并进行判断，如果该生命周期是自己绑定的，如Destory，那么就断开数据向下传递的过程
     *  实现以上功能需要用到Rxjava的Subject的子类PublishSubject
     *
     */

    public final PublishSubject<ActivityLifeCycleEvent> lifecycleSubject = PublishSubject.create();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.CREATE);
        super.onCreate(savedInstanceState);
        mPresenter= getInstance(this,1);
        mPresenter.attachView((V) this);
    }

    @Override
    protected void onStart() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.START);
        super.onStart();
    }

    @Override
    protected void onResume() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.RESUME);
        super.onResume();
    }

    @Override
    protected void onPause() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.PAUSE);
        super.onPause();
    }

    @Override
    protected void onStop() {
        lifecycleSubject.onNext(ActivityLifeCycleEvent.STOP);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lifecycleSubject.onNext(ActivityLifeCycleEvent.DESTROY);
        if (mPresenter!=null)
        mPresenter.detachView();
    }

    @Override
    public Context getContext(){
        return this;
    }

    @Override
    public PublishSubject getLifeCycle() {
        return lifecycleSubject;
    }

    public  <T> T getInstance(Object o, int i) {
        try {
            return ((Class<T>) ((ParameterizedType) (o.getClass()
                    .getGenericSuperclass())).getActualTypeArguments()[i])
                    .newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }
}
