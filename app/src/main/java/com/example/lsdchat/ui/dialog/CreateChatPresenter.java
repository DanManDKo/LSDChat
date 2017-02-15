package com.example.lsdchat.ui.dialog;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lsdchat.R;
import com.example.lsdchat.api.dialog.model.ItemUser;
import com.example.lsdchat.api.dialog.request.CreateDialogRequest;
import com.example.lsdchat.constant.ApiConstant;
import com.example.lsdchat.manager.SharedPreferencesManager;
import com.example.lsdchat.model.ContactsModel;
import com.example.lsdchat.util.CreateMapRequestBody;
import com.example.lsdchat.util.StorageHelper;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

public class CreateChatPresenter implements CreateChatContract.Presenter {

    private static final int REQUEST_IMAGE_CAMERA = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;
    private static final String DATE_FORMAT = "yyyyMMdd_HHmmss";
    private static final String AVATAR_FILE_NAME = "_icon.jpg";
    private Uri mFullSizeAvatarUri = null;
    private File mUploadFile = null;
    private Context mContext;
    private CreateChatContract.View mView;
    private CreateChatContract.Model mModel;
    private SharedPreferencesManager mSharedPreferencesManager;

    public CreateChatPresenter(CreateChatContract.View mView, SharedPreferencesManager sharedPreferencesManager) {
        this.mView = mView;
        mModel = new CreateChatModel();
        this.mSharedPreferencesManager = sharedPreferencesManager;
        mContext = mView.getContext();
    }

    @Override
    public void onDestroy() {
        mView = null;
        mModel = null;
        mContext = null;
    }

    @Override
    public void btnCreateClick(Button btnCreate, EditText etName) {
        btnCreate.setOnClickListener(view -> {

            if (mUploadFile != null) {
                getBlobObjectCreateFile(getToken());
            } else {
                createDialog(getToken(), getTypeDialog(mView.getChatName()));
            }

        });
    }

    @Override
    public void btnImageClick(SimpleDraweeView imageView) {
        imageView.setOnClickListener(view -> mView.showDialogImageSourceChooser());
    }

    @Override
    public void createDialog(String token, CreateDialogRequest request) {


        mModel.createDialog(token, request)
                .subscribe(itemDialog -> {
                            Log.e("DIALOG", itemDialog.getId());
                            Log.e("DIALOG", itemDialog.getName());
                            Log.e("DIALOG", itemDialog.getPhoto());
                        },
                        throwable -> {
                            Log.e("DIALOG", throwable.getMessage());
                        });

    }

    @Override
    public CreateDialogRequest getTypeDialog(String nameDialog) {
        if (mView.isRbPublic()) {
            return new CreateDialogRequest(ApiConstant.TYPE_DIALOG_PUBLIC, nameDialog);

        } else {
            return new CreateDialogRequest(ApiConstant.TYPE_DIALOG_PUBLIC, nameDialog);
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

        }
    }


    @Override
    public void getPhotoFromGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        ((Activity) mContext).startActivityForResult(pickPhoto, REQUEST_IMAGE_GALLERY);
    }

    @Override
    public void getPhotoFromCamera() {
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


    private void getBlobObjectCreateFile(String token) {
        String mime = getFileMimeType(mUploadFile);
        String fileName = mUploadFile.getName();

        mModel.createFile(token, mime, fileName)

                .subscribe(registrationCreateFileResponse -> {
                    Integer blobId = registrationCreateFileResponse.getBlob().getBlobObjestAccess().getBlobId();
                    String params = registrationCreateFileResponse.getBlob().getBlobObjestAccess().getParams();
                    Uri uri = Uri.parse(params);

                    RequestBody file = RequestBody.create(MediaType.parse(getFileMimeType(mUploadFile)), mUploadFile);
                    MultipartBody.Part multiPart = MultipartBody.Part.createFormData(ApiConstant.UploadParametres.FILE, mUploadFile.getName(), file);

                    uploadFileRetrofit(token, blobId, CreateMapRequestBody.createMapRequestBody(uri), multiPart);
                }, throwable -> {

                    Log.e("TEST", throwable.getMessage());
                });
    }

    private void uploadFileRetrofit(String token, long blobId, HashMap<String, RequestBody> map, MultipartBody.Part file) {

        mModel.uploadFileMap(map, file)

                .subscribe(aVoid -> {
                    long fileSize = mUploadFile.length();
                    if (fileSize != 0 && blobId != 0) {
                        declareFileUploaded(fileSize, token, blobId);
                    }
                }, throwable -> {


                    Log.e("TEST", throwable.getMessage());
                });
    }


    private void declareFileUploaded(long size, String token, long blobId) {
        mModel.declareFileUploaded(size, token, blobId)
                .subscribe(aVoid -> {

                    createDialog(token, new CreateDialogRequest(ApiConstant.TYPE_DIALOG_PUBLIC, mView.getChatName(), blobId));
                    Log.e("TEST", "SUCCESS");
                }, throwable -> {
                    Log.e("TEST", throwable.getMessage());
                });
    }

    private String getFileMimeType(File file) {
        return URLConnection.guessContentTypeFromName(file.getName());
    }

    @Override
    public void getContactsModel() {

        mModel.getUserList(getToken())
                .doOnNext(userListResponse -> mView.initAdapter())
                .subscribe(userListResponse -> {
                    List<ItemUser> loginResponses = userListResponse.getItemUserList();

                    getFileImage(loginResponses);

                }, throwable -> {
                    Log.e("getUserList", throwable.getMessage());
                });


    }

//TODO: what the f..? What add only 5 users? (|
    private void getFileImage(List<ItemUser> loginResponses) {
        for (ItemUser user : loginResponses) {


            if (user.getUser().getBlobId() != 0) {
                long id = user.getUser().getBlobId();

                mModel.downloadImage(id, getToken())
                        .subscribe(file -> {
                            Log.e("getUserList", "FILE OK");

                            mModel.insertToDb(new ContactsModel(user.getUser().getFullName(), user.getUser().getEmail(), file.getPath()));
                        }, throwable -> {
                            Log.e("getFileImage", throwable.getMessage());

                        });

            } else {

                mModel.insertToDb(new ContactsModel(user.getUser().getFullName(), user.getUser().getEmail()));
            }


        }
    }

    @Override
    public List<ContactsModel> getListContactsModel() {
        return mModel.getContactsModel();
    }

    @Override
    public String getToken() {
        return mSharedPreferencesManager.getToken();
    }
}
