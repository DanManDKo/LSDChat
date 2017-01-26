package com.example.lsdchat.ui.splash;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lsdchat.R;

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
        initViews();
        mPresenter = new SplashScreenPresenter();
        mPresenter.attachView(this);
        mPresenter.leaveSplashScreen();


    }
    private void initViews(){
        mTextView = (TextView) findViewById(R.id.name);
        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/FortuneCity.ttf");
        mTextView.setTypeface(typeface);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void navigateToLogin() {
        Toast.makeText(this,"Navigate to login",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToMain() {
        Toast.makeText(this,"Navigate to main",Toast.LENGTH_SHORT).show();

    }
}

