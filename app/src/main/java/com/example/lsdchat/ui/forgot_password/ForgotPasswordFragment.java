package com.example.lsdchat.ui.forgot_password;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.lsdchat.R;

/**
 * Created by User on 30.01.2017.
 */

public class ForgotPasswordFragment extends DialogFragment implements ForgotPasswordContract.View {
    private ForgotPasswordPresenter mPresenter;
    private Button mSend;
    private Button mCancel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new ForgotPasswordPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(R.string.restore_password);
        return inflater.inflate(R.layout.fragmenr_forgot_password, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }


}
