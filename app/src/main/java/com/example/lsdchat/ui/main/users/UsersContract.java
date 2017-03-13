package com.example.lsdchat.ui.main.users;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.widget.RelativeLayout;

import com.example.lsdchat.api.dialog.response.UserListResponse;
import com.example.lsdchat.api.login.model.LoginUser;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.RealmResults;
import rx.Observable;

public interface UsersContract {

    interface Model {
        String getToken();

        Observable<UserListResponse> getUserList(String token);

        RealmResults<LoginUser> getUsersQuick();

        void insetUsersQuick(LoginUser userQuick);


        void deleteAllUSerQuick();


        List<LoginUser> getUsersQuickList(String sort);
        List<LoginUser> getUsersQuickList();

        Observable<List<LoginUser>> getUserObservable();

    }

    interface View {


        void showMessageError(Throwable throwable);

        void setListUsers(List<LoginUser> list);

        void navigateToInfoUser(Fragment fragment);
    }

    interface Presenter {
        void setClickUser(LoginUser loginUser);

        List<LoginUser> getUsersQuickList(String sort);
        List<LoginUser> getUsersQuickList();

        Observable<String> getImageUrl(long blobId);


    }

}
