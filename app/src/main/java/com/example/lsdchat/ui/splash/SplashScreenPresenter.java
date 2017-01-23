package com.example.lsdchat.ui.splash;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.lsdchat.App;
import com.example.lsdchat.R;
import com.example.lsdchat.manager.ApiManager;
import com.example.lsdchat.manager.SharedPreferencesManager;
import com.example.lsdchat.util.HmacSha1Signature;
import com.example.lsdchat.util.Network;


/**
 * Created by User on 21.01.2017.
 */

public class SplashScreenPresenter implements SplashContract.Presenter {
    private SplashContract.View mView;
    private static int SPLASH_TIME_OUT = 3000;
    private Context mContext;
    private ApiManager mApiManager;
    private SharedPreferencesManager mSharedPreferencesManager;
    public static final String SIGNATURE_ERROR = "signature_error";

    @Override
    public void leaveSplashScreen() {

        if (!isOnLine()) {
            showErrorDialog();
            return;
        }
        String signature;
        try {
            signature = HmacSha1Signature.calculateRFC2104HMAC();
        } catch (Exception ex) {
            Log.e(SIGNATURE_ERROR, ex.getMessage());
            return;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mSharedPreferencesManager.isLogged()) {
                    navigateToMain();
                } else {
                    navigateToLogin();
                }

            }
        }, SPLASH_TIME_OUT);


    }

    @Override
    public boolean isOnLine() {
        return Network.isOnline(mContext);
    }

    @Override
    public boolean isLogged() {
        return mSharedPreferencesManager.isLogged();
    }

    @Override
    public void navigateToLogin() {
        Toast.makeText(mContext, "redirect to login", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToMain() {
        Toast.makeText(mContext, "redirect to main", Toast.LENGTH_SHORT).show();
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
        mSharedPreferencesManager = App.getSharedPreferencesManager(mContext);
    }

    @Override
    public void detachView() {
        mView = null;
    }

}
