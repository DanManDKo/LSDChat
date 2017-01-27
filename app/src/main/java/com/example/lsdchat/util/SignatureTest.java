package com.example.lsdchat.util;

import android.util.Log;

import com.example.lsdchat.constant.ApiConstant;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class SignatureTest {

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";


    private static String getSignatureParamsAuth(String email, String password, int nonce, long timestamp) {
        return String.format("application_id=%s&auth_key=%s&nonce=%s&timestamp=%s&user[email]=%s&user[password]=%s",
                ApiConstant.APP_ID, ApiConstant.AUTH_KEY, nonce, timestamp, email, password);
    }
    private static String getSignatureParamsNoAuth(int nonce, long timestamp) {
        return String.format("application_id=%s&auth_key=%s&nonce=%s&timestamp=%s",
                ApiConstant.APP_ID, ApiConstant.AUTH_KEY, nonce, timestamp);
    }

    public static String calculateSignatureAuth(String email, String password, int nonce, long timestamp) {
        String result = null;
        try {
            String data = getSignatureParamsAuth(email, password,nonce, timestamp);
            SecretKeySpec signingKey = new SecretKeySpec(ApiConstant.AUTH_SECRET.getBytes(), HMAC_SHA1_ALGORITHM);
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);

            byte[] digest = mac.doFinal(data.getBytes());

            StringBuilder sb = new StringBuilder(digest.length * 2);
            String s;
            for (byte b : digest) {
                s = Integer.toHexString(0xFF & b);
                if (s.length() == 1) {
                    sb.append('0');
                }
                sb.append(s);
            }
            result = sb.toString();
        } catch (Exception e) {
            Log.e("error_signature", "Failed to generate HMAC : " + e.getMessage());
        }
        return result;
    }

    public static String calculateSignatureNoAuth(int nonce, long timestamp) {
        String result = null;
        try {
            String data = getSignatureParamsNoAuth(nonce, timestamp);
            SecretKeySpec signingKey = new SecretKeySpec(ApiConstant.AUTH_SECRET.getBytes(), HMAC_SHA1_ALGORITHM);

            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);
            byte[] digest = mac.doFinal(data.getBytes());
            StringBuilder sb = new StringBuilder(digest.length * 2);
            String s;
            for (byte b : digest) {
                s = Integer.toHexString(0xFF & b);
                if (s.length() == 1) {
                    sb.append('0');
                }
                sb.append(s);
            }
            result = sb.toString();
        } catch (Exception e) {
            Log.e("error_signature", "Failed to generate HMAC : " + e.getMessage());
        }
        return result;
    }


}