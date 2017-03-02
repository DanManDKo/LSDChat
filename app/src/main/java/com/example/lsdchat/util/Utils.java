package com.example.lsdchat.util;

import android.content.Context;
import android.os.Environment;

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
import io.realm.internal.IOException;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Utils {
    static ImageLoader imageLoader;


    public static Observable<ContentResponse> downloadContent(long blobId, String token) {
        DialogService mDialogService = App.getApiManager().getDialogService();
        return mDialogService.downloadContent(blobId, token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    public static  void initLoader(Context context){

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300))
                .resetViewBeforeLoading(true)

                .build();

        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app
        config.defaultDisplayImageOptions(defaultOptions);
        ImageLoader.getInstance().init(config.build());
        imageLoader = ImageLoader.getInstance();

    }
    public static void downloadImageToView(String url, CircleImageView imageView){
        imageLoader.displayImage(url, imageView);
    }
    public static void downloadImageToView(String url, SimpleDraweeView imageView){
        imageLoader.displayImage(url, imageView);
    }



    public static Observable<File> downloadImage(long blobId, String token) {
        DialogService mDialogService = App.getApiManager().getDialogService();

        return mDialogService.downloadImage(blobId, token)
                .flatMap(responseBodyResponse -> saveImage(responseBodyResponse, blobId))
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
