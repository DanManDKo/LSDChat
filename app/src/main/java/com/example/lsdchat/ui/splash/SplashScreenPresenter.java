package com.example.lsdchat.ui.splash;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.example.lsdchat.App;
import com.example.lsdchat.R;
import com.example.lsdchat.api.ApiManager;
import com.example.lsdchat.api.request.SessionRequestNoAuth;
import com.example.lsdchat.manager.DataManager;
import com.example.lsdchat.model.User;
import com.example.lsdchat.util.HmacSha1Signature;
import com.example.lsdchat.util.Network;


/**
 * Created by User on 21.01.2017.
 */

public class SplashScreenPresenter implements SplashContract.Presenter {
    public static final String SIGNATURE_ERROR = "signature_error";
    private static int SPLASH_TIME_OUT = 3000;
    private SplashContract.View mView;
    private Context mContext;
    private ApiManager mApiManager;
    private int mRandom;
    private long mTimestamp;
    private String mSignature;
    private DataManager mDataManager;
    private User mUser;
    private boolean mNavigationFlag;

    public SplashScreenPresenter() {
        mApiManager = App.getApiManager();
        mDataManager = App.getDataManager();
        mUser = mDataManager.getUser();
    }

    @Override
    public void leaveSplashScreen() {

        if (!isOnLine()) {
            showErrorDialog();
            return;
        }

        new Handler().postDelayed(() -> {
            if (mNavigationFlag) {
                mView.navigateToMain();
            } else {
                mView.navigateToLogin();
            }
        }, SPLASH_TIME_OUT);

        if (isLogged()) {
            getSession();
            mNavigationFlag = true;
        }
    }

    @Override
    public boolean isOnLine() {
        return Network.isOnline(mContext);
    }

    @Override
    public boolean isLogged() {
        if (mUser != null)
            return mUser.isSignIn();
        return false;
    }


    @Override
    public void getSession() {
        App.getApiManager().getSessionNoAuth(new SessionRequestNoAuth())
                .subscribe(sessionResponse -> {
                            Log.e("AAA", "TOKEN  - " + sessionResponse.getSession().getToken());
                        },
                        throwable -> {
                            //                            error
                            Log.e("AAA", throwable.getMessage());
                        });


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

    @Override
    public void attachView(SplashContract.View view) {
        mView = view;
        mContext = mView.getContext();
    }

    @Override
    public void detachView() {
        mView = null;
    }

}
