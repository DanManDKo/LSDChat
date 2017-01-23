package com.example.lsdchat.util;


import com.example.lsdchat.manager.ApiManager;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Formatter;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by User on 23.01.2017.
 */

public class HmacSha1Signature {
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    private static String toHexString(byte[] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }
    public static String calculateRFC2104HMAC()
            throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec signingKey = new SecretKeySpec(ApiManager.AUTH_SECRET.getBytes(), HMAC_SHA1_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        mac.init(signingKey);
        return toHexString(mac.doFinal(getSignatureParams().getBytes()));
    }

    private static String getSignatureParams() {
        int random = new Random().nextInt();
        long timestamp = System.currentTimeMillis()/1000;
        return String.format("application_id=%s&auth_key=%s&nonce=%s&timestamp=%s",
                ApiManager.APP_ID, ApiManager.AUTH_KEY, random, timestamp);

    }

}
