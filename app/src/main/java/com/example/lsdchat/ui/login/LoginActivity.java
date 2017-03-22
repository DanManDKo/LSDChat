package com.example.lsdchat.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.lsdchat.App;
import com.example.lsdchat.R;
import com.example.lsdchat.ui.forgot_password.ForgotPasswordFragment;
import com.example.lsdchat.ui.main.MainActivity;
import com.example.lsdchat.ui.registration.RegistrationActivity;
import com.example.lsdchat.util.DialogUtil;
import com.example.lsdchat.util.ErrorsCode;
import com.example.lsdchat.util.Network;
import com.example.lsdchat.util.UsersUtil;
import com.example.lsdchat.util.error.ErrorInterface;
import com.example.lsdchat.util.error.NetworkConnect;

import retrofit2.adapter.rxjava.HttpException;

public class LoginActivity extends AppCompatActivity implements LoginContract.View,NetworkConnect,ErrorInterface {

    private static final String FORGOT_PASSWORD_DIALOG = "forgot";
    private ProgressBar mProgressBar;
    private EditText mEmail;
    private EditText mPassword;
    private LoginContract.Presenter mPresenter;
    private Button mBtnSignIn;
    private Button mBtnSignUp;
    private TextView mBtnForgotPassword;
    private CheckBox mKeepMeSignIn;
    private TextInputLayout mIlEmail;
    private TextInputLayout mIlPassword;

    private ForgotPasswordFragment mForgotPasswordFragment;

    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mPresenter = new LoginPresenter(this, App.getDataManager(),App.getSharedPreferencesManager(this));
        initView();

//        set button disable
        setLoginButtonEnabled(false);


//        validate data
        mPresenter.validateCredentials(mEmail, mPassword);

        onClickButton();


    }


    private void onClickButton() {
        mPresenter.btnSignInClick(mBtnSignIn,mEmail, mPassword);
        mPresenter.btnSignUpClick(mBtnSignUp);
        mPresenter.btnSignForgotPasswordClick(mBtnForgotPassword);
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

        mForgotPasswordFragment = new ForgotPasswordFragment();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

        }
        getSupportActionBar().setTitle(R.string.login_sign_in);

    }
    @Override
    public void dialogError(Throwable throwable) {
        String title = "Error " + String.valueOf(((HttpException) throwable).code());
        String message = ErrorsCode.getErrorMessage(this, throwable);

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss())
                .setCancelable(false)
                .create().show();
    }

    @Override
    public void onBackPressed() {
        mPresenter.startService(this);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    public Context getContext() {
        return this;
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
        mIlEmail.setError(getString(R.string.login_error_email));

    }

    @Override
    public void setPasswordError() {
        mIlPassword.setError(getString(R.string.login_error_password));
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
        startActivity(new Intent(this, RegistrationActivity.class));
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
    }



    @Override
    public void navigateToMainScreen() {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
        finish();
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
    }

    @Override
    public boolean isKeepSignIn() {
        return mKeepMeSignIn.isChecked();
    }

    @Override
    public void showDialogForgotPassword() {
        mForgotPasswordFragment.show(getFragmentManager(), FORGOT_PASSWORD_DIALOG);
    }

    @Override
    public void getDialogAndUser(String token) {
        UsersUtil.getUserListAndSave(token, this);
        DialogUtil.getAllDialogAndSave(token,this);
    }

    @Override
    public void showErrorDialog(Throwable throwable) {
        String title = "Error " + String.valueOf(((HttpException) throwable).code());
        String message = ErrorsCode.getErrorMessage(App.getContext(), throwable);

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss())
                .setCancelable(false)
                .create().show();
    }

    @Override
    public boolean isNetworkConnect() {
        if (!Network.isOnline(this)) {
            Network.showErrorConnectDialog(this);
            return false;
        } else {
            return true;
        }
    }
}
