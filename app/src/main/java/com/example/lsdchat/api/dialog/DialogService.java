package com.example.lsdchat.api.dialog;

import com.example.lsdchat.api.dialog.model.ItemDialog;
import com.example.lsdchat.api.dialog.model.ItemMessage;
import com.example.lsdchat.api.dialog.request.CreateDialogRequest;
import com.example.lsdchat.api.dialog.request.CreateMessageRequest;
import com.example.lsdchat.api.dialog.request.UpdateDialogRequest;
import com.example.lsdchat.api.dialog.response.ContentResponse;
import com.example.lsdchat.api.dialog.response.DialogsResponse;
import com.example.lsdchat.api.dialog.response.MessagesResponse;
import com.example.lsdchat.api.dialog.response.UserListResponse;
import com.example.lsdchat.api.login.response.LoginResponse;
import com.example.lsdchat.api.registration.request.UpdateRequest;
import com.example.lsdchat.constant.ApiConstant;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;


public interface DialogService {

    @Headers(ApiConstant.HEADER_CONTENT_TYPE)
    @GET(ApiConstant.DIALOGS_REQUEST)
    Observable<DialogsResponse> getDialog(@Header(ApiConstant.QB_TOKEN) String token);

    @Headers(ApiConstant.HEADER_CONTENT_TYPE)
    @GET(ApiConstant.DIALOGS_REQUEST)
    Observable<DialogsResponse> getDialog(@Header(ApiConstant.QB_TOKEN) String token, @Query(ApiConstant.MessageRequestParams.DIALOG_ID) String dialogID);

    @Headers(ApiConstant.HEADER_CONTENT_TYPE)
    @PUT(ApiConstant.UPDATE_DIALOG)
    Observable<ItemDialog> updateDialog(@Header(ApiConstant.QB_TOKEN) String token, @Path(ApiConstant.MessageRequestParams.DIALOG_ID) String dialogID, @Body UpdateDialogRequest updateDialogRequest);

    @Headers(ApiConstant.HEADER_CONTENT_TYPE)
    @POST(ApiConstant.DIALOGS_REQUEST)
    Observable<ItemDialog> createDialog(@Header(ApiConstant.QB_TOKEN) String token, @Body CreateDialogRequest createDialogRequest);

    @Headers(ApiConstant.HEADER_CONTENT_TYPE)
    @GET(ApiConstant.MESSAGES_REQUEST)
    Observable<MessagesResponse> getMessages(@Header(ApiConstant.QB_TOKEN) String token, @Query(ApiConstant.MessageRequestParams.CHAT_DIALOG_ID) String chatDialogId, @Query(ApiConstant.MessageRequestParams.LIMIT) int page, @Query(ApiConstant.MessageRequestParams.SKIP) int skip, @Query(ApiConstant.MessageRequestParams.SORT_DESC) String sort, @Query(ApiConstant.MessageRequestParams.MARK_AS_READ) int readMark);

    @Headers(ApiConstant.HEADER_CONTENT_TYPE)
    @GET(ApiConstant.MESSAGES_REQUEST)
    Observable<MessagesResponse> getMessageById(@Header(ApiConstant.QB_TOKEN) String token, @Query(ApiConstant.MessageRequestParams.CHAT_DIALOG_ID) String chatDialogId, @Query(ApiConstant.MessageRequestParams.MESSAGE_ID) String messageID, @Query(ApiConstant.MessageRequestParams.MARK_AS_READ) int readMark);

    @Headers(ApiConstant.HEADER_CONTENT_TYPE)
    @POST(ApiConstant.MESSAGES_REQUEST)
    Observable<ItemMessage> createMessages(@Header(ApiConstant.QB_TOKEN) String token, @Body CreateMessageRequest createMessageRequest);

    @Headers(ApiConstant.HEADER_CONTENT_TYPE)
    @GET(ApiConstant.USER_LIST_REQUEST)
    Observable<UserListResponse> getUserList(@Header(ApiConstant.QB_TOKEN) String token, @Query("per_page") int perPage);

    @Headers(ApiConstant.HEADER_CONTENT_TYPE)
    @GET(ApiConstant.GET_FILE_REQUEST)
    Observable<Response<ResponseBody>> downloadImage(@Path(ApiConstant.BLOB_ID) long blobId, @Header(ApiConstant.QB_TOKEN) String token);

    @Headers(ApiConstant.HEADER_CONTENT_TYPE)
    @POST(ApiConstant.GET_FILEPATH_REQUEST)
    Observable<ContentResponse> downloadContent(@Path(ApiConstant.BLOB_ID) long blobId, @Header(ApiConstant.QB_TOKEN) String token);

    @PUT(ApiConstant.UPDATE_REQUEST)
    Observable<LoginResponse> updateUserInfoRequest(@Path(ApiConstant.UploadParametres.ID) String dialogID, @Header(ApiConstant.QB_TOKEN) String token, @Body UpdateRequest body);
}
