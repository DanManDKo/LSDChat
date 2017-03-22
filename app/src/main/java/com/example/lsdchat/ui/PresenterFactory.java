package com.example.lsdchat.ui;

public interface PresenterFactory<T extends BasePresenter> {
    T create();
}
