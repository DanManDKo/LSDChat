package com.example.lsdchat.ui.main.createchat;


import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.example.lsdchat.R;
import com.example.lsdchat.api.dialog.request.CreateDialogRequest;
import com.example.lsdchat.constant.ApiConstant;
import com.example.lsdchat.ui.main.conversation.ConversationFragment;
import com.example.lsdchat.util.CreateMapRequestBody;

import java.io.File;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;

public class CreateChatPresenter implements CreateChatContract.Presenter {


    private CreateChatContract.View mView;
    private CreateChatContract.Model mModel;
    private Set<Integer> idChecked;


    public CreateChatPresenter(CreateChatContract.View mView, CreateChatContract.Model mModel) {
        this.mView = mView;
        this.mModel = mModel;
        idChecked = new TreeSet<>();


    }

    @Override
    public void onDestroy() {
        mView = null;
        mModel = null;

    }


    private void createDialog(CreateDialogRequest request) {
        mModel.createDialog(mModel.getToken(), request)
                .subscribe(itemDialog ->
                                mView.navigateToChat(ConversationFragment
                                        .newInstance(itemDialog.getId(), itemDialog.getName())),
                        throwable -> {
                            Log.e("DIALOG", throwable.getMessage());
                        });
    }


    private void getBlobObjectCreateFile(File mUploadFile) {
        String mime = getFileMimeType(mUploadFile);
        String fileName = mUploadFile.getName();

        mModel.createFile(mModel.getToken(), mime, fileName)

                .subscribe(registrationCreateFileResponse -> {
                    Integer blobId = registrationCreateFileResponse.getBlob().getBlobObjestAccess().getBlobId();
                    String params = registrationCreateFileResponse.getBlob().getBlobObjestAccess().getParams();
                    Uri uri = Uri.parse(params);

                    RequestBody file = RequestBody.create(MediaType.parse(getFileMimeType(mUploadFile)), mUploadFile);
                    MultipartBody.Part multiPart = MultipartBody.Part.createFormData(ApiConstant.UploadParametres.FILE, mUploadFile.getName(), file);

                    uploadFileRetrofit(mUploadFile, blobId, CreateMapRequestBody.createMapRequestBody(uri), multiPart);
                }, throwable -> mView.showErrorDialog(throwable));
    }

    private void uploadFileRetrofit(File mUploadFile, long blobId, HashMap<String, RequestBody> map, MultipartBody.Part file) {
        mModel.uploadFileMap(map, file)
                .subscribe(aVoid -> {
                    long fileSize = mUploadFile.length();
                    if (fileSize != 0 && blobId != 0) {
                        declareFileUploaded(fileSize, blobId);
                    }
                }, throwable -> mView.showErrorDialog(throwable));
    }


    private void declareFileUploaded(long size, long blobId) {
        mModel.declareFileUploaded(size, mModel.getToken(), blobId)
                .subscribe(aVoid -> {
                    createDialog(getTypeDialog(blobId));
                    Log.e("TEST", "SUCCESS");
                }, throwable -> mView.showErrorDialog(throwable));
    }

    private String getFileMimeType(File file) {
        return URLConnection.guessContentTypeFromName(file.getName());
    }

    @Override
    public void getUserListObservable() {
        mModel.getUserListObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(loginUserList -> mView.setListUsers(loginUserList));
    }

    @Override
    public void getObservableUserAvatar() {
        mModel.getObservableUserAvatar()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contentModels -> mView.setContentModelList(contentModels));
    }

    @Override
    public void checkBoxSetOnChecked(int userId, boolean isChecked) {
        if (!isChecked) {
            idChecked.remove(userId);
        } else {
            idChecked.add(userId);
        }

        if (idChecked.size() == 1) {
            mView.setNameAccessibility(false);
            mView.setImageAccessibility(false);
        } else {
            mView.setNameAccessibility(true);
            mView.setImageAccessibility(true);
        }
    }

    private boolean isCheckData() {
        String nameDialog = mView.getNameDialog();

        if (mView.isRbPublic()) {
            if (nameDialog.isEmpty()) {
                mView.showErrorDialog(R.string.chat_error_name);
                return false;
            } else {
                return true;
            }
        } else if (mView.isRbPrivate()) {
            if (idChecked.size() == 0) {
                mView.showErrorDialog(R.string.chat_error_users);
                return false;
            } else if (idChecked.size() > 1) {
                if (nameDialog.isEmpty()) {
                    mView.showErrorDialog(R.string.chat_error_name);
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } else {
            mView.showErrorDialog(R.string.chat_error_type);
            return false;
        }
    }

    @Override
    public void setClickCreateNewDialog(File mUploadFile) {
        if (isCheckData()) {
            if (mUploadFile != null) {
                getBlobObjectCreateFile(mUploadFile);
            } else {
                createDialog(getTypeDialog(0));
            }
        }
    }

    private CreateDialogRequest getTypeDialog(long imageId) {
        String nameDialog = mView.getNameDialog();
        CreateDialogRequest createDialogRequest = new CreateDialogRequest();

        if (mView.isRbPublic()) {
            createDialogRequest.setType(ApiConstant.TYPE_DIALOG_PUBLIC);
            createDialogRequest.setName(nameDialog);
            if (imageId != 0) {
                createDialogRequest.setPhotoId(imageId);
            }

        } else if (mView.isRbPrivate()) {
            if (idChecked.size() == 1) {
                createDialogRequest.setType(ApiConstant.TYPE_DIALOG_PRIVATE);
                for (Integer i : idChecked) {
                    createDialogRequest.setIdU(String.valueOf(i));
                }


            } else if (idChecked.size() > 1) {
                createDialogRequest.setType(ApiConstant.TYPE_DIALOG_GROUP);
                createDialogRequest.setName(nameDialog);
                List<String> list = new ArrayList<>();
                for (Integer i : idChecked) {
                    list.add(String.valueOf(i));
                }

                String joined = TextUtils.join(", ", list);
                createDialogRequest.setIdU(joined);

                if (imageId != 0) {
                    createDialogRequest.setPhotoId(imageId);
                }
            } else {
                Log.e("getTypeDialog", "Select item id");
            }

        } else {
            Log.e("getTypeDialog", "Select public or private");
        }


        return createDialogRequest;
    }
}
