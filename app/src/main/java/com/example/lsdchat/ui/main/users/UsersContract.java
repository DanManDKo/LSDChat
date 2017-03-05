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
        Observable<UserListResponse> getUserList(String token);

        RealmResults<LoginUser> getUsersQuick();

        void insetUsersQuick(LoginUser userQuick);


        void deleteAllUSerQiuck();

        Observable<File> downloadImage(LoginUser loginUser, String token);

        List<LoginUser> getUsersQuickList(String sort);
        List<LoginUser> getUsersQuickList();

    }

    interface View {
        void updateAdapter();
        Context getContext();
        void showToast(String text);

        void initAdapter(List<LoginUser> list);

        void navigateToInfoUser(Fragment fragment);
    }

    interface Presenter {
        void setOnClickListenerRl(RelativeLayout relativeLayout, LoginUser loginUser);

        void setOnQueryTextListener(SearchView searchView, UsersRvAdapter adapter);

        String getToken();

        List<LoginUser> getUsersQuickList(String sort);

        void getUserList();

        void setImageView(CircleImageView imageView, LoginUser loginUser);


    }

}
