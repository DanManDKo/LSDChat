package com.example.lsdchat.ui.main.usersinfo;


import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lsdchat.App;
import com.example.lsdchat.R;
import com.example.lsdchat.api.login.model.LoginUser;
import com.example.lsdchat.util.error.NetworkConnect;
import com.example.lsdchat.ui.main.fragment.BaseFragment;
import com.example.lsdchat.util.ErrorsCode;

import java.io.File;


public class UserInfoFragment extends BaseFragment implements UserInfoContract.View {
    private static final String LIST = "list";
    private FloatingActionButton mFabMessage;
    private TextView mEmail;
    private TextView mPhone;
    private TextView mWebsite;
    private Toolbar mToolbar;
    private LoginUser mLoginUser;
    private ImageView mImageUser;
    private RelativeLayout mRlPhone;
    private RelativeLayout mRlEmail;
    private RelativeLayout mRlWebsite;
    private UserInfoContract.Presenter mPresenter;
    private PackageManager mPackageManager;
    private NetworkConnect networkConnect;

    public UserInfoFragment() {
        // Required empty public constructor
    }

    public UserInfoFragment newInstance(LoginUser user) {
        UserInfoFragment userInfoFragment = new UserInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(LIST, user);
        userInfoFragment.setArguments(bundle);
        return userInfoFragment;
    }

    @Override
    public boolean isNetworkConnect() {
        return networkConnect.isNetworkConnect();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        networkConnect = ((NetworkConnect) getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users_info, container, false);
        mPresenter = new UserInfoPresenter(this,
                new UserInfoModel(App.getDataManager(),
                        App.getApiManager().getDialogService(),
                        App.getSharedPreferencesManager(getActivity())));
        mPackageManager = getActivity().getPackageManager();
        mLoginUser = getArguments().getParcelable(LIST);
        initView(view);

        fieldAccessibility(mEmail, mRlEmail, mLoginUser.getEmail());
        fieldAccessibility(mPhone, mRlPhone, mLoginUser.getPhone());
        fieldAccessibility(mWebsite, mRlWebsite, mLoginUser.getWebsite());

//        mEmail.setText(mLoginUser.getEmail());
//        mPhone.setText(mLoginUser.getPhone());
//        mWebsite.setText(mLoginUser.getWebsite());


        mPresenter.getUserAvatar(mLoginUser.getId());
        onClick();


        return view;
    }

    @Override
    public void setImagePath(String path) {
        if (path != null) {
            mImageUser.setImageURI(Uri.fromFile(new File(path)));
        }
    }

    private void onClick() {
        mFabMessage.setOnClickListener(v -> {
            mPresenter.createDialog(mLoginUser.getId());

        });
        mRlEmail.setOnClickListener(v -> navigateToSendEmail(mLoginUser.getEmail()));
        mRlPhone.setOnClickListener(v -> navigateToDial(mLoginUser.getPhone()));
        mRlWebsite.setOnClickListener(v -> navigateToWeb(mLoginUser.getWebsite()));

    }

    private void fieldAccessibility(TextView textView, RelativeLayout rl, String value) {
        if (value == null) {
            rl.setVisibility(View.GONE);
        } else {
            rl.setVisibility(View.VISIBLE);
            textView.setText(value);
        }
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

        initToolbar(mToolbar, mLoginUser.getFullName());
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }


    private void navigateToDial(String phone) {
        if (phone != null && !phone.isEmpty()) {
            Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts(
                    "tel", phone, null));
            if (mPackageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
                if (((TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE))
                        .getSimState() == TelephonyManager.SIM_STATE_READY) {
                    if (Settings.Global.getInt(getContext().getContentResolver(),
                            Settings.Global.AIRPLANE_MODE_ON, 0) == 0) {
                        startActivity(phoneIntent);
                        return;
                    }
                }
            } else {
                ErrorsCode.showErrorDialog(getActivity(), getString(R.string.user_info_error_phone));
            }
        }
    }

    private void navigateToSendEmail(String email) {
        if (email != null && !email.isEmpty()) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", email, null));
            if (emailIntent.resolveActivity(mPackageManager) != null) {
                startActivity(emailIntent);
            } else {
                ErrorsCode.showErrorDialog(getActivity(), getString(R.string.user_info_error_email));
            }
        }
    }

    private void navigateToWeb(String website) {
        if (website != null && !website.isEmpty()) {
            Intent websiteIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(website));
            if (websiteIntent.resolveActivity(mPackageManager) != null) {
                startActivity(websiteIntent);
            }
        }
    }

    @Override
    public void showDialogError(Throwable throwable) {
        dialogError(throwable);
    }

    @Override
    public void navigateToChat(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null).replace(R.id.fragment, fragment).commit();
    }
}
