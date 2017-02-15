package com.example.lsdchat.ui.registration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import com.example.lsdchat.R;
import com.example.lsdchat.constant.ApiConstant;
import com.example.lsdchat.util.CreateMapRequestBody;
import com.example.lsdchat.util.Email;
import com.example.lsdchat.util.ErrorsCode;
import com.example.lsdchat.util.Network;
import com.example.lsdchat.util.StorageHelper;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

public class RegistrationPresenter implements RegistrationContract.Presenter {
    private static final int REQUEST_IMAGE_CAMERA = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;
    private static final int MIN_DIGITS_AND_LETTERS_VALUE = 2;
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 12;

    private static final String DATE_FORMAT = "yyyyMMdd_HHmmss";
    private static final String AVATAR_FILE_NAME = "_avatar.jpg";

    private RegistrationContract.View mView;
    private RegistrationContract.Model mModel;

    private Context mContext;
    private CallbackManager mCallbackManager;

    private String mUserFacebookId = null;
    private Uri mFullSizeAvatarUri = null;
    private File mUploadFile = null;
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

    @Override
    public void onFacebookButtonClickListener() {
        if (isOnline(mContext)) {
            LoginManager.getInstance().logInWithReadPermissions((Activity) mContext, Arrays.asList("public_profile"));
            getFacebookToken();

            mView.setLinkedStatus();
            mView.setClickableFacebookButton(false);
        } else {
            mView.showNetworkErrorDialog();
        }
    }

    public void getFacebookToken() {
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mUserFacebookId = loginResult.getAccessToken().getUserId();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                mView.setClickableFacebookButton(true);
                Log.e("FB", error.getMessage());
            }
        });
    }

    @Override
    public void onSignupButtonClickListener(String email, String password, String confPassword, String name, String website) {

        RegistrationForm form = new RegistrationForm();
        form.setEmail(email);
        form.setPassword(password);
        form.setFullName(name);
        form.setWebsite(website);

        boolean validateValue = validateRegForm(email, password, confPassword);

        if (isOnline(mContext)) {
            requestSessionAndRegistration(validateValue, form);
        } else {
            mView.showNetworkErrorDialog();
        }
    }

    @Override
    public void requestSessionAndRegistration(boolean validateValue, RegistrationForm form) {
        if (validateValue) {
            mModel.getSessionNoAuth()
                    .doOnRequest(request -> mView.showProgressBar())
                    .doOnUnsubscribe(() -> mView.hideProgressBar())
                    .subscribe(sessionResponse -> {

                        String token = sessionResponse.getSession().getToken();
                        getRegistrationWithToken(token, form);
                        Log.e("TEST", token);

                    }, throwable -> {

                        mView.setClickableSignupButton(true);
                        decodeThrowableAndShowAlert(throwable);
                    });
            mView.setClickableSignupButton(false);
        }
    }

    private void getRegistrationWithToken(String token, RegistrationForm form) {
        form.setPhone(mPhoneNumber);

        if (mUserFacebookId != null) {
            form.setFacebookId(mUserFacebookId);
        }

        mModel.getRegistration(token, form)
                .doOnRequest(request -> mView.showProgressBar())
                .doOnUnsubscribe(() -> mView.hideProgressBar())
                .doOnNext(registrationResponse -> {
                    getLoginRegistratedUser(form.getEmail(), form.getPassword(), token);
                })
                .subscribe(registrationResponse -> {
                }, throwable -> {

                    mView.setClickableSignupButton(true);
                    decodeThrowableAndShowAlert(throwable);
                });
    }

    private void getLoginRegistratedUser(String email, String password, String token) {
        mModel.getLogin(email, password, token)
                .doOnRequest(request -> mView.showProgressBar())
                .doOnUnsubscribe(() -> mView.hideProgressBar())
                .doOnNext(loginResponse -> {
                    if (mUploadFile != null) {
                        getBlobObjectCreateFile(token, getFileMimeType(mUploadFile), mUploadFile.getName());
                    } else {
                        Toast.makeText(mContext, mContext.getString(R.string.registration_complete), Toast.LENGTH_SHORT).show();
                        mView.navigateToMainScreen();
                    }
                })
                .subscribe(loginResponse -> {
                    //at this point user can be added to database
                }, throwable -> {

                    mView.setClickableSignupButton(true);
                    decodeThrowableAndShowAlert(throwable);
                });
    }

    private void getBlobObjectCreateFile(String token, String mime, String fileName) {
        mModel.createFile(token, mime, fileName)
                .doOnRequest(request -> mView.showProgressBar())
                .doOnUnsubscribe(() -> mView.hideProgressBar())
                .subscribe(registrationCreateFileResponse -> {
                    long blobId = registrationCreateFileResponse.getBlob().getBlobObjestAccess().getBlobId();
                    String params = registrationCreateFileResponse.getBlob().getBlobObjestAccess().getParams();
                    Uri uri = Uri.parse(params);

                    RequestBody file = RequestBody.create(MediaType.parse(getFileMimeType(mUploadFile)), mUploadFile);
                    MultipartBody.Part multiPart = MultipartBody.Part.createFormData(ApiConstant.UploadParametres.FILE, mUploadFile.getName(), file);

                    uploadFileRetrofit(token, blobId, CreateMapRequestBody.createMapRequestBody(uri), multiPart);

                }, throwable -> {

                    decodeThrowableAndShowAlert(throwable);
                    Log.e("TEST", throwable.getMessage());
                });
    }

    private void uploadFileRetrofit(String token, long blobId, HashMap<String, RequestBody> map, MultipartBody.Part file) {

        mModel.uploadFileMap(map, file)
                .doOnRequest(request -> mView.showProgressBar())
                .doOnUnsubscribe(() -> mView.hideProgressBar())
                .subscribe(aVoid -> {
                    long fileSize = mUploadFile.length();
                    if (fileSize != 0 && blobId != 0) {
                        declareFileUploaded(fileSize, token, blobId);
                    }
                }, throwable -> {

                    mView.setClickableSignupButton(true);
                    decodeThrowableAndShowAlert(throwable);
                });
    }


    private void declareFileUploaded(long size, String token, long blobId) {
        mModel.declareFileUploaded(size, token, blobId)
                .doOnRequest(request -> mView.showProgressBar())
                .doOnUnsubscribe(() -> mView.hideProgressBar())
                .subscribe(aVoid -> {

                    Toast.makeText(mContext, mContext.getString(R.string.registration_complete), Toast.LENGTH_SHORT).show();
                    mView.navigateToMainScreen();
                }, throwable -> {

                    mView.setClickableSignupButton(true);
                    decodeThrowableAndShowAlert(throwable);
                });
    }

    @Override
    public void setPhoneNumber(String phone) {
        mPhoneNumber = phone;
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
    public void onAvatarClickListener() {
        mView.showDialogImageSourceChooser();
    }

    @Override
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_IMAGE_CAMERA:
                if (resultCode == RESULT_OK) {
                    mView.getUserpicUri(mFullSizeAvatarUri);
                    try {
                        mUploadFile = StorageHelper.decodeAndSaveUri(mContext, mFullSizeAvatarUri);
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
                        mUploadFile = StorageHelper.decodeAndSaveUri(mContext, mFullSizeAvatarUri);
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

    private void decodeThrowableAndShowAlert(Throwable t) {
        String title = t.getMessage();
        String message = ErrorsCode.getErrorMessage(mContext, t);
        mView.showResponseDialogError(title, message);
        mView.hideProgressBar();
    }

    private String getFileMimeType(File file) {
        return URLConnection.guessContentTypeFromName(file.getName());
    }

    public TextWatcher getTextWatcher() {
        return mTextWatcher;
    }

    @Override
    public boolean isOnline(Context context) {
        return Network.isOnline(context);
    }

    @Override
    public void onDestroy() {
        mView = null;
        mModel = null;
        mCallbackManager = null;
    }
}
