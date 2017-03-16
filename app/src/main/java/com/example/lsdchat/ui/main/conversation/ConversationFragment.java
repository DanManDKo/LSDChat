package com.example.lsdchat.ui.main.conversation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.example.lsdchat.constant.ApiConstant;
import com.example.lsdchat.listener.EndlessScrollListener;
import com.example.lsdchat.model.User;
import com.example.lsdchat.ui.main.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

public class ConversationFragment extends BaseFragment implements ConversationContract.View {
    private static final String EMPTY_STRING = "";

    private static final String DIALOG_ID = "dialog_id";
    private static final String DIALOG_TYPE = "dialog_type";
    private static final String DIALOG_NAME = "dialog_name";
    private static final int DIALOG_PRIVATE = 3;

    private ConversationPresenter mConversationPresenter;
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
    private int dialogType;
    private String mNameDialog;

    private ArrayList<ItemMessage> mMessageList = new ArrayList<>();

    public static ConversationFragment newInstance(String dialogID, int typeDialog, String nameDialog) {
        ConversationFragment conversationFragment = new ConversationFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DIALOG_ID, dialogID);
        bundle.putInt(DIALOG_TYPE, typeDialog);
        bundle.putString(DIALOG_NAME, nameDialog);
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation, container, false);
        mConversationPresenter = new ConversationPresenter(this, App.getSharedPreferencesManager(getActivity()));
        dialogID = getArguments().getString(DIALOG_ID);
        dialogType = getArguments().getInt(DIALOG_TYPE);
        mucToJID = ApiConstant.APP_ID + "_" + dialogID + ApiConstant.MessageRequestParams.MULTI_USER_CHAT;
        mNameDialog = getArguments().getString(DIALOG_NAME);

        initView(view);

        User user = App.getDataManager().getUser();
        Intent intentService = new Intent(getActivity(), XMPPService.class);
        intentService.putExtra("userID", user.getId());
        intentService.putExtra("password", user.getPassword());
        intentService.putExtra("dialogID", dialogID);
        getActivity().startService(intentService);

        mAdapter = new ConversationRecyclerAdapter(mMessageList, mConversationPresenter, user.getId());

        if (mConversationPresenter.isOnline()) {
            mConversationPresenter.getMessages(dialogID);
        } else {
            mConversationPresenter.fillAdapterListWithMessages(dialogID);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new EndlessScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                mConversationPresenter.loadMoreFromDataBase(dialogID, page);
            }
        });

        mButtonSend.setOnClickListener(v -> mConversationPresenter.sendMessage(dialogID, mMessage.getEditableText().toString(), mucToJID));
        mButtonSmiles.setOnClickListener(v -> {
        });

        return view;
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
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_toolbar_options_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroyView() {
        mConversationPresenter.onDestroy();
        mEditListener = null;
        super.onDestroyView();
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
        Log.e("AAA", String.valueOf(mMessageList.size()));
        mAdapter.addAll(list, 0, 1);
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
    public void replaceFragment(String dialogId) {
        mEditListener.onEditchatSelected(dialogId);
    }

    public interface OnEditchatButtonClicked {
        void onEditchatSelected(String dialogID);
    }
}
