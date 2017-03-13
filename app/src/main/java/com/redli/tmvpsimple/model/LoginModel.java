package com.redli.tmvpsimple.model;

import android.support.annotation.NonNull;

import com.redli.tmvpsimple.base.BaseModel;
import com.redli.tmvpsimple.contract.LoginContract;

/**
 * Created by redli on 2017/3/13.
 */

public class LoginModel extends BaseModel {


    public void login(@NonNull String userName, @NonNull String password, @NonNull final InfoHint
            infoHint) {
        if (infoHint == null)
            throw new RuntimeException("InfoHint不能为空");
        if (userName.equals("redli") && password.equals("123456")){
            infoHint.successInfo("user info");
        }else{
            infoHint.failInfo("no user info");
        }
    }

    //通过接口产生信息回调
    public interface InfoHint {
        void successInfo(String str);

        void failInfo(String str);
    }
}
