package com.example.lsdchat.ui.main.editchat;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.lsdchat.api.dialog.model.ItemDialog;
import com.example.lsdchat.api.dialog.request.CreateDialogRequest;
import com.example.lsdchat.api.dialog.request.UpdateDialogRequest;
import com.example.lsdchat.api.dialog.response.DialogsResponse;
import com.example.lsdchat.api.login.model.LoginUser;
import com.example.lsdchat.api.login.response.LoginResponse;
import com.example.lsdchat.api.registration.response.RegistrationCreateFileResponse;
import com.example.lsdchat.model.ContentModel;
import com.example.lsdchat.model.RealmDialogModel;
import com.example.lsdchat.ui.BasePresenter;

import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;

public interface EditchatContract {
    interface Presenter extends BasePresenter {
        void loadDialogCredentials(String dialogID);

        void getAvatarsFromDatabase();

        void getPhotoFromGallery();

        void getPhotoFromCamera();

        void saveDialogImageUri(Uri uri);

        Uri getDialogImageUri();

        void showPermissionErrorMessage();

        void updateDialogCredentials(String dialogName);

        void checkBoxSetOnChecked(int userId, boolean isChecked);
    }

    interface View {
        Context getContext();
        boolean isNetworkConnect();
        void fillDialogNameField(String name, Integer dialogCreaterID);

        void showDialogAvatar(Uri path);

        void fillDialogAdapter(List<Integer> occupantIDs, List<LoginUser> appUsers, int type);

        void fillAdapterContentModelsList(List<ContentModel> contentModels);

        void showPermissionErrorMessage();

        void navigateToConversationFragment(String dialogID, String dialogName, int dialogType, int singleOccupant);

        void initOccupantsIdList(Set<Integer> occupantsId);

        void showDialogError(Throwable throwable);

        void setRlUsersAccessibility(boolean enable);
    }

    interface Model {
        Observable<DialogsResponse> getDialogByID(String token, String dialogID);

        Observable<ItemDialog> updateDialog(String token, String dialogID, UpdateDialogRequest body);

        Observable<RealmDialogModel> getDialogFromDatabase(String dialogID);

        Observable<List<ContentModel>> getAllAvatarsFromDatabase();

        Observable<ContentModel> getDialogAvatarFromDatabase(String dialogID);

        Observable<List<LoginUser>> getAppUsersFromDatabase();

        Observable<RegistrationCreateFileResponse> createFile(String token, String mime, String fileName);

        Observable<Void> declareFileUploaded(long size, String token, long blobId);

        Observable<Void> uploadFileMap(Map<String, RequestBody> map, MultipartBody.Part part);

    }
}
