package com.redli.tmvpsimple.mvp;

import android.content.Context;
import rx.subjects.PublishSubject;

/**
 * Created by redli on 2017/3/13.
 */

public interface IView {

     /**
      * 获取上下文（当前的view）
      * @return
      */
     Context getContext();

     /**
      * 获取PublishSubject方便生命结束管控请求
      * @return
      */
     PublishSubject getLifeCycle();
}
