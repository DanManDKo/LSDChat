package com.example.lsdchat.contract;

import com.example.lsdchat.base.BaseMvpPresenter;
import com.example.lsdchat.base.BaseMvpView;

public class RegistrationContract {
    public interface Presenter extends BaseMvpPresenter<View>{
        //void submitRegForm();
        String getFbToken();
    }

    public interface View extends BaseMvpView {

        //void onError();
    }
}
