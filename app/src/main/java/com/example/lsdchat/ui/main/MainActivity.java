package com.example.lsdchat.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.lsdchat.App;
import com.example.lsdchat.R;
import com.example.lsdchat.ui.BaseActivity;
import com.example.lsdchat.ui.dialog.CreateChatActivity;
import com.example.lsdchat.ui.login.LoginActivity;
import com.example.lsdchat.ui.main.users.UsersFragment;
import com.facebook.drawee.view.SimpleDraweeView;


public class MainActivity extends BaseActivity implements MainContract.View {
    private Toolbar mToolbar;
    private FloatingActionButton mFloatingActionButton;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private MainContract.Presenter mPresenter;
    private FrameLayout mFrameLayout;
    private View mHeaderLayout;
    private SimpleDraweeView mHeaderImage;
    private TextView mHeaderName;
    private TextView mHeaderEmail;

    @Override

    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter = new MainPresenter(this,App.getSharedPreferencesManager(this));
        initView();
        initToolbar();

        mPresenter.fabClick(mFloatingActionButton);


        mPresenter.setNavigationItemSelectedListener(mNavigationView);


    }

    @Override
    public void startNewChat() {
        startActivity(new Intent(this, CreateChatActivity.class));
    }

    private void initToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("Chats");
        }
        //change back arrow icon here
        mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.chats_toolbar);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.chats_fab);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.chats_drawer);
        mNavigationView = (NavigationView) findViewById(R.id.chats_nav);
        mFrameLayout = (FrameLayout) findViewById(R.id.fragment);

//        mHeaderLayout = mNavigationView.inflateHeaderView(R.layout.navigation_drawer_header);
        mHeaderLayout = mNavigationView.getHeaderView(0);
        mHeaderImage = (SimpleDraweeView) mHeaderLayout.findViewById(R.id.nav_view_avatar);
        mHeaderName = (TextView) mHeaderLayout.findViewById(R.id.nav_view_full_name);
        mHeaderEmail = (TextView) mHeaderLayout.findViewById(R.id.nav_view_email_address);

        mPresenter.setHeaderData(mHeaderImage,mHeaderName,mHeaderEmail);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //creating search icon at the right side of toolbar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_options_menu, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(Gravity.LEFT);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

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
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
    }

    @Override
    public void showMessageError(Throwable throwable) {
        dialogError(throwable);
    }
}
