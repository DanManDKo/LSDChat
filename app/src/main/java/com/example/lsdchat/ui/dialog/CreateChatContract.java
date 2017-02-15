package com.example.lsdchat.ui.dialog;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Button;
import android.widget.EditText;

import com.example.lsdchat.api.dialog.model.ItemDialog;
import com.example.lsdchat.api.dialog.request.CreateDialogRequest;
import com.example.lsdchat.api.dialog.response.UserListResponse;
import com.example.lsdchat.api.registration.response.RegistrationCreateFileResponse;
import com.example.lsdchat.model.ContactsModel;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;

public interface CreateChatContract {

    interface View {

        boolean isRbPublic();
        boolean isRbPrivate();

        void setNameError();
        void hideNameError();

        void getUserpicUri(Uri uri);
        void showDialogImageSourceChooser();

        Context getContext();

        String getChatName();

        void initAdapter();
    }

    interface Presenter {
        void onDestroy();

        void btnCreateClick(Button btnCreate, EditText etName);
        void btnImageClick(SimpleDraweeView imageView);

        void onActivityResult(int requestCode, int resultCode, Intent data);

        void getPhotoFromGallery();
        void getPhotoFromCamera();

        void createDialog(String token,CreateDialogRequest request);

        CreateDialogRequest getTypeDialog(String nameDialog);

        void getContactsModel();

        List<ContactsModel> getListContactsModel();

        String getToken();

    }

    interface Model {
        Observable<ItemDialog> createDialog(String token, CreateDialogRequest createDialogRequest);

        Observable<RegistrationCreateFileResponse> createFile(String token, String mime, String fileName);

        Observable<Void> declareFileUploaded(long size, String token, long blobId);

        Observable<Void> uploadFileMap(Map<String, RequestBody> map, MultipartBody.Part part);

        Observable<UserListResponse> getUserList(String token);


        void insertToDb(ContactsModel contactsModel);

        List<ContactsModel> getContactsModel();

        Observable<File> downloadImage(long blobId, String token);

        Observable<File> saveImage(Response<ResponseBody> response,long blobId);

        void clear();
    }
}
