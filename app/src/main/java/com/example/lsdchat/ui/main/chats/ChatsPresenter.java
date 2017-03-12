package com.example.lsdchat.ui.main.chats;


import android.net.Uri;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.TextView;

import com.example.lsdchat.R;
import com.example.lsdchat.constant.ApiConstant;
import com.example.lsdchat.model.DialogModel;
import com.example.lsdchat.model.User;
import com.example.lsdchat.ui.main.chats.dialogs.DialogsFragment;
import com.example.lsdchat.util.Utils;
import com.facebook.common.util.UriUtil;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import rx.Observable;

public class ChatsPresenter implements ChatsContract.Presenter {
    private ChatsContract.View mView;
    private ChatsContract.Model mModel;

    private User mUser;


    public ChatsPresenter(ChatsContract.View mView, ChatsContract.Model model) {
        this.mView = mView;

        // SharedPreferencesManager should be in model layer
        this.mModel = model;
        mUser = mModel.getCurrentUser();

    }


    @Override
    public void setHeaderData(CircleImageView imageView, TextView fullName, TextView email) {


        if ((mUser.getBlobId()) != 0) {
            downLoadImage(mModel.getToken(), mUser.getBlobId(), imageView);
//
        } else {
            Uri uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                    .path(String.valueOf(R.drawable.userpic))
                    .build();
            imageView.setImageURI(uri);
        }
        fullName.setText(mUser.getFullName());
        email.setText(mUser.getEmail());


    }

    private void downLoadImage(String token, long blobId, CircleImageView imageView) {

        Observable<String> observable = Observable.create(subscriber ->
        {
            Utils.downloadContent(blobId, token)
                    .flatMap(contentResponse -> Observable.just(contentResponse.getItemContent().getImageUrl()))
                    .subscribe(imageUrl -> {
//                    imageView.setImageURI(Uri.parse(imageUrl));

                        Utils.downloadImageToView(imageUrl, imageView);
                        subscriber.onNext(imageUrl);
                        subscriber.onCompleted();
                    }, throwable -> {
                        Log.e("IMAGE-error", throwable.getMessage());
                    });

        });
       /* Utils.downloadImage(blobId, token)
                .subscribe(file -> {
                    imageView.setImageURI(Uri.fromFile(new File(file.getPath())));
                    Log.e("TETS", file.getPath());
                }, throwable -> {
                    mView.showMessageError(throwable);
                });*/

        Log.e("TEST OBS", observable.toString());

    }

    @Override
    public List<Fragment> setFragmentList() {
        List<Fragment> list = new ArrayList<>();
        list.add(new DialogsFragment().newInstance(ApiConstant.TYPE_DIALOG_PUBLIC));
        list.add(new DialogsFragment().newInstance(ApiConstant.TYPE_DIALOG_GROUP));

        return list;
    }

    @Override
    public void destroySession() {

        mModel.destroySession(mModel.getToken())
                .subscribe(aVoid -> {
                    mModel.deleteUser();
                    mView.logOut();
                }, throwable -> {
                    mView.showMessageError(throwable);
                });

    }

    @Override
    public void getAllDialogAndSave() {
        List<DialogModel> list = new ArrayList<>();
        mModel.getAllDialogs(mModel.getToken())
                .flatMap(dialogsResponse -> Observable.just(dialogsResponse.getItemDialogList()))
                .subscribe(dialogList -> {

                    Observable.from(dialogList)
                            .subscribe(dialog -> list.add(new DialogModel(dialog)));

                    mModel.saveDialog(list);

                }, throwable -> {
                    Log.e("getAllDialogAndSave", throwable.getMessage());
                });

    }
}