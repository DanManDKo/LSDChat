package com.example.lsdchat.ui.main.createchat;


import android.support.v4.app.Fragment;

import com.example.lsdchat.api.dialog.model.ItemDialog;
import com.example.lsdchat.api.dialog.request.CreateDialogRequest;
import com.example.lsdchat.api.login.model.LoginUser;
import com.example.lsdchat.api.registration.response.RegistrationCreateFileResponse;
import com.example.lsdchat.model.ContentModel;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

public interface CreateChatContract {

    interface View {

        void showErrorDialog(Throwable throwable);

        void showErrorDialog(int messageId);

        void setNameAccessibility(boolean enable);

        void setImageAccessibility(boolean enable);

        boolean isRbPublic();

        boolean isRbPrivate();

        String getNameDialog();


        void navigateToChat(Fragment fragment);
    }

    interface Presenter {
        void onDestroy();

        Observable<List<LoginUser>> getUserListObservable();

        void checkBoxSetOnChecked(int userId, boolean isChecked);

        void setClickCreateNewDialog(File mUploadFile);

        Observable<List<ContentModel>> getObservableUserAvatar();
    }

    interface Model {
        Observable<ItemDialog> createDialog(String token, CreateDialogRequest createDialogRequest);

        Observable<RegistrationCreateFileResponse> createFile(String token, String mime, String fileName);

        Observable<Void> declareFileUploaded(long size, String token, long blobId);

        Observable<Void> uploadFileMap(Map<String, RequestBody> map, MultipartBody.Part part);

        String getToken();

        Observable<List<ContentModel>> getObservableUserAvatar();

        Observable<List<LoginUser>> getUserListObservable();

    }
}
