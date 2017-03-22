package com.example.lsdchat.ui.main.users;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.example.lsdchat.model.ContentModel;
import com.example.lsdchat.ui.main.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;


public class UsersFragment extends BaseFragment implements UsersContract.View {
    private UsersContract.Presenter mPresenter;
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private UsersRvAdapter mUsersRvAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<LoginUser> mLoginUserList;

    public UsersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        mLoginUserList = new ArrayList<>();

        mPresenter = new UsersPresenter(this, new UsersModel(App.getSharedPreferencesManager(getActivity()),
                        App.getDataManager(),App.getApiManager().getDialogService()));

        initView(view);

        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        initToolbar(mToolbar, "Friends");
//        mToolbar.setNavigationOnClickListener(v -> onBackPressed());

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        mUsersRvAdapter = new UsersRvAdapter(mPresenter);
        mRecyclerView.setAdapter(mUsersRvAdapter);

        mPresenter.getContentModelList();

        mPresenter.getUserList();



        return view;
    }

    @Override
    public void setContentModelList(List<ContentModel> contentModelList) {
        mUsersRvAdapter.setContentModelList(contentModelList);
    }

    private void initView(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.chats_toolbar);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.realm_recycler_view);
    }

    @Override
    public void setListUsers(List<LoginUser> list) {
        clearList();
        mUsersRvAdapter.addData(list);

    }

    private void clearList() {
        mUsersRvAdapter.clearData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//
        inflater.inflate(R.menu.friends_option_menu, menu);

        MenuItem items = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(items);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                mPresenter.getUserFilterList(newText);

                return false;

            }
        });
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_clear:
                mPresenter.setSortedList(ApiConstant.SORT_CREATE_AT);
                break;
            case R.id.sort_name_asc:
                mPresenter.setSortedList(ApiConstant.SORT_NAME_ACS);
                break;
            case R.id.sort_name_desc:
                mPresenter.setSortedList(ApiConstant.SORT_NAME_DESC);
                break;
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }

        return false;
    }

    @Override
    public void navigateToInfoUser(Fragment fragment) {
//        TODO: add animation
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).addToBackStack(null).commit();
    }
}
