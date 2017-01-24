package com.example.lsdchat.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lsdchat.R;
import com.example.lsdchat.manager.ApiManager;
import com.example.lsdchat.ui.MainActivity;

public class LoginActivity extends AppCompatActivity implements LoginContract.View {
    private ProgressBar mProgressBar;
    private EditText mEmail;
    private EditText mPassword;
    private LoginContract.Presented mPresented;
    private Button mBtnSignIn;
    private Button mBtnSignUp;
    private TextView mBtnForgotPassword;
    private CheckBox mKeepMeSignIn;
    private TextInputLayout mIlEmail;
    private TextInputLayout mIlPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mPresented = new LoginPresented(this);

//        init quickblox
        ApiManager apiManager = new ApiManager();
        apiManager.initializeQb(this);

        initView();

        setLoginButtonEnabled(false);
        textChange();


        onClickButton();


    }

    private void onClickButton() {
        mBtnSignIn.setOnClickListener(view -> mPresented.validateCredentials(mEmail.getText().toString(),
                mPassword.getText().toString()));

        mBtnSignUp.setOnClickListener(view -> navigateToRegistration());

        mBtnForgotPassword.setOnClickListener(view -> navigateToForgotPassword());
    }


    private void textChange() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setLoginButtonEnabled(validationData(mEmail.getText().toString(), mPassword.getText().toString()));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        mEmail.addTextChangedListener(textWatcher);
        mPassword.addTextChangedListener(textWatcher);
    }

    private boolean validationData(String email, String password) {
        return isValidEmail(email) && isValidPassword(password);
    }

    private void initView() {
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mEmail = (EditText) findViewById(R.id.input_email);
        mPassword = (EditText) findViewById(R.id.input_password);
        mBtnSignIn = (Button) findViewById(R.id.btn_sign_in);
        mBtnSignUp = (Button) findViewById(R.id.btn_sign_up);
        mBtnForgotPassword = (TextView) findViewById(R.id.btn_forgot_password);
        mKeepMeSignIn = (CheckBox) findViewById(R.id.cb_keep_me_signed_in);
        mIlEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        mIlPassword = (TextInputLayout) findViewById(R.id.input_layout_password);

    }

    @Override
    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void setEmailError() {
        mIlEmail.setError(getString(R.string.error_input_email));
    }

    @Override
    public void setPasswordError() {
        mIlPassword.setError(getString(R.string.error_input_password));
    }

    @Override
    public void hideEmailError() {
        mIlEmail.setError(null);
    }

    @Override
    public void hidePasswordError() {
        mIlPassword.setError(null);
    }

    @Override
    public void setLoginButtonEnabled(boolean enabled) {
        if (enabled) {
            mBtnSignIn.setClickable(true);
        } else {
            mBtnSignIn.setClickable(false);
        }
    }


    @Override
    public void navigateToRegistration() {
//        TODO: start activity registration
    }

    @Override
    public void navigateToForgotPassword() {
//        TODO: start activity forgotpassword

    }

    @Override
    public void navigateToMainScreen() {
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public boolean isKeepSignIn() {
        return mKeepMeSignIn.isChecked();
    }

    @Override
    public boolean isValidPassword(String password) {
        if (!TextUtils.isEmpty(password) && !(password.length() < 8)) {
            hidePasswordError();
            return true;
        } else {
            setPasswordError();
            return false;
        }
    }

    @Override
    public boolean isValidEmail(String email) {
        if (!TextUtils.isEmpty(email) && email.contains("@")) {
            hideEmailError();
            return true;
        } else {
            setEmailError();
            return false;
        }
    }

    @Override
    public Context getContext() {
        return null;
    }


}
