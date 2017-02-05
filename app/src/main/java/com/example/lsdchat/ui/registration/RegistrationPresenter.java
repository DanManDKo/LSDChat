package com.example.lsdchat.ui.registration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Path;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.example.lsdchat.ui.MainActivity;
import com.example.lsdchat.util.Email;
import com.example.lsdchat.util.ErrorsCode;
import com.example.lsdchat.util.Network;
import com.example.lsdchat.util.RequestBuilder;
import com.example.lsdchat.util.StorageHelper;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.common.file.FileUtils;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.redmadrobot.inputmask.MaskedTextChangedListener;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
            loginWithFacebook();
            button.setText(mContext.getString(R.string.fb_button_text_linked));
            button.setClickable(false);
        });
    }

    private void loginWithFacebook() {
        LoginManager.getInstance().logInWithReadPermissions((Activity) mContext, Arrays.asList("public_profile"));
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
        //Somehow API backend says wrong user_id, but it is correct
        //form.setFacebookId(Integer.parseInt(mUserFacebookId));
        mModel.getRegistration(token, form)
                .doOnRequest(request -> mView.showProgressBar())
                .doOnUnsubscribe(() -> mView.hideProgressBar())
                .doOnNext(registrationResponse -> {
                    getLoginRegistratedUser(form.getEmail(), form.getPassword(), token);
                })
                .subscribe(registrationResponse -> {
                    Log.e("TEST", String.valueOf(registrationResponse.getUser().getId()));
                }, throwable -> {
                    decodeThrowableAndShowAlert(throwable);
                    Log.e("TEST", throwable.getMessage());
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
                        mView.navigatetoMainScreen();
                    }
                })
                .subscribe(loginResponse -> {
                    //at this point user can be added to database
                }, throwable -> {
                    decodeThrowableAndShowAlert(throwable);
                    Log.e("TEST", throwable.getMessage());
                });
    }

    private void getBlobObjectCreateFile(String token, String mime, String fileName) {
        mModel.createFile(token, mime, fileName)
                .subscribe(registrationCreateFileResponse -> {
                    long blobId = registrationCreateFileResponse.getBlob().getBlobObjestAccess().getBlobId();
                    String params = registrationCreateFileResponse.getBlob().getBlobObjestAccess().getParams();
                    Uri uri = Uri.parse(params);

                    RequestBody file = RequestBody.create(MediaType.parse("image/jpg"), mUploadFile);
                    //***
                    MultipartBody.Part multiPart = MultipartBody.Part.createFormData(ApiConstant.UploadParametres.FILE, mUploadFile.getName(), file);
                    //***
                    uploadFileRetrofit(
                            uri.getQueryParameter(ApiConstant.UploadParametres.CONTENT_TYPE),
                            uri.getQueryParameter(ApiConstant.UploadParametres.EXPIRES),
                            uri.getQueryParameter(ApiConstant.UploadParametres.ACL),
                            uri.getQueryParameter(ApiConstant.UploadParametres.KEY),
                            uri.getQueryParameter(ApiConstant.UploadParametres.POLICY),
                            uri.getQueryParameter(ApiConstant.UploadParametres.SUCCESS_ACTION_STATUS),
                            uri.getQueryParameter(ApiConstant.UploadParametres.ALGORITHM),
                            uri.getQueryParameter(ApiConstant.UploadParametres.CREDENTIAL),
                            uri.getQueryParameter(ApiConstant.UploadParametres.DATE),
                            uri.getQueryParameter(ApiConstant.UploadParametres.SIGNATURE),
                            multiPart);

//                    uploadFile(token, blobId,
//                            uri.getQueryParameter(ApiConstant.UploadParametres.CONTENT_TYPE),
//                            uri.getQueryParameter(ApiConstant.UploadParametres.EXPIRES),
//                            uri.getQueryParameter(ApiConstant.UploadParametres.ACL),
//                            uri.getQueryParameter(ApiConstant.UploadParametres.KEY),
//                            uri.getQueryParameter(ApiConstant.UploadParametres.POLICY),
//                            uri.getQueryParameter(ApiConstant.UploadParametres.SUCCESS_ACTION_STATUS),
//                            uri.getQueryParameter(ApiConstant.UploadParametres.ALGORITHM),
//                            uri.getQueryParameter(ApiConstant.UploadParametres.CREDENTIAL),
//                            uri.getQueryParameter(ApiConstant.UploadParametres.DATE),
//                            uri.getQueryParameter(ApiConstant.UploadParametres.SIGNATURE),
//                            mUploadFile);

                }, throwable -> {
                    decodeThrowableAndShowAlert(throwable);
                    Log.e("TEST", throwable.getMessage());
                });
    }

    private void uploadFileRetrofit(String content,
                                    String expires,
                                    String acl,
                                    String key,
                                    String policy,
                                    String success,
                                    String algorithm,
                                    String credential,
                                    String date,
                                    String signature,
                                    MultipartBody.Part file) {
        mModel.uploadFile(content, expires, acl, key, policy, success, algorithm, credential, date, signature, file)
                .doOnNext(aVoid -> {
                    mView.showAlertD();
                })
                .subscribe(aVoid -> {}, throwable -> {
                    Log.e("TEST", throwable.getMessage());
                });
    }

    private void uploadFile(String token, long blobId, String type, String expires, String acl, String key, String policy, String actionStatus, String algorithm, String credential, String date, String signature, File file) {
        final HttpUrl url = new HttpUrl.Builder().scheme("https").host("qbprod.s3.amazonaws.com").build();
        final OkHttpClient client = new OkHttpClient();

        new AsyncTask<Void, Void, Response>() {

            @Override
            protected okhttp3.Response doInBackground(Void... voids) {
                okhttp3.Response response = null;
                try {
                    Request request = new Request.Builder()
                            .url(url)
                            .post(RequestBuilder.uploadRequestBody(type, expires, acl, key, policy, actionStatus,
                                    algorithm, credential, date, signature, file))
                            .build();

                    response = client.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return response;
            }

            @Override
            protected void onPostExecute(okhttp3.Response response) {
                if (response != null) {
                    declareFileUploaded(file.length(), token, blobId);
                }
            }
        }.execute();
    }

    private void declareFileUploaded(long size, String token, long blobId) {
        mModel.declareFileUploaded(size, token, blobId)
                .doOnNext(aVoid -> mView.navigatetoMainScreen())
                .subscribe(aVoid -> {

                }, throwable -> {
                    decodeThrowableAndShowAlert(throwable);
                    Log.e("TEST", throwable.getMessage());
                });
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
