package com.example.lsdchat.ui.login;

import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import com.example.lsdchat.manager.DataManager;
import com.example.lsdchat.model.User;
import com.jakewharton.rxbinding.widget.RxTextView;

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
    public void validateCredentials(EditText etEmail, EditText etPassword) {

        Observable<CharSequence> emailSubscription = RxTextView.textChanges(etEmail);
        emailSubscription.filter(charSequence -> charSequence.toString().length() != 0)
                .subscribe(value -> {
                    if (!isValidEmail(value)) {
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
            boolean emailIs = isValidEmail(email.toString());
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
                        throwable -> Log.e("11111", throwable.getMessage())
                );
    }


    private void getLoginWithToken(String email, String password, String token) {
        mModel.getLogin(email, password, token)
                .doOnNext(loginResponse -> mView.navigateToMainScreen())
                .subscribe(loginUser -> {
                            //                    save Model users
                            Log.e("AAA", "id  - " + loginUser.getLoginUser().getId() + " phone - " + loginUser.getLoginUser().getPhone());
                        },
                        // TODO: 28.01.2017 [Code Review] add proper error handling logic
                        throwable -> Log.e("22222", throwable.getMessage()));
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
    public boolean isValidEmail(CharSequence email) {
        return email.toString().matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

    }

    @Override
    public void btnSignInClick(String email, String password) {
        if (isEmptyFields(email, password)) {
            requestSessionAndLogin(email, password);
        }
    }

    private Boolean isEmptyFields(String email, String password) {
        return !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password);

    }

    @Override
    public void btnSignUpClick() {
        mView.navigateToRegistration();
    }

    @Override
    public void btnSignForgotPasswordClick() {
        mView.showDialogForgotPassword();
    }




}
