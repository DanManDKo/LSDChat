package com.example.lsdchat.ui.splash;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;

import com.example.lsdchat.App;
import com.example.lsdchat.R;
import com.example.lsdchat.manager.ApiManager;
import com.example.lsdchat.util.Network;

/**
 * Created by User on 21.01.2017.
 */

public class SplashScreenPresenter implements SplashContract.Presenter {
    private SplashContract.View mView;
    private static int SPLASH_TIME_OUT = 3000;
    private Context mContext;
    private ApiManager mApiManager;

    @Override
    public void leaveSplashScreen() {

        if (!isOnLine()) {
            showErrorDialog();
            return;
        }
        mApiManager = App.getApiManager();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mApiManager.isUserLogged()) {
                    navigateToMain();
                } else {
                    navigateToLogin();
                }

            }
        }, SPLASH_TIME_OUT);

        if (isLogged()) {
            navigateToMain();
        } else {
            navigateToLogin();
        }

    }

    @Override
    public boolean isOnLine() {
        return Network.isOnline(mContext);
    }

    @Override
    public boolean isLogged() {
        return false;
    }

    @Override
    public void navigateToLogin() {

    }

    @Override
    public void navigateToMain() {

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
