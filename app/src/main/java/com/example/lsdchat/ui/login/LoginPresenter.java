package com.example.lsdchat.ui.login;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lsdchat.R;
import com.example.lsdchat.manager.DataManager;
import com.example.lsdchat.manager.SharedPreferencesManager;
import com.example.lsdchat.model.User;
import com.example.lsdchat.service.NotifyService;
import com.example.lsdchat.util.Email;
import com.example.lsdchat.util.Network;
import com.example.lsdchat.util.Utils;
import com.jakewharton.rxbinding.widget.RxTextView;

import rx.Observable;


public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View mView;
    private LoginContract.Model mModel;
    private DataManager mDataManager;
    private SharedPreferencesManager mSharedPreferencesManager;

    public LoginPresenter(LoginContract.View mView, DataManager dataManager,SharedPreferencesManager sharedPreferencesManager) {
        this.mView = mView;
        this.mDataManager = dataManager;
        this.mSharedPreferencesManager = sharedPreferencesManager;
        mModel = new LoginModel();

    }

    @Override
    public void onDestroy() {
        stopService(mView.getContext());

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
                            Log.e("AAA", "TOKEN  - " + sessionResponse.getSession().getToken());

                        mSharedPreferencesManager.saveToken(sessionResponse.getSession().getToken());
                        },
                        throwable -> {
                            Log.e("11111", throwable.getMessage());
                            mView.dialogError(throwable);

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


    private void getLoginWithToken(String email, String password, String token) {
        mModel.getLogin(email, password, token)
                .doOnNext(loginResponse -> mView.navigateToMainScreen())
                .subscribe(loginUser -> {
                    String fullName = loginUser.getLoginUser().getFullName();
                    long blobId = loginUser.getLoginUser().getBlobId();

                            if (blobId!=0) {
                                mModel.saveUser(new User(email,password,fullName,blobId,mView.isKeepSignIn()));
                            }
                            else {
                                mModel.saveUser(new User(email,password,fullName,mView.isKeepSignIn()));
                            }
                        },
                        throwable -> mView.dialogError(throwable));
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
            } else {
                Toast.makeText(mView.getContext(), R.string.login_presenter_error_input, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean isEmptyFields(String email, String password) {
        return !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password);

    }

    @Override
    public void btnSignUpClick(Button btnSignUp) {
        btnSignUp.setOnClickListener(view -> goToRegistrationScreen());

    }

    @Override
    public void btnSignForgotPasswordClick(TextView btnForgotPassword) {
        btnForgotPassword.setOnClickListener(view -> goToForgotPassword());
    }

    public void startService(Context context) {
        context.startService(new Intent(context, NotifyService.class));
    }

    @Override
    public void stopService(Context context) {
        context.stopService(new Intent(context, NotifyService.class));
    }

    @Override
    public void goToForgotPassword() {
        mView.showDialogForgotPassword();
    }

    @Override
    public void goToRegistrationScreen() {
        mView.navigateToRegistration();
    }
}
