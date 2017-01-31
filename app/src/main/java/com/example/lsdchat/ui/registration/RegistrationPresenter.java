package com.example.lsdchat.ui.registration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.lsdchat.R;
import com.example.lsdchat.ui.MainActivity;
import com.example.lsdchat.util.Email;
import com.example.lsdchat.util.ErrorsCode;
import com.example.lsdchat.util.Network;
import com.example.lsdchat.util.StorageHelper;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.redmadrobot.inputmask.MaskedTextChangedListener;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class RegistrationPresenter implements RegistrationContract.Presenter {
    private static final int REQUEST_IMAGE_CAMERA = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;
    private static final int MIN_DIGITS_AND_LETTERS_VALUE = 2;
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 12;

    private static final String DATE_FORMAT = "yyyyMMdd_HHmmss";
    private static final String AVATAR_FILE_NAME = "_avatar.jpg";
    private static final String PHONE_MASK = "+38 (0[00]) [000]-[00]-[00]";

    private RegistrationContract.View mView;
    private RegistrationContract.Model mModel;

    private Context mContext;
    private CallbackManager mCallbackManager;

    private String mUserFacebookId = null;
    private Uri mFullSizeAvatarUri = null;
    private Uri mUploadPhotoUri = null;
    private String mPhoneNumber = null;

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

    public RegistrationPresenter(RegistrationContract.View view) {
        mView = view;
        mModel = new RegistrationModel();
        mContext = view.getContext();
        mCallbackManager = CallbackManager.Factory.create();
    }


    public TextWatcher getTextWatcher() {
        return mTextWatcher;
    }

    public void requestSessionAndRegistration(boolean validateValue, RegistrationForm form, Button button) {
        if (validateValue) {
            if (mUploadPhotoUri != null) {
                mModel.getSessionAuth(form.getEmail().toString(), form.getPassword().toString())
                        .doOnRequest(aLong -> mView.showProgressBar())
                        .doOnUnsubscribe(() -> mView.hideProgressBar())
                        .doOnNext(sessionResponse -> {
                            File file = getAvatarFile(mUploadPhotoUri);
                            String token = sessionResponse.getSession().getToken();
                            String mime = getFileMimeType(file);
//                            String fileName = file.getName();
                            String fileName = "avatar.jpeg";
                            getBlobObjectCreateFile(token, mime, fileName);
                        })
                        .subscribe(sessionResponse -> {
                            String token = sessionResponse.getSession().getToken();
                            Log.e("TEST", token);
                        }, throwable -> {
                            Log.e("TEST", throwable.getMessage());

                            decodeThrowableAndShowAlert(throwable);
                        });
            } else {
                mModel.getSessionNoAuth()
                        .doOnRequest(request -> mView.showProgressBar())
                        .doOnUnsubscribe(() -> mView.hideProgressBar())
                        .subscribe(sessionResponse -> {
                            String token = sessionResponse.getSession().getToken();
                            Log.e("TEST", token);
                            getRegistrationWithToken(token, form);

                        }, throwable -> {
                            Log.e("TEST", throwable.getMessage());

                            decodeThrowableAndShowAlert(throwable);
                        });
            }
            button.setClickable(false);
        }
    }

    private void getRegistrationWithToken(String token, RegistrationForm form) {
        form.setPhone(mPhoneNumber);
        //Somehow API backend says wrong user_id, but it is correct
        //form.setFacebookId(Integer.parseInt(mUserFacebookId));
        mModel.getRegistration(token, form)
                .doOnRequest(request -> mView.showProgressBar())
                .doOnUnsubscribe(() -> mView.hideProgressBar())
                .subscribe(registrationResponse -> {
                    Log.e("TEST", String.valueOf(registrationResponse.getUser().getId()));
                    Toast.makeText(mContext, mContext.getString(R.string.registration_complete), Toast.LENGTH_SHORT).show();
                    navigateToMainScreen();
                }, throwable -> {
                    Log.e("TEST", throwable.getMessage());

                    decodeThrowableAndShowAlert(throwable);
                });
    }

    private void getBlobObjectCreateFile(String token, String mime, String fileName) {
        mModel.createFile(token, mime, fileName)
                .subscribe(registrationCreateFileResponse -> {
                    String blobId = registrationCreateFileResponse.getBlob().getBlobObjestAccess().getBlobId().toString();
                    Log.e("TEST", blobId);
                }, throwable -> {
                    Log.e("TEST", throwable.getMessage());

                    decodeThrowableAndShowAlert(throwable);
                });
    }

    private void navigateToMainScreen() {
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        mContext.startActivity(intent);
        ((Activity) mContext).finish();
    }

    @Override
    public void getFacebookToken() {
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //Somehow API backend says wrong user_id, but it is correct
                mUserFacebookId = loginResult.getAccessToken().getUserId();
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
    public void setTextChangedInputMaskListener(TextInputEditText phone) {
        MaskedTextChangedListener listener = new MaskedTextChangedListener(
                PHONE_MASK,
                true,
                phone,
                null,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onExtracted(@NotNull String s) {
                        if (s.length() == 9) mPhoneNumber = "+380" + s;
                    }

                    @Override
                    public void onMandatoryCharactersFilled(boolean b) {
                    }
                }
        );
        phone.addTextChangedListener(listener);
        phone.setOnFocusChangeListener(listener);
    }

    @Override
    public void onSignupButtonClickListener(Button button, TextInputEditText email, TextInputEditText pass, TextInputEditText confpass, TextInputEditText name, TextInputEditText web) {
        button.setOnClickListener(view -> {
            RegistrationForm form = new RegistrationForm();
            form.setEmail(email.getText().toString());
            form.setPassword(pass.getText().toString());
            form.setFullName(name.getText().toString());
            form.setWebsite(web.getText().toString());

            boolean validateValue = validateRegForm(
                    email.getText().toString(),
                    pass.getText().toString(),
                    confpass.getText().toString());

            requestSessionAndRegistration(validateValue, form, button);
        });
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
        if (TextUtils.isEmpty(email) || !Email.checkEmail(email)) {
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

        if (passLength < MIN_PASSWORD_LENGTH || passLength > MAX_PASSWORD_LENGTH) {
            mView.setLengthPasswordError();
            return false;
        }

        for (int i = 0; i < passLength; i++) {
            if (Character.isDigit(pass.charAt(i))) digitCounter++;
            if (Character.isUpperCase(pass.charAt(i))) capitalizeLetterCounter++;
        }
        if (digitCounter < MIN_DIGITS_AND_LETTERS_VALUE || capitalizeLetterCounter < MIN_DIGITS_AND_LETTERS_VALUE || passLength == (digitCounter + capitalizeLetterCounter)) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_IMAGE_CAMERA:
                if (resultCode == RESULT_OK) {
                    mView.getUserpicUri(mFullSizeAvatarUri);
                    try {
                        mUploadPhotoUri = StorageHelper.decodeAndSaveUri(mContext, mFullSizeAvatarUri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(mContext, mContext.getString(R.string.photo_added), Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_IMAGE_GALLERY:
                if (resultCode == RESULT_OK) {
                    mFullSizeAvatarUri = data.getData();
                    mView.getUserpicUri(mFullSizeAvatarUri);
                    try {
                        mUploadPhotoUri = StorageHelper.decodeAndSaveUri(mContext, mFullSizeAvatarUri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(mContext, mContext.getString(R.string.photo_added), Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                if (resultCode == RESULT_OK) {
                    mCallbackManager.onActivityResult(requestCode, resultCode, data);
                }
                break;
        }
    }

    public void getPhotoFromGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        ((Activity) mContext).startActivityForResult(pickPhoto, REQUEST_IMAGE_GALLERY);
    }

    public void getPhotoFromCamera() {
        //check for device camera
        if (mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (pictureIntent.resolveActivity(mContext.getPackageManager()) != null) {

                File storageDir = mContext.getExternalFilesDir(String.valueOf(Environment.DIRECTORY_PICTURES));
                String timestamp = new SimpleDateFormat(DATE_FORMAT).format(new Date());
                File file = new File(storageDir + "/" + timestamp + AVATAR_FILE_NAME);
                mFullSizeAvatarUri = Uri.fromFile(file);

                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mFullSizeAvatarUri);
                ((Activity) mContext).startActivityForResult(pictureIntent, REQUEST_IMAGE_CAMERA);
            }
        } else {
            Toast.makeText(mContext, mContext.getString(R.string.there_is_no_device_camera), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onAvatarClickListener(ImageView imageView) {
        imageView.setOnClickListener(view -> showDialogImageSourceChooser());
    }

    private void showDialogImageSourceChooser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setTitle(mContext.getString(R.string.add_photo))
                .setPositiveButton(mContext.getString(R.string.photo_gallery), (dialogInterface, i) -> {
                    getPhotoFromGallery();
                })
                .setNegativeButton(mContext.getString(R.string.device_camera), (dialogInterface, i) -> {
                    getPhotoFromCamera();
                });
        builder.create().show();
    }

    @Override
    public void onFacebookButtonClickListener(Button button) {
        button.setOnClickListener(view -> {
            loginWithFacebook();
            button.setText(mContext.getString(R.string.fb_button_text_linked));
            button.setClickable(false);
        });
    }

    private void loginWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions((Activity) mContext, Arrays.asList("public_profile"));
    }

    private void decodeThrowableAndShowAlert(Throwable t) {
        String title = t.getMessage();
        String message = ErrorsCode.getErrorMessage(mContext, t);
        mView.showResponseDialogError(title, message);
    }

    private File getAvatarFile(Uri uri) {
        return new File(uri.toString());
    }

    private long getFileLength(File file) {
        return file.length();
    }

    private String getFileMimeType(File file) {
        return URLConnection.guessContentTypeFromName(file.getName());
    }

    @Override
    public void onDestroy() {
        mView = null;
        mModel = null;
    }

    @Override
    public boolean isOnline() {
        return Network.isOnline(mContext);
    }
}
