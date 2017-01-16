package com.example.lsdchat.base;

public interface BaseMvpPresenter<V> {
    void attachView(V view);
    void detachView();
}
