package com.example.lsdchat.constant;

public final class ApiConstant {
    public ApiConstant() {
        //Empty constructor
    }
    public static final String SCHEME = "https://";
    public static final String HOSTNAME = "api.quickblox.com";
    public static final String SERVER = SCHEME + HOSTNAME;

    public static final String APP_ID = "52350";
    public static final String AUTH_KEY = "eYHZLgP44jpLOpf";
    public static final String AUTH_SECRET = "FcTEnZY7p7ShrUV";

    public static final String HEADER_CONTENT_TYPE = "Content-Type: application/json";

    public static final String REGISTRATION_REQUEST = "/users.json";
    public static final String SESSION_REQUEST = "/session.json";

}
