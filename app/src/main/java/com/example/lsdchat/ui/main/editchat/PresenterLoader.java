package com.example.lsdchat.ui.main.editchat;

import android.content.Context;
import android.support.v4.content.Loader;
import android.util.Log;

public final class PresenterLoader<T extends BasePresenter> extends Loader<T> {
    private final PresenterFactory<T> factory;
    private T presenter;
    private final String tag;

    public PresenterLoader(Context context, PresenterFactory<T> factory, String tag) {
        super(context);
        this.factory = factory;
        this.tag = tag;
    }


    @Override
    protected void onStartLoading() {
        Log.i("loader", "onStartLoading-" + tag);
        if (presenter != null) {
            deliverResult(presenter);
            return;
        }

        forceLoad();

//        super.onStartLoading();
    }


    @Override
    protected void onForceLoad() {
        Log.i("loader", "onForceLoad-" + tag);
        presenter = factory.create();
        deliverResult(presenter);

//        super.onForceLoad();
    }

    @Override
    protected void onReset() {
        Log.i("loader", "onReset-" + tag);
        if (presenter != null) {
            presenter.onDestroy();
            presenter = null;
        }
//        super.onReset();
    }
}
