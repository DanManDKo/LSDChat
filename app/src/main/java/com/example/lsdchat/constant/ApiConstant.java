package com.example.lsdchat.constant;

public class ApiConstant {
    public static final String SCHEME = "https://";
    public static final String HOSTNAME = "api.quickblox.com";
    public static final String SERVER = SCHEME + HOSTNAME;
    public static final String SCHEME_AMAZON = "https://";
    public static final String HOSTNAME_AMAZON = "qbprod.s3.amazonaws.com";
    public static final String SERVER_AMAZON = SCHEME_AMAZON + HOSTNAME_AMAZON;

    //    for test - Serj
    public static final String APP_ID = "52350";
    public static final String AUTH_KEY = "eYHZLgP44jpLOpf";
    public static final String AUTH_SECRET = "FcTEnZY7p7ShrUV";
    //entry points
    public static final String SESSION_REQUEST = "/session.json";
    public static final String LOGIN_REQUEST = "/login.json";
    public static final String BLOB_REQUEST = "/blobs.json";
    public static final String REGISTRATION_REQUEST = "/users.json";
    public static final String DECLARING_REQUEST = "/blobs/{blob_id}/complete.json";
    //headers
    public static final String HEADER_CONTENT_TYPE = "Content-Type: application/json";
    public static final String QB_TOKEN = "QB-Token";
    //    60 -  second, 15 - minutes
    public static final int TIME_REMIND = 60 * 15;

    public class UploadParametres {
        public static final String CONTENT_TYPE = "Content-Type";
        public static final String EXPIRES = "Expires";
        public static final String ACL = "acl";
        public static final String KEY = "key";
        public static final String POLICY = "policy";
        public static final String SUCCESS_ACTION_STATUS = "success_action_status";
        public static final String ALGORITHM = "x-amz-algorithm";
        public static final String CREDENTIAL = "x-amz-credential";
        public static final String DATE = "x-amz-date";
        public static final String SIGNATURE = "x-amz-signature";

        public static final String BLOB_ID = "blob_id";
        public static final String FILE = "file";
    }
}
