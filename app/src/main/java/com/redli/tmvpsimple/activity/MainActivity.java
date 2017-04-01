package com.redli.tmvpsimple.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.redli.tmvpsimple.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @OnClick(R.id.login) void jumpLogin() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    @OnClick(R.id.movie) void jumpMovie() {
        startActivity(new Intent(this, MovieActivity.class));
    }

    @OnClick(R.id.combine) void jumpCombine(){ startActivity(new Intent(this, CombineActivity.class));}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

}
