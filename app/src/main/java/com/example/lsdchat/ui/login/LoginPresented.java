package com.example.lsdchat.ui.login;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.example.lsdchat.manager.DataManager;
import com.example.lsdchat.model.User;
import com.quickblox.auth.QBAuth;
import com.quickblox.auth.model.QBSession;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;


public class LoginPresented implements LoginContract.Presented {
    private LoginContract.View mView;
    private Context mContext;
    private DataManager mDataManager = new DataManager();

    public LoginPresented(LoginContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void validateCredentials(String email, String password) {
        mView.showProgressBar();
        if (validationData(email, password)) {
            signIn(email, password);
        }

    }

    //    validation email and password
    private boolean validationData(String email, String password) {
        return validEmail(email) && validPassword(password);
    }


    private boolean validPassword(String password) {
        if (!TextUtils.isEmpty(password) && !(password.length() < 8)) {
            mView.hidePasswordError();
            return true;
        } else {
            mView.setPasswordError();
            return false;
        }
    }

    private boolean validEmail(String email) {
        if (!TextUtils.isEmpty(email) && email.contains("@")) {
            mView.hideEmailError();
            return true;
        } else {
            mView.setEmailError();
            return false;
        }
    }

    private void signIn(final String email, final String password) {
        QBUser qbUser = new QBUser();
        qbUser.setEmail(email);
        qbUser.setPassword(password);

        QBUsers.signIn(qbUser, new QBEntityCallback<QBUser>() {
            @Override
            public void onSuccess(QBUser user, Bundle params) {

                createSession(qbUser);

                addUserToDb(email, password, mView.isKeepSignIn());

                mView.hideProgressBar();
            }

            @Override
            public void onError(QBResponseException errors) {
                mView.hideProgressBar();
//                TODO: add error message
            }
        });
    }

    //    add current user to db
    private void addUserToDb(String email, String password, boolean isKeepSignIn) {
        User currentUser = new User();
        currentUser.setEmail(email);
        currentUser.setPassword(password);
        currentUser.setSignIn(isKeepSignIn);
        mDataManager.addToRealm(currentUser);
    }

    // create session, getToken a successful auth and add token to db
    private void createSession(QBUser user) {
        QBAuth.createSession(user, new QBEntityCallback<QBSession>() {
            @Override
            public void onSuccess(QBSession qbSession, Bundle bundle) {

//              add token to db
                mDataManager.addToken(qbSession.getToken());


                mView.navigateToMainScreen();

            }

            @Override
            public void onError(QBResponseException e) {
                mView.hideProgressBar();
                //                TODO: add error message

            }
        });
    }


    @Override
    public void attachView(LoginContract.View view) {
        mView = view;
        mContext = mView.getContext();
    }

    @Override
    public void detachView() {
        mView = null;
    }
}
