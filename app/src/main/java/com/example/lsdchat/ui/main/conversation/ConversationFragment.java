package com.example.lsdchat.ui.main.conversation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lsdchat.App;
import com.example.lsdchat.R;
import com.example.lsdchat.api.dialog.model.ItemMessage;
import com.example.lsdchat.api.login.model.LoginUser;
import com.example.lsdchat.constant.ApiConstant;
import com.example.lsdchat.listener.EndlessScrollListener;
import com.example.lsdchat.model.ContentModel;
import com.example.lsdchat.model.User;
import com.example.lsdchat.ui.main.fragment.BaseFragment;
import com.example.lsdchat.util.error.NetworkConnect;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ConversationFragment extends BaseFragment implements ConversationContract.View {
    private static final String EMPTY_STRING = "";
    private static final int CONVERSATION_LOADER_ID = 202;
    private static final String TAG = "Conversation Loader";

    private static final String DIALOG_ID = "dialog_id";
    private static final String DIALOG_NAME = "dialog_name";
    private static final String DIALOG_TYPE = "dialog_type";
    private static final String OCCUPANT_INDEX = "singleOccupant";
    private static final String INTENT_USER_ID = "userID";
    private static final String INTENT_DIALOG_ID = "dialogID";
    private static final String INTENT_PASSWORD = "password";

    private static final int DIALOG_PRIVATE = 3;
    private static final int TOTAL_ITEM_COUNT = 20;

    private ConversationContract.Presenter mConversationPresenter;
    private OnEditchatButtonClicked mEditListener;

    private Toolbar mToolbar;
    private EditText mMessage;
    private TextView mTextViewError;
    private ProgressBar mProgressBar;
    private ImageButton mButtonSend;
    private ImageButton mButtonSmiles;

    private ConversationRecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private String mucToJID;
    private String dialogID;
    private String mNameDialog;
    private String mPrivateOccupant;
    private int mDialogType;
    private NetworkConnect networkConnect;
    private ArrayList<ItemMessage> mMessageList = new ArrayList<>();

    public static ConversationFragment newInstance(String dialogID, String nameDialog, int dialogType, int singleOccupant) {
        ConversationFragment conversationFragment = new ConversationFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DIALOG_ID, dialogID);
        bundle.putString(DIALOG_NAME, nameDialog);
        bundle.putInt(DIALOG_TYPE, dialogType);
        bundle.putInt(OCCUPANT_INDEX, singleOccupant);
        conversationFragment.setArguments(bundle);
        return conversationFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mEditListener = (OnEditchatButtonClicked) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity should implement " + OnEditchatButtonClicked.class.getSimpleName());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        networkConnect = ((NetworkConnect) getActivity());
    }

    @Override
    public boolean isNetworkConnect() {
        return networkConnect.isNetworkConnect();
    }

    @Override
    public void showErrorDialog(Throwable throwable) {
        dialogError(throwable);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);
        mConversationPresenter = new ConversationPresenter(this, new ConversationModel(), App.getSharedPreferencesManager(getActivity()));

        dialogID = getArguments().getString(DIALOG_ID);
        if (getArguments().getInt(OCCUPANT_INDEX) != -1) {
            mPrivateOccupant = String.valueOf(getArguments().getInt(OCCUPANT_INDEX));
        }
        mNameDialog = getArguments().getString(DIALOG_NAME);
        mDialogType = getArguments().getInt(DIALOG_TYPE);
        if (mDialogType != 3) {
            mucToJID = ApiConstant.APP_ID + "_" + dialogID + ApiConstant.MessageRequestParams.MULTI_USER_CHAT;
        } else {
            mucToJID = mPrivateOccupant + "-" + ApiConstant.APP_ID + "@" + ApiConstant.MessageRequestParams.USER_CHAT;
        }

        initView(view);

        onPresenterPrepared();
        return view;
    }


    private void onPresenterPrepared() {

        User user = App.getDataManager().getUser();
        Intent intentService = new Intent(getActivity(), XMPPService.class);
        intentService.putExtra(INTENT_USER_ID, user.getId());
        intentService.putExtra(INTENT_PASSWORD, user.getPassword());
        intentService.putExtra(INTENT_DIALOG_ID, dialogID);
        intentService.putExtra(DIALOG_TYPE, mDialogType);
        getActivity().startService(intentService);

        mAdapter = new ConversationRecyclerAdapter(mMessageList, mConversationPresenter, user.getId());


        mConversationPresenter.getUsersListFromDatabase();
        mConversationPresenter.getUsersAvatarsFromDatabase();
        mConversationPresenter.getMessages(dialogID, ApiConstant.MessageRequestParams.MESSAGE_LIMIT, ApiConstant.MessageRequestParams.MESSAGE_SKIP);


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new EndlessScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int skip, int totalItemsCount, RecyclerView view) {
                if (totalItemsCount > TOTAL_ITEM_COUNT) {
                    mConversationPresenter.loadMore(dialogID, skip);
                }
            }
        });

        mButtonSend.setOnClickListener(v -> sendMessage(mMessage.getEditableText().toString(), mucToJID, mDialogType));
        mButtonSmiles.setOnClickListener(v -> {
        });
    }

    private void sendMessage(String message, String sendTo, int dialogType) {

        if (XMPPService.getState().equals(XMPPConnection.ConnectionState.CONNECTED)) {

            if (!message.equalsIgnoreCase("")) {
                //Send the message to the server
                if (dialogType != 3) {
                    Intent intent = new Intent(XMPPService.SEND_MESSAGE);
                    intent.putExtra(XMPPService.BUNDLE_MESSAGE_BODY, message);
                    intent.putExtra(XMPPService.BUNDLE_TO, sendTo);
                    getApplicationContext().sendBroadcast(intent);
                } else {
                    Intent intent = new Intent(XMPPService.SEND_MESSAGE_PRIVATE);
                    intent.putExtra(XMPPService.BUNDLE_MESSAGE_BODY, message);
                    intent.putExtra(XMPPService.BUNDLE_TO, sendTo);
                    getApplicationContext().sendBroadcast(intent);
                }
            }
        } else {
            Toast.makeText(getApplicationContext(),
                    "Client not connected to server, message not sent!",
                    Toast.LENGTH_LONG).show();
        }
        clearEditableField();

    }


    @Override
    public void onResume() {
        super.onResume();
        mConversationPresenter.onRegisterBroadcastReceiver();
    }

    @Override
    public void onPause() {
        mConversationPresenter.onUnregisterBroadcastReceiver();
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
            case R.id.toolbar_edit:
                mConversationPresenter.navigateToEditchatFragment(dialogID);
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_toolbar_options_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public void onDestroy() {
        getActivity().stopService(new Intent(getContext(), XMPPService.class));
        mConversationPresenter.onDestroy();
        super.onDestroy();

    }

    private void initView(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.chats_toolbar);

        mMessage = (EditText) view.findViewById(R.id.conversation_edittext);
        mButtonSend = (ImageButton) view.findViewById(R.id.conversation_send);
        mButtonSmiles = (ImageButton) view.findViewById(R.id.conversation_smiles);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.conversation_list);
        mTextViewError = (TextView) view.findViewById(R.id.conversation_error);
        mProgressBar = (ProgressBar) view.findViewById(R.id.conversation_progress_bar);

        initToolbar(mToolbar, mNameDialog);
    }

    @Override
    public void fillConversationAdapter(List<ItemMessage> list) {
        if (list != null) {
            mAdapter.addAll(list);
        }
    }

    @Override
    public String getCurrentDialogID() {
        return dialogID;
    }

    @Override
    public void fillConversationAdapter(ItemMessage item) {
        mAdapter.addFirst(item);
        mRecyclerView.scrollToPosition(0);
    }

    @Override
    public void clearEditableField() {
        mMessage.setText(EMPTY_STRING);
    }

    @Override
    public void showLoadProgressBar(boolean visible) {
        mProgressBar.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void loadMoreData(List<ItemMessage> list) {
        mAdapter.addMore(list);
    }

    @Override
    public void passUsersListToAdapter(List<LoginUser> users) {
        mAdapter.addUsersList(users);
    }

    @Override
    public void passUsersAvatarsToAdapter(List<ContentModel> models) {
        mAdapter.addContentList(models);
    }

    @Override
    public void showAppropriateMessage(int msg) {
        String message = null;
        switch (msg) {
            case 0:
                message = getString(R.string.you_cannot_edit_this_dialog);
                break;
            case 1:
                message = getString(R.string.message_deleted);
                break;
            case 2:
                message = getString(R.string.message_updated);
                break;
        }
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showConfirmationWindow(String messageID, int position, String message, String dialogID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(message)
                .setPositiveButton(getString(R.string.alert_delete_chat_delete), (dialogInterface, i) -> {
                    mConversationPresenter.deleteMessage(messageID, position);
                    dialogInterface.dismiss();
                })
                .setNegativeButton(getString(R.string.alert_delete_chat_update), (dialog, which) -> {
                    showEditMessageWindow(messageID, position, message, dialogID);
                    dialog.dismiss();
                })
                .setNeutralButton(getString(R.string.alert_delete_chat_cancel), (dialog, which) -> dialog.dismiss())
                .setCancelable(true)
                .create()
                .show();
    }

    private void showEditMessageWindow(String messageID, int position, String message, String dialogID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        final EditText input = new EditText(getContext());
        input.setText(message);
        builder.setView(input)
                .setPositiveButton(getString(R.string.alert_delete_chat_update), (dialog, which) -> {
                    mConversationPresenter.updateMessage(messageID, position, input.getEditableText().toString(), dialogID);
                    dialog.dismiss();
                })
                .setNegativeButton(getString(R.string.alert_delete_chat_cancel), (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }


    @Override
    public void notifyAdapterItemDeleted(int position) {
        mAdapter.deleteItem(position);
        showAppropriateMessage(1);
    }

    @Override
    public void notifyAdapterItemUpdated(int position, String message) {
        mAdapter.updateItem(position, message);
        showAppropriateMessage(2);
    }


    @Override
    public void replaceFragment(String dialogId) {
        mEditListener.onEditchatSelected(dialogId);
    }

    @Override
    public void showErrorDialog(String throwable) {

        dialogError(throwable);

    }

    public interface OnEditchatButtonClicked {
        void onEditchatSelected(String dialogID);
    }
}
