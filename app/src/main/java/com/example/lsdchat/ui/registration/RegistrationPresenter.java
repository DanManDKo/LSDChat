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
import com.example.lsdchat.constant.ApiConstant;
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
    private static final String PHONE_MASK = "+38 (0[00]) [000]-[00]-[00]";

    private RegistrationContract.View mView;
    private RegistrationContract.Model mModel;

    private Context mContext;
    private CallbackManager mCallbackManager;

    private String mUserFacebookId;
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
    public void onFacebookButtonClickListener(Button button) {
        button.setOnClickListener(view -> {
            if (isOnline()) {
                LoginManager.getInstance().logInWithReadPermissions((Activity) mContext, Arrays.asList("public_profile"));

                getFacebookToken();

                button.setText(mContext.getString(R.string.fb_button_text_linked));
                button.setClickable(false);
            } else {
                mView.showNetworkErrorDialog();
            }
        });
    }

    @Override
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
                Log.e("FB", error.getMessage());
            }
        });
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

            if (isOnline()) {
                requestSessionAndRegistration(validateValue, form, button);
            } else {
                mView.showNetworkErrorDialog();
            }
        });
    }

    public void requestSessionAndRegistration(boolean validateValue, RegistrationForm form, Button button) {
        if (validateValue) {
            mModel.getSessionNoAuth()
                    .doOnRequest(request -> mView.showProgressBar())
                    .doOnUnsubscribe(() -> mView.hideProgressBar())
                    .subscribe(sessionResponse -> {

                        String token = sessionResponse.getSession().getToken();
                        getRegistrationWithToken(token, form);
                        Log.e("TEST", token);

                    }, throwable -> {
                        decodeThrowableAndShowAlert(throwable);
                        Log.e("TEST", throwable.getMessage());
                    });
            button.setClickable(false);
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
                }, this::decodeThrowableAndShowAlert);
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
                        mView.navigatetoMainScreen();
                    }
                })
                .subscribe(loginResponse -> {
                    //at this point user can be added to database
                }, this::decodeThrowableAndShowAlert);
    }

    private void getBlobObjectCreateFile(String token, String mime, String fileName) {
        mModel.createFile(token, mime, fileName)
                .doOnRequest(request -> mView.showProgressBar())
                .doOnUnsubscribe(() -> mView.hideProgressBar())
                .subscribe(registrationCreateFileResponse -> {
                    long blobId = registrationCreateFileResponse.getBlob().getBlobObjestAccess().getBlobId();
                    String params = registrationCreateFileResponse.getBlob().getBlobObjestAccess().getParams();
                    Uri uri = Uri.parse(params);

                    RequestBody contentR = RequestBody.create(MultipartBody.FORM, uri.getQueryParameter(ApiConstant.UploadParametres.CONTENT_TYPE));
                    RequestBody expiresR = RequestBody.create(MultipartBody.FORM, uri.getQueryParameter(ApiConstant.UploadParametres.EXPIRES));
                    RequestBody aclR = RequestBody.create(MultipartBody.FORM, uri.getQueryParameter(ApiConstant.UploadParametres.ACL));
                    RequestBody keyR = RequestBody.create(MultipartBody.FORM, uri.getQueryParameter(ApiConstant.UploadParametres.KEY));
                    RequestBody policyR = RequestBody.create(MultipartBody.FORM, uri.getQueryParameter(ApiConstant.UploadParametres.POLICY));
                    RequestBody successR = RequestBody.create(MultipartBody.FORM, uri.getQueryParameter(ApiConstant.UploadParametres.SUCCESS_ACTION_STATUS));
                    RequestBody algorithmR = RequestBody.create(MultipartBody.FORM, uri.getQueryParameter(ApiConstant.UploadParametres.ALGORITHM));
                    RequestBody credentialR = RequestBody.create(MultipartBody.FORM, uri.getQueryParameter(ApiConstant.UploadParametres.CREDENTIAL));
                    RequestBody dateR = RequestBody.create(MultipartBody.FORM, uri.getQueryParameter(ApiConstant.UploadParametres.DATE));
                    RequestBody signatureR = RequestBody.create(MultipartBody.FORM, uri.getQueryParameter(ApiConstant.UploadParametres.SIGNATURE));

                    RequestBody file = RequestBody.create(MediaType.parse(getFileMimeType(mUploadFile)), mUploadFile);
                    MultipartBody.Part multiPart = MultipartBody.Part.createFormData(ApiConstant.UploadParametres.FILE, mUploadFile.getName(), file);

                    uploadFileRetrofit(token, blobId, contentR, expiresR, aclR, keyR, policyR, successR, algorithmR, credentialR, dateR, signatureR, multiPart);

                }, throwable -> {
                    decodeThrowableAndShowAlert(throwable);
                    Log.e("TEST", throwable.getMessage());
                });
    }

    private void uploadFileRetrofit(
            String token, long blobId,
            RequestBody content,
            RequestBody expires,
            RequestBody acl,
            RequestBody key,
            RequestBody policy,
            RequestBody success,
            RequestBody algorithm,
            RequestBody credential,
            RequestBody date,
            RequestBody signature,
            MultipartBody.Part file) {

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put(ApiConstant.UploadParametres.CONTENT_TYPE, content);
        map.put(ApiConstant.UploadParametres.EXPIRES, expires);
        map.put(ApiConstant.UploadParametres.ACL, acl);
        map.put(ApiConstant.UploadParametres.KEY, key);
        map.put(ApiConstant.UploadParametres.POLICY, policy);
        map.put(ApiConstant.UploadParametres.SUCCESS_ACTION_STATUS, success);
        map.put(ApiConstant.UploadParametres.ALGORITHM, algorithm);
        map.put(ApiConstant.UploadParametres.CREDENTIAL, credential);
        map.put(ApiConstant.UploadParametres.DATE, date);
        map.put(ApiConstant.UploadParametres.SIGNATURE, signature);

        mModel.uploadFileMap(map, file)
                .doOnRequest(request -> mView.showProgressBar())
                .doOnUnsubscribe(() -> mView.hideProgressBar())
                .subscribe(aVoid -> {
                    long fileSize = mUploadFile.length();
                    if (fileSize != 0 && blobId != 0) {
                        declareFileUploaded(fileSize, token, blobId);
                    }
                }, this::decodeThrowableAndShowAlert);
    }


    private void declareFileUploaded(long size, String token, long blobId) {
        mModel.declareFileUploaded(size, token, blobId)
                .doOnRequest(request -> mView.showProgressBar())
                .doOnUnsubscribe(() -> mView.hideProgressBar())
                .subscribe(aVoid -> {

                    Toast.makeText(mContext, mContext.getString(R.string.registration_complete), Toast.LENGTH_SHORT).show();
                    mView.navigatetoMainScreen();
                }, this::decodeThrowableAndShowAlert);

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
    public void onDestroy() {
        mView = null;
        mModel = null;
    }

    @Override
    public boolean isOnline() {
        return Network.isOnline(mContext);
    }
}
