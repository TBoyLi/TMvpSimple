package com.redli.tmvpsimple.base;

import android.app.Application;

import com.orhanobut.hawk.Hawk;
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
        Hawk.init(this).build();
    }

    private void setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }
}
