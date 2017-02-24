package com.example.lsdchat.ui.splash;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.lsdchat.App;
import com.example.lsdchat.R;
import com.example.lsdchat.ui.login.LoginActivity;
import com.example.lsdchat.ui.main.MainActivity;

/**
 * Created by User on 18.01.2017.
 */

public class SplashScreenActivity extends AppCompatActivity implements SplashContract.View {
    private SplashScreenPresenter mPresenter;
    private TextView mTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh);
        initView();
        mPresenter = new SplashScreenPresenter(this, App.getSharedPreferencesManager(this));
        mPresenter.leaveSplashScreen();
    }

    private void initView() {
        mTextView = (TextView) findViewById(R.id.name);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/FortuneCity.ttf");
        mTextView.setTypeface(typeface);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void navigateToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
        finish();
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
    }

    @Override
    public void navigateToMain() {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
        finish();
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
    }
}

