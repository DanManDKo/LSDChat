package com.example.lsdchat.ui.registration;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.example.lsdchat.R;
import com.example.lsdchat.ui.main.dialogs.DialogsFragment;
import com.facebook.FacebookSdk;
import com.facebook.drawee.view.SimpleDraweeView;
import com.redmadrobot.inputmask.MaskedTextChangedListener;

import org.jetbrains.annotations.NotNull;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class RegistrationActivity extends AppCompatActivity implements RegistrationContract.View {

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
    private TextInputEditText mNameEdit;
    private TextInputEditText mWebEdit;

    private Button mFacebookButton;
    private Button mSignUpButton;
    private SimpleDraweeView mImageView;
    private ProgressBar mProgressBar;
    private Toolbar mToolbar;
    private RegistrationPresenter mRegistrationPresenter;

    private static final String PHONE_MASK = "+38 (0[00]) [000]-[00]-[00]";
    private static final String PHONE_FORMAT = "+380";
    private static final int PHONE_LENGTH = 9;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_registration);

        mRegistrationPresenter = new RegistrationPresenter(this);
        initView();

        setRegFormHint();

        setSupportActionBar(mToolbar);
        configurateToolbar();

        mEmailEdit.addTextChangedListener(mRegistrationPresenter.getTextWatcher());
        mPassEdit.addTextChangedListener(mRegistrationPresenter.getTextWatcher());
        mConfPassEdit.addTextChangedListener(mRegistrationPresenter.getTextWatcher());
        mNameEdit.addTextChangedListener(mRegistrationPresenter.getTextWatcher());

        MaskedTextChangedListener listener = new MaskedTextChangedListener(PHONE_MASK, true, mPhoneEdit, null,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onExtracted(@NotNull String s) {
                        if (s.length() == PHONE_LENGTH) mRegistrationPresenter.setPhoneNumber(PHONE_FORMAT + s);
                    }

                    @Override
                    public void onMandatoryCharactersFilled(boolean b) {
                    }
                }
        );
        mPhoneEdit.addTextChangedListener(listener);
        mPhoneEdit.setOnFocusChangeListener(listener);

        mImageView.setOnClickListener(view -> mRegistrationPresenter.onAvatarClickListener());
        mFacebookButton.setOnClickListener(view -> mRegistrationPresenter.onFacebookButtonClickListener());
        mSignUpButton.setOnClickListener(view -> mRegistrationPresenter.onSignupButtonClickListener(
                mEmailEdit.getText().toString(),
                mPassEdit.getText().toString(),
                mConfPassEdit.getText().toString(),
                mNameEdit.getText().toString(),
                mWebEdit.getText().toString()));
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_reg);
        mEmail = (TextInputLayout) findViewById(R.id.til_email_reg);
        mPass = (TextInputLayout) findViewById(R.id.til_pass_reg);
        mConfPass = (TextInputLayout) findViewById(R.id.til_confpass_reg);
        mName = (TextInputLayout) findViewById(R.id.til_name_reg);
        mPhone = (TextInputLayout) findViewById(R.id.til_phone_reg);
        mWeb = (TextInputLayout) findViewById(R.id.til_web_reg);

        mFacebookButton = (Button) findViewById(R.id.fb_button_reg);
        mSignUpButton = (Button) findViewById(R.id.sign_up_button);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar_reg);
        mImageView = (SimpleDraweeView) findViewById(R.id.iv_user_reg);

        mEmailEdit = (TextInputEditText) findViewById(R.id.tiet_email_reg);
        mPassEdit = (TextInputEditText) findViewById(R.id.tiet_pass_reg);
        mConfPassEdit = (TextInputEditText) findViewById(R.id.tiet_confpass_reg);
        mPhoneEdit = (TextInputEditText) findViewById(R.id.tiet_phone_reg);
        mNameEdit = (TextInputEditText) findViewById(R.id.tiet_name_reg);
        mWebEdit = (TextInputEditText) findViewById(R.id.tiet_web_reg);
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
    public void navigateToMainScreen() {
        Intent intent = new Intent(this, DialogsFragment.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
        finish();
        overridePendingTransition(R.anim.push_down_in, R.anim.push_down_out);
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
        mRegistrationPresenter.onDestroy();
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
    public void setFullNameError() {
        mName.setError(getString(R.string.name_cannot_be_empty));
    }

    @Override
    public void setClickableSignupButton(boolean value) {
        mSignUpButton.setClickable(value);
    }

    @Override
    public void setLinkedStatus() {
        mFacebookButton.setText(getString(R.string.fb_button_text_linked));
    }

    @Override
    public void setClickableFacebookButton(boolean value) {
        mFacebookButton.setClickable(value);
    }

    @Override
    public void resetErrorMessages() {
        mEmail.setError(null);
        mPass.setError(null);
        mConfPass.setError(null);
        mName.setError(null);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showResponseDialogError(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(getString(R.string.alert_ok), (dialogInterface, i) -> dialogInterface.dismiss())
                .setCancelable(false)
                .create()
                .show();
    }

    @Override
    public void showNetworkErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.no_internet_connection))
                .setPositiveButton(getString(R.string.alert_ok), (dialogInterface, i) -> dialogInterface.dismiss())
                .setCancelable(false)
                .create()
                .show();
    }

    @Override
    public void showDialogImageSourceChooser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.add_photo))
                .setPositiveButton(getString(R.string.photo_gallery), (dialogInterface, i) -> {
                    mRegistrationPresenter.getPhotoFromGallery();
                })
                .setNegativeButton(getString(R.string.device_camera), (dialogInterface, i) -> {
                    mRegistrationPresenter.getPhotoFromCamera();
                });
        builder.create().show();
    }

    @Override
    public void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }
}
