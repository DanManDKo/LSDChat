package com.example.lsdchat.ui.dialog;


import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.lsdchat.api.dialog.request.CreateDialogRequest;
import com.example.lsdchat.constant.ApiConstant;
import com.example.lsdchat.manager.SharedPreferencesManager;

public class CreateChatPresenter implements CreateChatContract.Presenter {

    private CreateChatContract.View mView;
    private CreateChatContract.Model mModel;
    private SharedPreferencesManager mSharedPreferencesManager;

    public CreateChatPresenter(CreateChatContract.View mView, SharedPreferencesManager sharedPreferencesManager) {
        this.mView = mView;
        mModel = new CreateChatModel();
        this.mSharedPreferencesManager = sharedPreferencesManager;
    }

    @Override
    public void onDestroy() {
        mView = null;
        mModel = null;
    }

    @Override
    public void btnCreateClick(Button btnCreate, EditText etName) {

        btnCreate.setOnClickListener(view -> {
            String name = etName.getText().toString();
            createDialog(name);
        });
    }


    @Override
    public void createDialog(String nameDialog) {
        String token = mSharedPreferencesManager.getToken();

        mModel.createDialog(token, getTypeDialog(nameDialog))
                .subscribe(itemDialog -> {
                            Log.e("DIALOG", itemDialog.getId());
                            Log.e("DIALOG", itemDialog.getName());
                        },
                        throwable -> {
                            Log.e("DIALOG", throwable.getMessage());
                        });

    }

    @Override
    public CreateDialogRequest getTypeDialog(String nameDialog) {
        if (mView.isRbPublic()) {
            return new CreateDialogRequest(ApiConstant.TYPE_DIALOG_PUBLIC, nameDialog);
        } else {

            return new CreateDialogRequest(ApiConstant.TYPE_DIALOG_PUBLIC, nameDialog);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onAvatarClickListener() {

    }

    @Override
    public void getPhotoFromGallery() {

    }

    @Override
    public void getPhotoFromCamera() {

    }
}
