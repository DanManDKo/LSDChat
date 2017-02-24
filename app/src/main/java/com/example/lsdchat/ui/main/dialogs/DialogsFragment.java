package com.example.lsdchat.ui.main.dialogs;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lsdchat.App;
import com.example.lsdchat.R;
import com.example.lsdchat.ui.dialog.CreateChatActivity;
import com.example.lsdchat.ui.login.LoginActivity;
import com.example.lsdchat.ui.main.fragment.BaseFragment;
import com.example.lsdchat.ui.main.users.UsersFragment;
import com.facebook.drawee.view.SimpleDraweeView;


public class DialogsFragment extends BaseFragment implements DialogsContract.View {

    private FloatingActionButton mFloatingActionButton;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private DialogsContract.Presenter mPresenter;
    private Toolbar mToolbar;
    private View mHeaderLayout;
    private SimpleDraweeView mHeaderImage;
    private TextView mHeaderName;
    private TextView mHeaderEmail;

    public DialogsFragment() {
    }

    @Override
    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialogs,container,false);
        mPresenter = new DialogsPresenter(this,App.getSharedPreferencesManager(getActivity()));
        initView(view);
        initToolbar(mToolbar,"Chats");

        mPresenter.fabClick(mFloatingActionButton);


        mPresenter.setNavigationItemSelectedListener(mNavigationView);


        return view;
    }



    @Override
    public void startNewChat() {
        startActivity(new Intent(getActivity(), CreateChatActivity.class));
    }



    private void initView(View view) {
        mToolbar =(Toolbar) view.findViewById(R.id.chats_toolbar);
        mFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.chats_fab);
        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.chats_drawer);
        mNavigationView = (NavigationView) view.findViewById(R.id.chats_nav);
        mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);

//        mHeaderLayout = mNavigationView.inflateHeaderView(R.layout.navigation_drawer_header);
        mHeaderLayout = mNavigationView.getHeaderView(0);
        mHeaderImage = (SimpleDraweeView) mHeaderLayout.findViewById(R.id.nav_view_avatar);
        mHeaderName = (TextView) mHeaderLayout.findViewById(R.id.nav_view_full_name);
        mHeaderEmail = (TextView) mHeaderLayout.findViewById(R.id.nav_view_email_address);

        mPresenter.setHeaderData(mHeaderImage,mHeaderName,mHeaderEmail);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_options_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                Log.e("TTT","test");
                mDrawerLayout.openDrawer(Gravity.LEFT);
                break;
            default:
                break;
        }
        return false;
    }

/*

    if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
        mDrawerLayout.closeDrawers();
    } else {
        super.onDestroyView();

    }
*/

    @Override
    public void startUsers() {
        replaceFragment(new UsersFragment());
    }

    @Override
    public void startInviteUsers() {
        dialogError("kjndaskjhads");
    }

    @Override
    public void startSetting() {

    }

    @Override
    public void logOut() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }

    private void replaceFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
    }

    @Override
    public void showMessageError(Throwable throwable) {
        dialogError(throwable);
    }


    @Override
    public boolean onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawers();
            return true;
        } else {
            return false;

        }
    }
}
