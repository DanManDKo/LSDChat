package com.example.lsdchat.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.example.lsdchat.App;
import com.example.lsdchat.api.dialog.DialogService;
import com.example.lsdchat.api.dialog.response.ContentResponse;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Response;
import rx.Emitter;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Utils {



    public static Observable<ContentResponse> downloadContent(long blobId, String token) {
        DialogService mDialogService = App.getApiManager().getDialogService();
        return mDialogService.downloadContent(blobId, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }



    public static Observable<File> downloadImage(long blobId, String token) {
        DialogService mDialogService = App.getApiManager().getDialogService();

        return mDialogService.downloadImage(blobId, token)
                .flatMap(responseBodyResponse -> saveImage(responseBodyResponse, blobId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    private static Observable<File> saveImage(Response<ResponseBody> response, long blobId) {
        return Observable.fromCallable(() -> {

            String fileName = String.valueOf(blobId) + ".jpg";
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsoluteFile(), fileName);
            BufferedSink sink = Okio.buffer(Okio.sink(file));
            sink.writeAll(response.body().source());
            sink.close();
            return file;

        });
    }

}
