package com.example.lsdchat.ui.main.chats.dialogs;


import com.example.lsdchat.model.DialogModel;

import java.util.List;

public interface DialogsContract {

    interface Model {
        List<DialogModel> getDialogsByType(int type);


    }

    interface View {


    }

    interface Presenter {
        void showDialogs(int type);

    }

}
