package com.example.lsdchat.ui.main.chats.dialogs;


import android.util.Log;

import com.example.lsdchat.App;
import com.example.lsdchat.constant.ApiConstant;
import com.example.lsdchat.model.IdsListInteger;
import com.example.lsdchat.model.RealmDialogModel;
import com.example.lsdchat.ui.main.conversation.ConversationFragment;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;

public class DialogsPresenter implements DialogsContract.Presenter {

    private DialogsContract.View mView;
    private DialogsContract.Model mModel;


    public DialogsPresenter(DialogsContract.View mView, DialogsContract.Model mModel) {
        this.mView = mView;
        this.mModel = mModel;

        getAllDialogAndSave();
    }

    @Override
    public void getObservableDialogByType(int type) {
        if (type == ApiConstant.TYPE_DIALOG_PUBLIC) {
            getObservableDialog(ApiConstant.TYPE_DIALOG_PUBLIC)
                    .subscribe(dialogModels -> mView.setListDialog(dialogModels));

        } else {
            List<RealmDialogModel> list = new ArrayList<>();

            Observable<List<RealmDialogModel>> oG = getObservableDialog(ApiConstant.TYPE_DIALOG_GROUP);
            oG.subscribe(list::addAll);

            Observable<List<RealmDialogModel>> oP = getObservableDialog(ApiConstant.TYPE_DIALOG_PRIVATE);
            oP.subscribe(list::addAll);

            mView.setListDialog(list);

        }
    }

    @Override
    public void getDialogFilterList(int typeDialog, String query) {
        if (typeDialog == ApiConstant.TYPE_DIALOG_PUBLIC) {
            getObservableDialog(ApiConstant.TYPE_DIALOG_PUBLIC)
                    .subscribe(dialogModels -> setFilterList(dialogModels, query));

        } else {
            List<RealmDialogModel> list = new ArrayList<>();

            Observable<List<RealmDialogModel>> oG = getObservableDialog(ApiConstant.TYPE_DIALOG_GROUP);
            oG.subscribe(list::addAll);


            Observable<List<RealmDialogModel>> oP = getObservableDialog(ApiConstant.TYPE_DIALOG_PRIVATE);
            oP.subscribe(list::addAll);

            setFilterList(list, query);

        }

    }

    private void setFilterList(List<RealmDialogModel> list, String query) {
        query = query.toLowerCase();
        List<RealmDialogModel> filterList = new ArrayList<>();
        for (RealmDialogModel dialogModel : list) {
            String name = dialogModel.getName().toLowerCase();
            if (name.contains(query)) {
                filterList.add(dialogModel);
            }
        }
        mView.setListDialog(filterList);

    }

    @Override
    public void setClickRl(RealmDialogModel realmDialogModel) {
        int ocID = 0;
        for (IdsListInteger id: realmDialogModel.getOccupantsIdsList()) {
            if (id.getValue() != App.getDataManager().getUser().getId()){
                ocID = id.getValue();
            }
        }


        mView.navigateToChat(ConversationFragment
                .newInstance(realmDialogModel.getId(), realmDialogModel.getName(),realmDialogModel.getType(),ocID));

    }

    @Override
    public void getContentModelList() {
        mModel.getObservableUserAvatar()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(contentModels -> mView.setContentModelList(contentModels));
    }

    private Observable<List<RealmDialogModel>> getObservableDialog(int type) {
        return mModel.getObservableDialogsByType(type);
    }

    @Override
    public void getAllDialogAndSave() {

        List<RealmDialogModel> list = new ArrayList<>();
        mModel.getAllDialogs(mModel.getToken())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(dialogsResponse -> Observable.just(dialogsResponse.getItemDialogList()))
                .subscribe(dialogList -> {
                    Observable.from(dialogList)
                            .subscribe(dialog -> list.add(new RealmDialogModel(dialog)));

                    mModel.saveDialog(list);

                    checkSizeDb(list);

                }, throwable -> mView.showErrorDialog(throwable));

    }

    private void checkSizeDb(List<RealmDialogModel> list) {
        if (list!=null) {
            mModel.getAllDialogFromDb()
                    .filter(realmDialogModels -> realmDialogModels.size() > list.size())
                    .flatMap(Observable::from)
                    .subscribe(dialogModel -> {
                        if (!list.contains(dialogModel)) {
                            mView.deleteItemDialog(dialogModel);
                            mModel.deleteItemDialog(dialogModel.getId());
                        }
                    });
        }
    }
}
