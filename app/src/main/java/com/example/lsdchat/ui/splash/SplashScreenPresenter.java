package com.example.lsdchat.ui.splash;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.Log;

import com.example.lsdchat.App;
import com.example.lsdchat.R;
import com.example.lsdchat.constant.ApiConstant;
import com.example.lsdchat.manager.ApiManager;
import com.example.lsdchat.manager.DataManager;
import com.example.lsdchat.model.SessionRequestBody;
import com.example.lsdchat.model.SessionResponse;
import com.example.lsdchat.model.User;
import com.example.lsdchat.util.HmacSha1Signature;
import com.example.lsdchat.util.Network;

import retrofit2.Response;
import rx.Subscriber;


/**
 * Created by User on 21.01.2017.
 */

public class SplashScreenPresenter implements SplashContract.Presenter {
    private SplashContract.View mView;
    private static int SPLASH_TIME_OUT = 3000;
    private Context mContext;
    private ApiManager mApiManager;
    public static final String SIGNATURE_ERROR = "signature_error";
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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mNavigationFlag) {
                    mView.navigateToMain();
                } else {
                    mView.navigateToLogin();
                }
            }
        }, SPLASH_TIME_OUT);
        if (isLogged()) {
            mSignature = getSignature(mUser.getEmail(), mUser.getPassword());
            getSession(createRequestBody());
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


    private SessionRequestBody createRequestBody() {
        SessionRequestBody body = new SessionRequestBody();
        body.setApplicationId(ApiConstant.APP_ID);
        body.setAuthKey(ApiConstant.AUTH_KEY);
        body.setSignature(mSignature);
        body.setEmail(mUser.getEmail());
        body.setPassword(mUser.getPassword());
        body.setNonce(mRandom);
        body.setTimestamp(mTimestamp);
        return body;
    }

    @Override
    public void getSession(SessionRequestBody body) {
        App.getApiManager().getSession(body).subscribe(new Subscriber<Response<SessionResponse>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Response<SessionResponse> sessionResponseResponse) {
                SessionResponse session = sessionResponseResponse.body();
                App.getDataManager().insertSession(session);
            }
        });
    }

    @Override
    public String getSignature(String user, String password) {

        String signature;
        try {
            signature = HmacSha1Signature.calculateRFC2104HMAC(user, password);
            mRandom = HmacSha1Signature.getRandom();
            mTimestamp = HmacSha1Signature.getTimestamp();
        } catch (Exception ex) {
            Log.e(SIGNATURE_ERROR, ex.getMessage());
            return null;
        }
        return signature;
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
