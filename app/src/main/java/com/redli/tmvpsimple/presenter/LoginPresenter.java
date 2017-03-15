package com.redli.tmvpsimple.presenter;

import android.text.TextUtils;

import com.redli.tmvpsimple.base.BasePresenter;
import com.redli.tmvpsimple.contract.LoginContract;
import com.redli.tmvpsimple.model.LoginModel;


public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter{

    @Override
    public void login(String name, String pwd) {
        if (checkNull(name, pwd))
            return;
        new LoginModel().login(name, pwd, new LoginModel.InfoHint() {
            @Override
            public void successInfo(String str) {
                mView.loginSuccess(str);
            }

            @Override
            public void failInfo(String str) {
                mView.loginFail(str);
            }
        });
    }

    public boolean checkNull(String name, String pwd) {
        boolean isNull = false;
        if (TextUtils.isEmpty(name)) {
            mView.getNameNull("用户名不能为空");
            isNull = true;
        } else if (TextUtils.isEmpty(pwd)) {
            mView.getPassWordNull("密码不能为空");
            isNull = true;
        }
        return isNull;
    }
}
