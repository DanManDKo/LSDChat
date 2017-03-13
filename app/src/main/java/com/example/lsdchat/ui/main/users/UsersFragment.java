package com.example.lsdchat.ui.main.users;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lsdchat.App;
import com.example.lsdchat.R;
import com.example.lsdchat.api.login.model.LoginUser;
import com.example.lsdchat.constant.ApiConstant;
import com.example.lsdchat.ui.main.fragment.BaseFragment;

import java.util.ArrayList;
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
        mPresenter = new UsersPresenter(this, new UsersModel(App.getSharedPreferencesManager(getActivity())));
        mToolbar = (Toolbar) view.findViewById(R.id.chats_toolbar);
        mRealmRecyclerView = (RecyclerView) view.findViewById(R.id.realm_recycler_view);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        initToolbar(mToolbar, "Friends");
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        mRealmRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        setListUsers(mPresenter.getUsersQuickList());

        return view;
    }

    @Override
    public void setListUsers(List<LoginUser> list) {
        mUsersRvAdapter = new UsersRvAdapter(list, mPresenter, App.getDataManager().getListUserAvatar());

        mRealmRecyclerView.setAdapter(mUsersRvAdapter);
        mUsersRvAdapter.notifyDataSetChanged();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
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
                getFilterList(mPresenter.getUsersQuickList(), newText);
                return false;

            }
        });


    }

    private List<LoginUser> getFilterList(List<LoginUser> list, String query) {
        query = query.toLowerCase();
        List<LoginUser> filterList = new ArrayList<>();
        for (LoginUser user : list) {
            String name = user.getFullName().toLowerCase();
            if (name.contains(query)) {
                filterList.add(user);
            }
        }

        setListUsers(filterList);

        return filterList;
    }


    @Override
    public void showMessageError(Throwable throwable) {
        dialogError(throwable);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort_clear:
                setListUsers(mPresenter.getUsersQuickList(ApiConstant.SORT_CREATE_AT));
                break;
            case R.id.sort_name_asc:
                setListUsers(mPresenter.getUsersQuickList(ApiConstant.SORT_NAME_ACS));
                break;
            case R.id.sort_name_desc:
                setListUsers(mPresenter.getUsersQuickList(ApiConstant.SORT_NAME_DESC));
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
