package com.example.lsdchat.ui.main.users;


import android.support.v7.widget.SearchView;

import com.example.lsdchat.api.dialog.response.UserListResponse;
import com.example.lsdchat.api.login.model.LoginUser;

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

        List<LoginUser> getUsersQuickList(String sort);
    }

    interface View {

        void showToast(String text);
        void initAdapter(List<LoginUser> list);
    }

    interface Presenter {

        void setOnQueryTextListener(SearchView searchView,UsersRvAdapter adapter);

        String getToken();

        List<LoginUser> getUsersQuickList(String sort);
        void getUserList();

        void setImageView(CircleImageView imageView, long blobId);
    }

}
