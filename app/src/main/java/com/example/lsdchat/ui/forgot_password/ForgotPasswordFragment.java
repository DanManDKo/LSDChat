package com.example.lsdchat.ui.forgot_password;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lsdchat.R;

/**
 * Created by User on 30.01.2017.
 */

public class ForgotPasswordFragment extends DialogFragment implements ForgotPasswordContract.View {
    private ForgotPasswordPresenter mPresenter;
    private Button mSend;
    private Button mCancel;
    private EditText mEmailInput;
    private TextInputLayout mTextInputLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new ForgotPasswordPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle(R.string.restore_password);
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        initView(view);
        onClickButtons();
        return view;
    }

    private void onClickButtons() {
        mCancel.setOnClickListener(view -> mPresenter.onCancelClick());
        mSend.setOnClickListener(view -> mPresenter.onSendClick());
        mEmailInput.setOnClickListener(view -> mPresenter.onInputClick());
    }

    private void initView(View view) {
        mCancel = (Button) view.findViewById(R.id.btn_cancel_forgot_password);
        mSend = (Button) view.findViewById(R.id.btn_send_forgot_password);
        mEmailInput = (EditText) view.findViewById(R.id.email_input_forgot_password);
        mTextInputLayout = (TextInputLayout) view.findViewById(R.id.forgot_password_input_layout);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }


    @Override
    public void setEmailError(String error) {
        mTextInputLayout.setError(error);
    }

    @Override
    public void hideEmailError() {
        mTextInputLayout.setError(null);
    }

    @Override
    public void showEmailSuccessToast() {
        Toast.makeText(getActivity(), R.string.emai_success, Toast.LENGTH_SHORT).show();

    }




    @Override
    public String getEmail() {
        return mEmailInput.getText().toString();
    }

    @Override
    public void dismiss() {
        getDialog().dismiss();
    }

    @Override
    public Context getContext() {
        return getActivity();
    }


}
