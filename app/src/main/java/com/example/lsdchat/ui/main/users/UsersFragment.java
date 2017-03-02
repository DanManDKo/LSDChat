package com.example.lsdchat.ui.main.users;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.lsdchat.App;
import com.example.lsdchat.R;
import com.example.lsdchat.api.login.model.LoginUser;
import com.example.lsdchat.constant.ApiConstant;
import com.example.lsdchat.ui.main.fragment.BaseFragment;

import java.util.List;


public class UsersFragment extends BaseFragment implements UsersContract.View {
    private UsersContract.Presenter mPresenter;
    private Toolbar mToolbar;
    private RecyclerView mRealmRecyclerView;
    private UsersRvAdapter mUsersRvAdapter;


    public UsersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        mPresenter = new UsersPresenter(this, App.getSharedPreferencesManager(getActivity()));
        mToolbar = (Toolbar) view.findViewById(R.id.chats_toolbar);
        mRealmRecyclerView = (RecyclerView) view.findViewById(R.id.realm_recycler_view);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        initToolbar(mToolbar, "Friends");

        mPresenter.getUserList();

        mRealmRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        initAdapter(mPresenter.getUsersQuickList(ApiConstant.SORT_CREATE_AT));
        return view;
    }

    private void initAdapter(List<LoginUser> list) {
        mUsersRvAdapter = new UsersRvAdapter(list, mPresenter);
        mRealmRecyclerView.setAdapter(mUsersRvAdapter);
        mUsersRvAdapter.notifyDataSetChanged();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.friends_option_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.sort_clear:
                initAdapter(mPresenter.getUsersQuickList(ApiConstant.SORT_CREATE_AT));
                break;
            case R.id.sort_name_asc:
                initAdapter(mPresenter.getUsersQuickList(ApiConstant.SORT_NAME_ACS));
                break;
            case R.id.sort_name_desc:
                initAdapter(mPresenter.getUsersQuickList(ApiConstant.SORT_NAME_DESC));
                break;
            case R.id.search:

                break;
            default:
                break;
        }

        return false;
    }

}
