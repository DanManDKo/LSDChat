package com.example.lsdchat.ui.registration;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.Toast;

import com.example.lsdchat.R;
import com.example.lsdchat.ui.MainActivity;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.io.File;
import java.util.Arrays;
import java.util.regex.Pattern;

import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser;
import ru.tinkoff.decoro.slots.Slot;
import ru.tinkoff.decoro.watchers.FormatWatcher;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;


public class RegistrationPresenter implements RegistrationContract.Presenter {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int MIN_DIGITS_AND_LETTERS_VALUE = 2;
    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MAX_PASSWORD_LENGTH = 12;
    private static final String PHONE_MASK = "+38 (0__) ___-__-__";
    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private RegistrationContract.View mView;
    private Context mContext;
    private CallbackManager mCallbackManager;

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            mView.resetErrorMessages();
        }
    };

    public TextWatcher getTextWatcher() {
        return mTextWatcher;
    }

    @Override
    public void attachView(RegistrationContract.View view) {
        mView = view;
        mContext = mView.getContext();
        mCallbackManager = CallbackManager.Factory.create();
    }

    @Override
    public void detachView() {
        mView = null;
    }


    @Override
    public void initFacebookSdk() {
        FacebookSdk.sdkInitialize(mContext.getApplicationContext());
    }

    @Override
    public void loginWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions((Activity) mContext, Arrays.asList("public_profile"));
    }

    @Override
    public void getFacebookeToken() {
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String token = loginResult.getAccessToken().getToken();
                //TODO save in User Model
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    public void setTextChangedListenerWithInputMask(TextInputEditText phone) {
        Slot[] slots = new UnderscoreDigitSlotsParser().parseSlots(PHONE_MASK);
        FormatWatcher formatWatcher = new MaskFormatWatcher(MaskImpl.createTerminated(slots));
        formatWatcher.installOn(phone);
    }

    @Override
    public void navigateToMainScreen(boolean validateValue) {
        if (validateValue) {
            Intent intent = new Intent(mContext, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            mContext.startActivity(intent);
            ((Activity) mContext).finish();
        }
    }

    @Override
    public void showImageSourceChooser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getString(R.string.image_source_chooser))
                .setPositiveButton(mContext.getString(R.string.photo_gallery), (dialogInterface, i) -> {

                })
                .setNegativeButton(mContext.getString(R.string.device_camera), (dialogInterface, i) -> {
                    //check for divice camera
                    if (mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (pictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
                            ((Activity) mContext).startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE);
                        }
                    } else {
                        Toast.makeText(mContext, "Your device doesn`t have camera", Toast.LENGTH_SHORT).show();
                    }
                }).create().show();
    }

    @Override
    public boolean validateRegForm(String email, String pass, String confPass) {
        boolean test = true;
        if (!validateEmail(email)) test = false;
        if (!validatePassword(pass)) test = false;
        if (!validateConfPassword(pass, confPass)) test = false;
        return test;
    }

    @Override
    public boolean validateEmail(String email) {
        if (TextUtils.isEmpty(email) || !Pattern.compile(EMAIL_PATTERN).matcher(email).matches()) {
            mView.setInvalideEmailError();
            return false;
        }
        return true;
    }

    @Override
    public boolean validatePassword(String pass) {
        int passLength = pass.length();
        int digitCounter = 0;
        int capitalizeLetterCounter = 0;
        //length validation
        if (passLength < MIN_PASSWORD_LENGTH || passLength > MAX_PASSWORD_LENGTH) {
            mView.setLengthPasswordError();
            return false;
        }
        //strenth validation
        for (int i = 0; i < passLength; i++) {
            if (Character.isDigit(pass.charAt(i))) digitCounter++;
            if (Character.isUpperCase(pass.charAt(i))) capitalizeLetterCounter++;
        }
        if (digitCounter < MIN_DIGITS_AND_LETTERS_VALUE || capitalizeLetterCounter < MIN_DIGITS_AND_LETTERS_VALUE) {
            mView.setWeakPasswordError();
            return false;
        }
        return true;
    }

    @Override
    public boolean validateConfPassword(String pass, String confPass) {
        if (!pass.equals(confPass)) {
            mView.setEquelsPasswordError();
            return false;
        }
        return true;
    }

    public CallbackManager getCallbackManager() {
        return mCallbackManager;
    }
}
