package com.example.lsdchat.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import io.fabric.sdk.android.services.network.HttpRequest;



/**
 * Created by User on 21.01.2017.
 */

public class Network {
    public static boolean isOnline(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }


}
