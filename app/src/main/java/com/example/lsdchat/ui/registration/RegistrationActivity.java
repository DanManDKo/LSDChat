package com.example.lsdchat.ui.registration;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.lsdchat.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class RegistrationActivity extends AppCompatActivity implements RegistrationContract.View {
    private static final int REQUEST_IMAGE_CAMERA = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;

    private TextInputLayout mEmail;
    private TextInputLayout mPass;
    private TextInputLayout mConfPass;
    private TextInputLayout mName;
    private TextInputLayout mPhone;
    private TextInputLayout mWeb;

    private TextInputEditText mEmailEdit;
    private TextInputEditText mPassEdit;
    private TextInputEditText mConfPassEdit;
    private TextInputEditText mPhoneEdit;

    private Button mFbSignUpButton;
    private Button mSignUpButton;
    private SimpleDraweeView mImageView;
    private ProgressBar mProgressBar;
    private Toolbar mToolbar;
    private RegistrationPresenter mRegistrationPresenter = new RegistrationPresenter();

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRegistrationPresenter.attachView(this);
        mRegistrationPresenter.initFacebookSdk();

        setContentView(R.layout.activity_registration);

        createUI();
        setRegFormHint();

        setSupportActionBar(mToolbar);
        configurateToolbar();

        mRegistrationPresenter.getFacebookToken();

        mEmailEdit.addTextChangedListener(mRegistrationPresenter.getTextWatcher());
        mPassEdit.addTextChangedListener(mRegistrationPresenter.getTextWatcher());
        mConfPassEdit.addTextChangedListener(mRegistrationPresenter.getTextWatcher());
        mRegistrationPresenter.setTextChangedListenerWithInputMask(mPhoneEdit);

        mImageView.setOnClickListener(v -> mRegistrationPresenter.showDialogImageSourceChooser());

        mFbSignUpButton.setOnClickListener(v -> {
            mRegistrationPresenter.loginWithFacebook();

            mFbSignUpButton.setText(getString(R.string.fb_button_text_linked));
            mFbSignUpButton.setEnabled(false);
            mFbSignUpButton.setClickable(false);
        });

        mSignUpButton.setOnClickListener(v -> {
            boolean validateValue = mRegistrationPresenter.validateRegForm(
                    String.valueOf(mEmailEdit.getText()),
                    String.valueOf(mPassEdit.getText()),
                    String.valueOf(mConfPassEdit.getText()));

            mRegistrationPresenter.navigateToMainScreen(validateValue);
        });
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
        mSignUpButton = (Button) findViewById(R.id.sign_up_button);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar_reg);
        mImageView = (SimpleDraweeView) findViewById(R.id.iv_user_reg);

        mEmailEdit = (TextInputEditText) findViewById(R.id.tiet_email_reg);
        mPassEdit = (TextInputEditText) findViewById(R.id.tiet_pass_reg);
        mConfPassEdit = (TextInputEditText) findViewById(R.id.tiet_confpass_reg);
        mPhoneEdit = (TextInputEditText) findViewById(R.id.tiet_phone_reg);
    }

    public void setRegFormHint() {
        mEmail.setHint(getString(R.string.email_reg));
        mPass.setHint(getString(R.string.password_reg));
        mConfPass.setHint(getString(R.string.conf_pass_reg));
        mName.setHint(getString(R.string.full_name_reg));
        mPhone.setHint(getString(R.string.phone_reg));
        mWeb.setHint(getString(R.string.website_reg));
    }

    private void configurateToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public void getUserpicUri(Uri uri) {
        mImageView.setImageURI(uri);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mRegistrationPresenter.onActivityResult(requestCode, resultCode, data);
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void setInvalideEmailError() {
        mEmail.setError(getString(R.string.invalid_email));
    }

    @Override
    public void setWeakPasswordError() {
        mPass.setError(getString(R.string.password_is_weak));
    }

    @Override
    public void setLengthPasswordError() {
        mPass.setError(getString(R.string.password_length));
    }

    @Override
    public void setEquelsPasswordError() {
        mConfPass.setError(getString(R.string.passwords_donot_match));
    }

    @Override
    public void resetErrorMessages() {
        mEmail.setError(null);
        mPass.setError(null);
        mConfPass.setError(null);
    }

    @Override
    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.GONE);
    }
}
