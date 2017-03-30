package com.redli.tmvpsimple.base;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by redli on 2017/3/17.
 */

public class BaseApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        //配置LeakCanary
        setupLeakCanary();
    }

    private void setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }
}
