package com.example.lsdchat.ui.main.chats.dialogs;


import android.support.v4.widget.SwipeRefreshLayout;

import com.example.lsdchat.api.dialog.response.DialogsResponse;
import com.example.lsdchat.model.DialogModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import rx.Observable;

public interface DialogsContract {

    interface Model {
        List<DialogModel> getDialogsByType(int type);
        Observable<DialogsResponse> getAllDialogs(String token);

        void saveDialog(List<DialogModel> dialogList);

    }

    interface View {
        void initAdapter(List<DialogModel> list);
        void updateAdapter();
        int getType();

    }

    interface Presenter {
        List<DialogModel> showDialogs(int type);
        void getAllDialogAndSave();

        void setImageDialog(CircleImageView imageView, DialogModel dialogModel);

        void setOnRefreshListener(SwipeRefreshLayout swipeRefreshLayout);
    }

}
