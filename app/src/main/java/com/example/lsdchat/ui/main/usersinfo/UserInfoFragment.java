package com.example.lsdchat.ui.main.usersinfo;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lsdchat.R;
import com.example.lsdchat.api.login.model.LoginUser;
import com.example.lsdchat.ui.main.fragment.BaseFragment;

import java.io.Serializable;


public class UserInfoFragment extends BaseFragment implements UserInfoContract.View{
    private FloatingActionButton mFabMessage;
    private TextView mEmail;
    private TextView mPhone;
    private TextView mWebsite;
    private Toolbar mToolbar;
    private static final String LIST= "list";
    private LoginUser mLoginUser;
    private ImageView mImageUser;
    private RelativeLayout mRlPhone;
    private RelativeLayout mRlEmail;
    private RelativeLayout mRlWebsite;
    private UserInfoContract.Presenter mPresenter;


    public UserInfoFragment() {
        // Required empty public constructor
    }

    public UserInfoFragment newInstance(LoginUser user) {
        UserInfoFragment userInfoFragment = new UserInfoFragment();
        Bundle bundle  = new Bundle();
        bundle.putSerializable(LIST,(Serializable) user);
        userInfoFragment.setArguments(bundle);
        return userInfoFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users_info, container, false);
        mPresenter = new UserInfoPresenter(this);
        mLoginUser = (LoginUser) getArguments().getSerializable(LIST);
        initView(view);

        mEmail.setText(mLoginUser.getEmail());
        mPhone.setText(mLoginUser.getPhone());
        mWebsite.setText(mLoginUser.getWebsite());



        return view;
    }

    private void initView(View view) {
        mFabMessage = (FloatingActionButton) view.findViewById(R.id.fab_user_info);
        mEmail = (TextView) view.findViewById(R.id.user_info_email);
        mPhone = (TextView) view.findViewById(R.id.user_info_phone);
        mWebsite = (TextView) view.findViewById(R.id.user_info_web);
        mToolbar = (Toolbar) view.findViewById(R.id.user_toolbar);
        mImageUser = (ImageView) view.findViewById(R.id.user_info_image);
        mRlEmail = (RelativeLayout) view.findViewById(R.id.user_info_email_rl);
        mRlPhone = (RelativeLayout) view.findViewById(R.id.user_info_phone_rl);
        mRlWebsite = (RelativeLayout) view.findViewById(R.id.user_info_website_rl);

        initToolbar(mToolbar,mLoginUser.getFullName());
        mToolbar.setNavigationOnClickListener(v -> getActivity().onBackPressed());
    }


}
