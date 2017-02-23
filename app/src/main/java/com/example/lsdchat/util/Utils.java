package com.example.lsdchat.util;

import android.os.Environment;

import com.example.lsdchat.App;
import com.example.lsdchat.api.dialog.DialogService;

import java.io.File;

import io.realm.internal.IOException;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Utils {


    public static Observable<File> downloadImage(long blobId, String token) {
        DialogService mDialogService = App.getApiManager().getDialogService();

        return mDialogService.downloadImage(blobId, token)
                .flatMap(responseBodyResponse -> saveImage(responseBodyResponse,blobId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    private static Observable<File> saveImage(Response<ResponseBody> response, long blobId) {
        return Observable.create(subscriber -> {
            try {

                String fileName = String.valueOf(blobId) + ".jpg";
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsoluteFile(), fileName);
                BufferedSink sink = Okio.buffer(Okio.sink(file));

                sink.writeAll(response.body().source());
                sink.close();
                subscriber.onNext(file);
                subscriber.onCompleted();
            } catch (IOException e) {
                e.printStackTrace();
                subscriber.onError(e);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        });
    }

}
