package com.redli.tmvpsimple.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;
import android.widget.Toast;

import com.redli.tmvpsimple.R;
import com.redli.tmvpsimple.base.BaseActivity;
import com.redli.tmvpsimple.bean.Subject;
import com.redli.tmvpsimple.contract.CombineContract;
import com.redli.tmvpsimple.presenter.CombinePresenter;
import com.redli.tmvpsimple.util.LogUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by redli on 2017/3/31.
 */

public class CombineActivity extends BaseActivity<CombineContract.View, CombinePresenter> implements CombineContract.View{

    @BindView(R.id.content)
    TextView content;

    @OnClick(R.id.get_data) void getData(){
        mPresenter.getMovie(30, 30);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combine);
        ButterKnife.bind(this);
    }

    @Override
    public void getMovieSuccess(List<Subject> list) {
        LogUtils.e(list);
        String str = "";
        for (int i = 0; i < list.size(); i++) {
            str += "电影名：" + list.get(i).getTitle() + "\n";
        }
        content.setText(str);
    }

    @Override
    public void getMovieFail(String message) {
        Toast.makeText(CombineActivity.this, message, Toast.LENGTH_LONG).show();
    }
}
