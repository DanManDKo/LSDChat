package com.example.lsdchat.ui.main.users;


import android.support.v4.app.Fragment;

import com.example.lsdchat.api.dialog.response.UserListResponse;
import com.example.lsdchat.api.login.model.LoginUser;
import com.example.lsdchat.model.ContentModel;

import java.util.List;

import io.realm.RealmResults;
import rx.Observable;

public interface UsersContract {

    interface Model {
        String getToken();

        Observable<UserListResponse> getUserList(String token);

        RealmResults<LoginUser> getUsersQuick();


        void insetUsersQuick(LoginUser userQuick);

        void deleteAllUSerQuick();


        Observable<List<LoginUser>> getUserObservable();

        Observable<List<LoginUser>> getUserObservable(String sort);

        Observable<List<ContentModel>> getObservableUserAvatar();

    }

    interface View {

        void setListUsers(List<LoginUser> list);

        void navigateToInfoUser(Fragment fragment);

        void setContentModelList(List<ContentModel> contentModelList);
    }

    interface Presenter {
        void setClickUser(LoginUser loginUser);
        void onDestroy();
        void getUserList();

        void getUserFilterList(String query);

        void setSortedList(String sort);

        void getContentModelList();
    }

}
