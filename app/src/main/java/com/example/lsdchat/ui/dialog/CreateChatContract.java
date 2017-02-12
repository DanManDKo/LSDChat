package com.example.lsdchat.ui.dialog;


import android.content.Intent;
import android.net.Uri;
import android.widget.Button;
import android.widget.EditText;

import com.example.lsdchat.api.dialog.model.ItemDialog;
import com.example.lsdchat.api.dialog.request.CreateDialogRequest;

import rx.Observable;

public interface CreateChatContract {

    interface View {

        boolean isRbPublic();
        boolean isRbPrivate();

        void setNameError();
        void hideNameError();

        void getUserpicUri(Uri uri);
        void showDialogImageSourceChooser();
    }

    interface Presenter {
        void onDestroy();

        void btnCreateClick(Button btnCreate, EditText etName);

        void onActivityResult(int requestCode, int resultCode, Intent data);
        void onAvatarClickListener();
        void getPhotoFromGallery();
        void getPhotoFromCamera();

        void createDialog(String nameDialog);

        CreateDialogRequest getTypeDialog(String nameDialog);

    }

    interface Model {
        Observable<ItemDialog> createDialog(String token, CreateDialogRequest createDialogRequest);

    }
}
