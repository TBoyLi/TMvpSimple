package com.redli.tmvpsimple.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.redli.tmvpsimple.R;
import com.redli.tmvpsimple.base.BaseActivity;
import com.redli.tmvpsimple.contract.LoginContract;
import com.redli.tmvpsimple.presenter.LoginPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by redli on 2017/3/13.
 */

public class LoginActivity1 extends BaseActivity<LoginContract.View, LoginPresenter> implements
        LoginContract.View, View.OnClickListener {



    @BindView(R.id.email)
    AutoCompleteTextView email;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.email_sign_in_button)
    Button emailSignInButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        emailSignInButton.setOnClickListener(this);
    }


    @Override
    public String getName() {
        return email.getText().toString();
    }

    @Override
    public String getPassWord() {
        return password.getText().toString();
    }

    @Override
    public void getNameNull(String str) {
        email.setError(str);
    }

    @Override
    public void getPassWordNull(String str) {
        password.setError(str);
    }

    @Override
    public void loginSuccess(String str) {
        Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void loginFail(String failMsg) {
        Toast.makeText(getContext(), failMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        mPresenter.login(getName(), getPassWord());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
