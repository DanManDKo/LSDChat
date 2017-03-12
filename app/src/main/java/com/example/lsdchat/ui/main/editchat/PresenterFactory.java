package com.example.lsdchat.ui.main.editchat;

public interface PresenterFactory<T extends BasePresenter> {
    T create();
}
