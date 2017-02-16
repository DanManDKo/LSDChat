package com.example.lsdchat.ui.dialog;


import android.os.Environment;

import com.example.lsdchat.App;
import com.example.lsdchat.api.dialog.DialogService;
import com.example.lsdchat.api.dialog.model.ItemDialog;
import com.example.lsdchat.api.dialog.request.CreateDialogRequest;
import com.example.lsdchat.api.dialog.response.UserListResponse;
import com.example.lsdchat.api.registration.request.RegistrationCreateFileRequest;
import com.example.lsdchat.api.registration.request.RegistrationCreateFileRequestBlob;
import com.example.lsdchat.api.registration.request.RegistrationDeclaringRequest;
import com.example.lsdchat.api.registration.request.RegistrationDeclaringRequestSize;
import com.example.lsdchat.api.registration.response.RegistrationCreateFileResponse;
import com.example.lsdchat.api.registration.service.RegistrationService;
import com.example.lsdchat.manager.DataManager;
import com.example.lsdchat.model.ContactsModel;
import com.example.lsdchat.util.Utils;

import java.io.File;
import java.util.List;
import java.util.Map;

import io.realm.internal.IOException;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CreateChatModel implements CreateChatContract.Model {
    private DialogService mDialogService;
    private RegistrationService mRegistrationService;
    private DataManager mDataManager;

    public CreateChatModel() {
        mDialogService = App.getApiManager().getDialogService();
        mRegistrationService = App.getApiManager().getRegistrationService();
        mDataManager = App.getDataManager();
    }


    @Override
    public Observable<ItemDialog> createDialog(String token, CreateDialogRequest createDialogRequest) {

        return mDialogService.createDialog(token, createDialogRequest)
                .subscribeOn(Schedulers.io())
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
        return mRegistrationService.uploadFileWithPartMap(map, part)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public Observable<UserListResponse> getUserList(String token) {
        return mDialogService.getUserList(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }


    @Override
    public void insertToDb(ContactsModel contactsModel) {
        mDataManager.insertCM(contactsModel);
    }

    @Override
    public List<ContactsModel> getContactsModel() {
        return mDataManager.getContactsModel();
    }

    @Override
    public void clear(){
        mDataManager.clearContactsModel();
    }

    @Override
    public Observable<File> downloadImage(long blobId, String token) {
        return mDialogService.downloadImage(blobId, token)
                .flatMap(responseBodyResponse -> saveImage(responseBodyResponse,blobId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    @Override
    public Observable<File> saveImage(Response<ResponseBody> response,long blobId) {
        return Observable.create(subscriber -> {
            try {

                String fileName = String.valueOf(blobId) + ".jpg";
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsoluteFile(), fileName);
                BufferedSink sink = Okio.buffer(Okio.sink(file));

                sink.writeAll(response.body().source());
                sink.close();
                subscriber.onNext(file);
                subscriber.onCompleted();
            } catch (IOException e) {
                e.printStackTrace();
                subscriber.onError(e);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        });
    }



}
