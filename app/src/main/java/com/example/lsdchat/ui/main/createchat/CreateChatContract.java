package com.example.lsdchat.ui.main.createchat;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.lsdchat.api.dialog.model.ItemDialog;
import com.example.lsdchat.api.dialog.request.CreateDialogRequest;
import com.example.lsdchat.api.dialog.response.UserListResponse;
import com.example.lsdchat.api.registration.response.RegistrationCreateFileResponse;
import com.example.lsdchat.model.ContactsModel;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;

public interface CreateChatContract {

    interface View {

        void showErrorDialog(String message);

        boolean isRbPublic();
        boolean isRbPrivate();

        void setNameError();
        void hideNameError();

        void setEnableName(boolean enableName);
        void setEnableImage(boolean enableImage);

        // TODO: 3/9/17 [Code Review] pls use more abstract names like 'setUsersListAccessibility(boolean enabled)'
        // or make 2 methods 'enableUsersList' and 'disableUsersList'
        void setRecyclerEnableDisable(boolean enable);

        void getUserpicUri(Uri uri);
        // TODO: 3/9/17 [Code Review] pls use more abstract names
        void showDialogImageSourceChooser();

        Context getContext();

        String getChatName();

        // TODO: 3/9/17 [Code Review] pls use more abstract names, do not use 'Adapter' word
        void initAdapter(List<ContactsModel> list);

        // TODO: 3/9/17 [Code Review] ask someone to implement this method's logic. What should be in it?
        // I don't know, for sure
        void addModel(List<ContactsModel> list);

        void navigateToChat(Fragment fragment);
    }

    interface Presenter {
        void onDestroy();

        void btnCreateClick(Button btnCreate, EditText etName);
        void btnImageClick(SimpleDraweeView imageView);

        void onActivityResult(int requestCode, int resultCode, Intent data);

        void getPhotoFromGallery();
        void getPhotoFromCamera();

        void createDialog(String token,CreateDialogRequest request);

        CreateDialogRequest getTypeDialog(long imageId);

        void getContactsModel();

        void setOnCheckedChangeListener(RadioGroup radioGroup);

        String getToken();

        void setOnCheckedChangeListener(CheckBox checkBox, TextView textView,ContactsModel model);


        void setImageViewUser(CircleImageView imageView,ContactsModel user);
    }

    interface Model {
        Observable<ItemDialog> createDialog(String token, CreateDialogRequest createDialogRequest);

        Observable<RegistrationCreateFileResponse> createFile(String token, String mime, String fileName);

        Observable<Void> declareFileUploaded(long size, String token, long blobId);

        Observable<Void> uploadFileMap(Map<String, RequestBody> map, MultipartBody.Part part);

        Observable<UserListResponse> getUserList(String token);




        Observable<File> downloadImage(long blobId, String token);

        Observable<File> saveImage(Response<ResponseBody> response,long blobId);

    }
}
