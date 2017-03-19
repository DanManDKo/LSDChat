package com.example.lsdchat.ui.main.editchat;

import com.example.lsdchat.App;
import com.example.lsdchat.api.dialog.DialogService;
import com.example.lsdchat.api.dialog.model.ItemDialog;
import com.example.lsdchat.api.dialog.request.CreateDialogRequest;
import com.example.lsdchat.api.dialog.request.UpdateDialogRequest;
import com.example.lsdchat.api.dialog.response.DialogsResponse;
import com.example.lsdchat.api.dialog.response.MessagesResponse;
import com.example.lsdchat.api.login.model.LoginUser;
import com.example.lsdchat.api.login.request.LoginRequest;
import com.example.lsdchat.api.login.response.LoginResponse;
import com.example.lsdchat.api.registration.request.RegistrationCreateFileRequest;
import com.example.lsdchat.api.registration.request.RegistrationCreateFileRequestBlob;
import com.example.lsdchat.api.registration.request.RegistrationDeclaringRequest;
import com.example.lsdchat.api.registration.request.RegistrationDeclaringRequestSize;
import com.example.lsdchat.api.registration.request.UpdateRequest;
import com.example.lsdchat.api.registration.request.UpdateRequestUser;
import com.example.lsdchat.api.registration.response.RegistrationCreateFileResponse;
import com.example.lsdchat.api.registration.service.RegistrationService;
import com.example.lsdchat.manager.DataManager;
import com.example.lsdchat.model.ContentModel;
import com.example.lsdchat.model.RealmDialogModel;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EditchatModel implements EditchatContract.Model {
    private DialogService mDialogService;
    private RegistrationService mRegistrationService;
    private DataManager mDataManager;

    public EditchatModel() {
        mDialogService = App.getApiManager().getDialogService();
        mRegistrationService = App.getApiManager().getRegistrationService();
        mDataManager = App.getDataManager();
    }

    @Override
    public Observable<DialogsResponse> getDialogByID(String token, String dialogID) {
        return mDialogService.getDialog(token, dialogID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ItemDialog> updateDialog(String token, String dialogID, UpdateDialogRequest body) {
        return mDialogService.updateDialog(token, dialogID, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<RealmDialogModel> getDialogFromDatabase(String dialogID) {
        return mDataManager.getDialogByID(dialogID)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<ContentModel> getDialogAvatarFromDatabase(String dialogID) {
        return mDataManager.getObservableUserAvatar(dialogID)
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<ContentModel>> getAllAvatarsFromDatabase() {
        return mDataManager.getObservableUserAvatar()
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<List<LoginUser>> getAppUsersFromDatabase() {
        return mDataManager.getUserObservable()
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<RegistrationCreateFileResponse> createFile(String token, String mime, String fileName) {
        RegistrationCreateFileRequestBlob blob = new RegistrationCreateFileRequestBlob(mime, fileName);
        RegistrationCreateFileRequest body = new RegistrationCreateFileRequest(blob);

        return mRegistrationService.createFileRequest(token, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Void> declareFileUploaded(long size, String token, long blobId) {
        RegistrationDeclaringRequestSize fileSize = new RegistrationDeclaringRequestSize(size);
        RegistrationDeclaringRequest body = new RegistrationDeclaringRequest(fileSize);

        return mRegistrationService.declaringFileUploadedRequest(blobId, token, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<Void> uploadFileMap(Map<String, RequestBody> map, MultipartBody.Part part) {
        return mRegistrationService.uploadFileWithPartMap(map,part)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
