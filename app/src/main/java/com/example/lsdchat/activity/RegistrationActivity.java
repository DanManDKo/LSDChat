package com.example.lsdchat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;

import com.example.lsdchat.R;
import com.example.lsdchat.contract.RegistrationContract;
import com.example.lsdchat.presenter.RegistrationPresenter;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

import static com.facebook.FacebookSdk.getApplicationContext;


public class RegistrationActivity extends AppCompatActivity implements RegistrationContract.View {
    private TextInputLayout mEmail;
    private TextInputLayout mPass;
    private TextInputLayout mConfPass;
    private TextInputLayout mName;
    private TextInputLayout mPhone;
    private TextInputLayout mWeb;
    private Button mFbSignUpButton;
    private Toolbar mToolbar;
    private RegistrationPresenter mRegistrationPresenter = new RegistrationPresenter();
    private CallbackManager mCallbackManager;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_registration);

        createUI();
        setRegFormHint();

        setSupportActionBar(mToolbar);
        setToolbar();

        mRegistrationPresenter.attachView(this);

        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String fbToken = loginResult.getAccessToken().getToken();
                //TODO do refactoring
                //генерация сигнатуры, которая переходит в создание сессии
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        mFbSignUpButton.setOnClickListener(v -> LoginManager.getInstance().logInWithReadPermissions(RegistrationActivity.this, Arrays.asList("public_profile")));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRegistrationPresenter.detachView();
    }

    @Override
    public Context getContext() {
        return this;
    }

    private void createUI() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_reg);
        mEmail = (TextInputLayout) findViewById(R.id.til_email_reg);
        mPass = (TextInputLayout) findViewById(R.id.til_pass_reg);
        mConfPass = (TextInputLayout) findViewById(R.id.til_confpass_reg);
        mName = (TextInputLayout) findViewById(R.id.til_name_reg);
        mPhone = (TextInputLayout) findViewById(R.id.til_phone_reg);
        mWeb = (TextInputLayout) findViewById(R.id.til_web_reg);
        mFbSignUpButton = (Button) findViewById(R.id.fb_button_reg);
    }

    public void setRegFormHint() {
        mEmail.setHint(getString(R.string.email_reg));
        mPass.setHint(getString(R.string.password_reg));
        mConfPass.setHint(getString(R.string.conf_pass_reg));
        mName.setHint(getString(R.string.full_name_reg));
        mPhone.setHint(getString(R.string.phone_reg));
        mWeb.setHint(getString(R.string.website_reg));
    }

    private void setToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }
}
