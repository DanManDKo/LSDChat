package com.example.lsdchat.ui.main.users;


import android.os.Environment;

import com.example.lsdchat.App;
import com.example.lsdchat.api.dialog.DialogService;
import com.example.lsdchat.api.dialog.response.UserListResponse;
import com.example.lsdchat.api.login.model.LoginUser;
import com.example.lsdchat.manager.DataManager;

import java.io.File;
import java.util.List;
import java.util.Objects;

import io.realm.RealmResults;
import io.realm.internal.IOException;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UsersModel implements UsersContract.Model {
    private DialogService mDialogService;
    private DataManager mDataManager;

    public UsersModel() {
        mDataManager = App.getDataManager();
        mDialogService = App.getApiManager().getDialogService();
    }

    @Override
    public Observable<UserListResponse> getUserList(String token) {
        return mDialogService.getUserList(token, 100)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }


    @Override
    public void insetUsersQuick(LoginUser userQuick) {
//         && !Objects.equals(userQuick.getEmail(), mDataManager.getUser().getEmail())
        if (!Objects.equals(userQuick.getLogin(), "eroy"))
            mDataManager.insertUserQuickToDB(userQuick);
    }


    @Override
    public RealmResults<LoginUser> getUsersQuick() {
        return mDataManager.getUsersQuick();
    }

    @Override
    public LoginUser getUserById(int id) {
        return mDataManager.getUserById(id);
    }


    @Override
    public List<LoginUser> getUsersQuickList(String sort) {
        return mDataManager.getUsersQuickList(sort);
    }
    @Override
    public List<LoginUser> getUsersQuickList() {
        return mDataManager.getUsersQuickList();
    }

    @Override
    public void deleteAllUSerQiuck() {
        mDataManager.deleteAllUsersQuick();
    }



    @Override
    public Observable<File> downloadImage(LoginUser loginUser, String token) {
        DialogService mDialogService = App.getApiManager().getDialogService();

        return mDialogService.downloadImage(loginUser.getBlobId(), token)
                .flatMap(responseBodyResponse -> saveImage(responseBodyResponse, loginUser))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    private Observable<File> saveImage(Response<ResponseBody> response, LoginUser loginUser) {
        return Observable.create(subscriber -> {
            try {

                String fileName = String.valueOf(loginUser.getBlobId()) + ".jpg";
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
