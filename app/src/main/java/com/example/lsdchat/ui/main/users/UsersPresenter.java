package com.example.lsdchat.ui.main.users;


import android.support.v7.widget.SearchView;
import android.util.Log;
import android.widget.RelativeLayout;

import com.example.lsdchat.api.dialog.model.ItemUser;
import com.example.lsdchat.api.login.model.LoginUser;
import com.example.lsdchat.constant.ApiConstant;
import com.example.lsdchat.manager.SharedPreferencesManager;
import com.example.lsdchat.ui.main.usersinfo.UserInfoFragment;
import com.example.lsdchat.util.Utils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import rx.Observable;

public class UsersPresenter implements UsersContract.Presenter {
    private UsersContract.View mView;
    private SharedPreferencesManager mSharedPreferencesManager;
    private UsersContract.Model mModel;


    public UsersPresenter(UsersContract.View mView, SharedPreferencesManager sharedPreferencesManager) {
        this.mView = mView;
        this.mSharedPreferencesManager = sharedPreferencesManager;
        mModel = new UsersModel();

    }


    @Override
    public String getToken() {
        return mSharedPreferencesManager.getToken();
    }

    @Override
    public void getUserList() {
        mModel.getUserList(getToken())
                .subscribe(userListResponse -> {
                    List<ItemUser> itemUsers = userListResponse.getItemUserList();

                    if (userListResponse.getTotalEntries() < (mModel.getUsersQuick().size() + 2)) {
                        mModel.deleteAllUSerQiuck();
                        getU(itemUsers);
                    } else
                        getU(itemUsers);

                }, throwable -> {
                    Log.e("getUserList-error", throwable.getMessage());
                });

    }


    private void getU(List<ItemUser> itemUsers) {
        Observable.from(itemUsers)
                .flatMap(user -> Observable.just(user.getUser()))
                .subscribe(loginUser -> {

                    mModel.insetUsersQuick(loginUser);

                }, throwable -> {
                    Log.e("getUserList-error", throwable.getMessage());
                });
    }


    @Override
    public void setImageView(CircleImageView imageView, long blobId) {
        if (blobId != 0) {
            Utils.downloadContent(blobId, getToken())
                    .flatMap(contentResponse -> Observable.just(contentResponse.getItemContent().getImageUrl()))
                    .subscribe(imageUrl -> {
                        Utils.downloadImageToView(imageUrl,imageView);
                    }, throwable -> {
                        Log.e("IMAGE-error", throwable.getMessage());
                    });

        }


    }


    @Override
    public void setOnQueryTextListener(SearchView searchView, UsersRvAdapter adapter) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                List<LoginUser> loginUserList = filter(getUsersQuickList(ApiConstant.SORT_CREATE_AT), query);
                if (loginUserList.size() > 0) {
                    adapter.setFilter(loginUserList);
                    return true;
                } else {
                    mView.showToast("Not Found");
                    return false;
                }
            }
        });
    }

    private List<LoginUser> filter(List<LoginUser> list, String query) {
        query = query.toLowerCase();
        List<LoginUser> filterList = new ArrayList<>();
        for (LoginUser user : list) {
            String name = user.getFullName().toLowerCase();
            if (name.contains(query)) {
                filterList.add(user);
            }
        }

        mView.initAdapter(filterList);

        return filterList;
    }

    @Override
    public void setOnClickListenerRl(RelativeLayout relativeLayout,LoginUser loginUser) {
        relativeLayout.setOnClickListener(v -> {
            mView.navigateToInfoUser(new UserInfoFragment().newInstance(loginUser));
        });
    }

    @Override
    public List<LoginUser> getUsersQuickList(String sort) {
        return mModel.getUsersQuickList(sort);
    }
}
