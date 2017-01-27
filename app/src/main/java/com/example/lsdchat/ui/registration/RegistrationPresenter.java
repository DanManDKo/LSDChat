package com.example.lsdchat.ui.registration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.RelativeLayout;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Pattern;

import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser;
import ru.tinkoff.decoro.slots.Slot;
import ru.tinkoff.decoro.watchers.FormatWatcher;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;

import static android.app.Activity.RESULT_OK;


public class RegistrationPresenter implements RegistrationContract.Presenter {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;
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
            Toast.makeText(mContext, mContext.getString(R.string.registration_complete), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mContext, MainActivity.class);
            mContext.startActivity(intent);
            ((Activity) mContext).finish();
        }
    }

    @Override
    public void showDialogImageSourceChooser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setTitle(mContext.getString(R.string.image_source_chooser))
                .setPositiveButton(mContext.getString(R.string.photo_gallery), (dialogInterface, i) -> {
                    getPhotoFromGallery();
                })
                .setNegativeButton(mContext.getString(R.string.device_camera), (dialogInterface, i) -> {
                    getPhotoFromCamera();
                });
        builder.create().show();
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
    public Uri getUserImageFromCameraOrGallery(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    return data.getData();
                }
                break;
            case REQUEST_IMAGE_GALLERY:
                if (resultCode == RESULT_OK) {
                    return data.getData();
                }
                break;
            default:
                break;
        }
        return null;
    }

    public CallbackManager getCallbackManager() {
        return mCallbackManager;
    }

    void getPhotoFromGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        ((Activity) mContext).startActivityForResult(pickPhoto, REQUEST_IMAGE_GALLERY);
    }

    void getPhotoFromCamera() {
        //check for device camera
        if (mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (pictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(mContext,
                            "com.internship.lsd.lsdchat",
                            photoFile);
                    pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    ((Activity) mContext).startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        } else {
            Toast.makeText(mContext, mContext.getString(R.string.there_is_no_device_camera), Toast.LENGTH_SHORT).show();
        }
    }

    private String mCurrentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "LSD_" + timeStamp + "_";
        File storageDir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        // Save a file path
        mCurrentPhotoPath = image.getAbsolutePath();

        return image;
    }
    public Bitmap getPic(int w, int h) {
        // Get the dimensions of the View
        int targetW = w;
        int targetH = h;

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        return bitmap;
    }
}
