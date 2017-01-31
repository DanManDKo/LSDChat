package com.example.lsdchat.ui.login;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lsdchat.manager.DataManager;
import com.example.lsdchat.model.User;
import com.example.lsdchat.service.NotifyService;
import com.example.lsdchat.util.Email;
import com.example.lsdchat.util.ErrorsCode;
import com.example.lsdchat.util.Network;
import com.jakewharton.rxbinding.widget.RxTextView;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;


public class LoginPresenter implements LoginContract.Presenter {
    private LoginContract.View mView;
    private LoginContract.Model mModel;
    private DataManager mDataManager;

    public LoginPresenter(LoginContract.View mView, DataManager dataManager) {
        this.mView = mView;
        this.mDataManager = dataManager;

        mModel = new LoginModel();

    }

    @Override
    public void onDestroy() {
        this.mDataManager = null;
        this.mView = null;
        this.mModel = null;
    }

    @Override
    public void onResume() {
        stopService(mView.getContext());
    }

    @Override
    public void onPause() {
        startService(mView.getContext());
    }


    @Override
    public void validateCredentials(EditText etEmail, EditText etPassword) {

        Observable<CharSequence> emailSubscription = RxTextView.textChanges(etEmail);
        emailSubscription.filter(charSequence -> charSequence.toString().length() != 0)
                .subscribe(value -> {
                    if (!Email.checkEmail(value)) {
                        mView.setEmailError();
                    } else {
                        mView.hideEmailError();
                    }
                });
        Observable<CharSequence> passwordSubscription = RxTextView.textChanges(etPassword);
        passwordSubscription.filter(charSequence -> charSequence.toString().length() != 0)
                .subscribe(value -> {
                    if (!isValidPassword(value)) {
                        mView.setPasswordError();
                    } else {
                        mView.hidePasswordError();
                    }
                });
        Observable.combineLatest(emailSubscription, passwordSubscription, (email, password) -> {
            boolean emailIs = Email.checkEmail(email.toString());
            boolean passIs = isValidPassword(password.toString());
            return emailIs && passIs;
        })
                .subscribe(aBoolean -> {
                    mView.setLoginButtonEnabled(aBoolean);

                });

    }


    @Override
    public void requestSessionAndLogin(String email, String password) {


        mModel.getSessionAuth(email, password)
                .doOnRequest(request -> mView.showProgressBar())
                .doOnUnsubscribe(() -> mView.hideProgressBar())
                .doOnNext(sessionResponse -> getLoginWithToken(email, password, sessionResponse.getSession().getToken()))
                .subscribe(sessionResponse -> {
                            addUserToDb(email, password, mView.isKeepSignIn());
                            Log.e("AAA", "TOKEN  - " + sessionResponse.getSession().getToken());
//                            save token

                        },
                        // TODO: 28.01.2017 [Code Review] add proper error handling logic
                        throwable -> {
                            Log.e("11111", throwable.getMessage());
                            dialogError(throwable);


                        }
                );
    }

    @Override
    public boolean isOnline() {
        if (!Network.isOnline(mView.getContext())) {
            Network.showErrorConnectDialog(mView.getContext());
            return false;
        } else return true;
    }

    private void dialogError(Throwable throwable) {
        String title = throwable.getMessage();
        String message = ErrorsCode.getErrorMessage(mView.getContext(), throwable);

        new AlertDialog.Builder(mView.getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss())
                .setCancelable(false)
                .create().show();
    }

    private void getLoginWithToken(String email, String password, String token) {
        mModel.getLogin(email, password, token)
                .doOnNext(loginResponse -> mView.navigateToMainScreen())
                .subscribe(loginUser -> {
                            //                    save Model users
                            Log.e("AAA", "id  - " + loginUser.getLoginUser().getId() + " phone - " + loginUser.getLoginUser().getPhone());
                        },
                        // TODO: 28.01.2017 [Code Review] add proper error handling logic
                        throwable -> {
                            Log.e("22222", String.valueOf(((HttpException) throwable).response().body()));

                            dialogError(throwable);

                        });
    }

    //    add current user to db
    private void addUserToDb(String email, String password, boolean isKeepSignIn) {
        User currentUser = new User();
        currentUser.setEmail(email);
        currentUser.setPassword(password);
        currentUser.setSignIn(isKeepSignIn);
        mDataManager.insertUser(currentUser);
    }


    @Override
    public boolean isValidPassword(CharSequence password) {
        return !TextUtils.isEmpty(password) && (password.toString().length() > 7);
    }

    @Override
    public void btnSignInClick(Button btnSignIn, EditText etEmail, EditText etPassword) {
        btnSignIn.setOnClickListener(view -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            if (isEmptyFields(email, password)) {
                if (isOnline()) {
                    requestSessionAndLogin(email, password);
                }
            }
        });

    }


    private Boolean isEmptyFields(String email, String password) {
        return !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password);

    }

    @Override
    public void btnSignUpClick(Button btnSignUp) {
        btnSignUp.setOnClickListener(view -> mView.navigateToRegistration());

    }

    @Override
    public void btnSignForgotPasswordClick(TextView btnForgotPassword) {
        btnForgotPassword.setOnClickListener(view -> mView.navigateToForgotPassword());
    }

    @Override
    public void startService(Context context) {
        context.startService(new Intent(context, NotifyService.class));
    }

    @Override
    public void stopService(Context context) {
        context.stopService(new Intent(context, NotifyService.class));
    }

}
