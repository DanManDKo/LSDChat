package com.example.lsdchat.util;


import com.example.lsdchat.constant.ApiConstant;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Formatter;

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

    public static String calculateRFC2104HMAC(int random, long timestamp, String email,String password)
            throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec signingKey = new SecretKeySpec(ApiConstant.AUTH_SECRET.getBytes(), HMAC_SHA1_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        mac.init(signingKey);
        return toHexString(mac.doFinal(getSignatureParams(random, timestamp,email,password).getBytes()));
    }

    private static String getSignatureParams(int random, long timestamp, String email, String password) {

        return String.format("application_id=%s&auth_key=%s&nonce=%s&timestamp=%s&user[email]=%s&user[password]=%s",
                ApiConstant.APP_ID, ApiConstant.AUTH_KEY, random, timestamp, email, password);

    }

}
