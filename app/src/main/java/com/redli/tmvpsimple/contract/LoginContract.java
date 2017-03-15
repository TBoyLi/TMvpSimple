package com.redli.tmvpsimple.contract;

import com.redli.tmvpsimple.mvp.IView;

/**
 * Created by redli on 2017/3/13.
 */

 public class LoginContract {
    public interface View extends IView {

        String getName();

        String getPassWord();

        void getNameNull(String str);

        void getPassWordNull(String str);

        void loginSuccess(String str);

        void loginFail(String failMsg);
    }

    public interface Presenter {

        void login(String name, String pwd);
    }
}
