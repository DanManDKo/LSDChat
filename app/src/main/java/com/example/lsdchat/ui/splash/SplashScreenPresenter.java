package com.example.lsdchat.ui.splash;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;

import com.example.lsdchat.App;
import com.example.lsdchat.R;
import com.example.lsdchat.manager.DataManager;
import com.example.lsdchat.manager.SharedPreferencesManager;
import com.example.lsdchat.model.User;
import com.example.lsdchat.util.Network;


/**
 * Created by User on 21.01.2017.
 */

public class SplashScreenPresenter implements SplashContract.Presenter {
    public final static String TOKEN_TAG = "token";
    public final static String ERROR_TAG = "rxError";
    private static int SPLASH_TIME_OUT = 3000;
    private SplashContract.View mView;
    private SplashContract.Model mModel;
    private Context mContext;
    private DataManager mDataManager;
    private User mUser;
    private boolean mNavigateToMain;
    private SharedPreferencesManager mSharedPreferencesManager;

    public SplashScreenPresenter(SplashContract.View view, SharedPreferencesManager sharedPreferencesManager) {
        mView = view;
        mModel = new SplashModel();
        mContext = mView.getContext();
        mDataManager = App.getDataManager();
        mUser = mDataManager.getUser();
        mSharedPreferencesManager = sharedPreferencesManager;
    }

    @Override
    public void leaveSplashScreen() {

        if (!isOnLine()) {
            showErrorDialog();
            return;
        }

        new Handler().postDelayed(() -> {
            if (mNavigateToMain) {
                mView.navigateToMain();
                ((Activity) mContext).finish();
            } else {
                mView.navigateToLogin();
                ((Activity) mContext).finish();
            }
        }, SPLASH_TIME_OUT);

        if (isLogged()) {
            requestSessionAndLogin(mUser.getEmail(), mUser.getPassword());
            mNavigateToMain = true;
        }
    }

    @Override
    public void requestSessionAndLogin(String email, String password) {

        mModel.getSessionAuth(email, password)
                .subscribe(sessionResponse -> {
                    getLoginWithToken(email, password, sessionResponse.getSession().getToken());
                    Log.e(TOKEN_TAG, sessionResponse.getSession().getToken());
                }, throwable -> Log.e(ERROR_TAG, throwable.getMessage()));
    }

    private void getLoginWithToken(String email, String password, String token) {
        mModel.getLogin(email, password, token)
                .subscribe(loginUser -> {
                mSharedPreferencesManager.saveToken(token);
                        },
                        throwable -> {

                        });
    }

    @Override
    public boolean isOnLine() {
        return Network.isOnline(mContext);
    }

    @Override
    public void onDestroy() {
        mView = null;
        mModel = null;
    }

    @Override
    public boolean isLogged() {
        if (mUser != null)
            return mUser.isSignIn();
        return false;
    }


    private void showErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(mContext.getString(R.string.network_is_absent))
                .setMessage(mContext.getString(R.string.check_connection))
                .setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((Activity) mContext).finish();
                    }
                })
                .setPositiveButton(mContext.getString(R.string.retry), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        leaveSplashScreen();
                    }
                }).setCancelable(false).create().show();

    }


}
