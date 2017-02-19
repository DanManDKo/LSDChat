package com.example.lsdchat.ui.chat;

/**
 * Created by User on 12.02.2017.
 */

public interface ChatContract {
    interface View {
    }

    interface Model {
        int getUserId();

        void onDestroy();
    }

    interface Presenter {
        int getUserId();

        void onDestroy();
    }
}
