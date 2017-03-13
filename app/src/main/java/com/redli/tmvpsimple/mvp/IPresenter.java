package com.redli.tmvpsimple.mvp;

/**
 * Created by redli on 2017/3/13.
 */

public interface IPresenter<V extends IView>{
    /**
     * @param view 绑定
     */
    void attachView(V view);


    /**
     * 防止内存的泄漏,清楚presenter与activity之间的绑定
     */
    void detachView();
}
