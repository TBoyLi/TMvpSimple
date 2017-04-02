package com.redli.tmvpsimple.http;

import android.content.Context;

import com.redli.tmvpsimple.util.NetUtils;
import com.redli.tmvpsimple.view.SimpleLoadDialog;

import rx.Subscriber;

/**
 * Created by redli on 2017/4/1.
 */

public abstract class ProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {

    private SimpleLoadDialog dialogHandler;
    private boolean isConnected;

    public ProgressSubscriber(Context context) {
        isConnected = NetUtils.isConnected(context);
        dialogHandler = new SimpleLoadDialog(context,this,true);
    }

    @Override
    public void onCompleted() {
        dismissProgressDialog();
    }

    @Override
    public void onNext(T t) {
        _onNext(t);
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        if (isConnected) { //这里自行替换判断网络的代码
            _onError("网络不可用");
        } else if (e instanceof ApiException) {
            _onError(e.getMessage());
        } else {
            _onError("请求失败，请稍后再试...");
        }
        dismissProgressDialog();
    }

    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }


    /**
     * 显示Dialog
     */
    public void showProgressDialog(){
        if (dialogHandler != null) {
            dialogHandler.show();
        }
    }

    /**
     * 隐藏Dialog
     */
    private void dismissProgressDialog(){
        if (dialogHandler != null) {
            dialogHandler.dismiss();
            dialogHandler=null;
        }
    }


    protected abstract void _onNext(T t);
    protected abstract void _onError(String message);
}
