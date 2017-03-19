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
    public static final String UPDATE_REQUEST = "users/{id}.json";
    public static final String DECLARING_REQUEST = "/blobs/{blob_id}/complete.json";

    //    dialog
    public static final String DIALOGS_REQUEST = "/chat/Dialog.json";
    public static final String UPDATE_DIALOG = "chat/Dialog/{id}.json";
    public static final String MESSAGES_REQUEST = "/chat/Message.json";
    public static final String USER_LIST_REQUEST = "/users.json";
    public static final String GET_FILE_REQUEST = "/blobs/{blob_id}/download.json";
    public static final String BLOB_ID = "blob_id";

    public static final String GET_FILEPATH_REQUEST = "/blobs/{blob_id}/getblobobjectbyid.json";

    public static final String SORT_CREATE_AT="sort_create_at";
    public static final String SORT_NAME_ACS="sort_name_asc";
    public static final String SORT_NAME_DESC="sort_name_desc";

    public static final int TYPE_DIALOG_PUBLIC = 1;
    public static final int TYPE_DIALOG_GROUP = 2;
    public static final int TYPE_DIALOG_PRIVATE = 3;



    //headers
    public static final String HEADER_CONTENT_TYPE = "Content-Type: application/json";

    public static final String HEADER_QB_TOKEN_KEY = "QB-Token:";

    public static final String FORGOT_PASSWORD_REQUEST = "/users/password/reset.json";

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
        public static final String ID = "id";
        public static final String FILE = "file";
    }

    public class MessageRequestParams {
        public static final String CHAT_DIALOG_ID = "chat_dialog_id";
        public static final String LIMIT = "limit";
        public static final String SKIP = "skip";
        public static final String SORT_ASC = "sort_asc";
        public static final String SORT_DESC = "sort_desc";
        public static final String DATE_SENT = "date_sent";
        public static final String MESSAGE_ID = "_id";
        public static final String DIALOG_ID = "_id";
        public static final String MARK_AS_READ = "mark_as_read";
        public static final int MESSAGE_LIMIT = 100;
        public static final String MULTI_USER_CHAT = "@muc.chat.quickblox.com";
        public static final String USER_CHAT = "chat.quickblox.com";
    }
}
