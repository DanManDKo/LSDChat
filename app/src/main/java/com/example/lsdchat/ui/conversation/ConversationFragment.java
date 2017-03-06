package com.example.lsdchat.ui.conversation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import com.example.lsdchat.listener.EndlessScrollListener;

import java.util.ArrayList;
import java.util.List;

public class ConversationFragment extends Fragment implements ConversationContract.View {
    private static final String EMPTY_STRING = "";

    private ConversationPresenter mConversationPresenter;
    private Toolbar mToolbar;
    private EditText mMessage;
    private TextView mTextViewError;
    private ProgressBar mProgressBar;
    private ImageButton mButtonSend;
    private ImageButton mButtonSmiles;

    private ConversationRecyclerAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private ArrayList<ItemMessage> mMessageList = new ArrayList<>();

    public static ConversationFragment newInstance() {
        return new ConversationFragment();
    }

    private String ownerJID = "23163511-52350@chat.quickblox.com";
    private String mucToJID = "52350_589f6bfda0eb47ea8400026a@muc.chat.quickblox.com";
    private String dialogID = "589f6bfda0eb47ea8400026a";

    private BroadcastReceiver mBroadcastReceiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_conversation, container, false);
        mConversationPresenter = new ConversationPresenter(this, App.getSharedPreferencesManager(getActivity()));

        initView(view);

        configurateToolbar();

        Intent intentService = new Intent(getActivity(), ConversationService.class);
        getActivity().startService(intentService);

        mAdapter = new ConversationRecyclerAdapter(mMessageList, mConversationPresenter);

        if (mConversationPresenter.isOnline()) {
            mConversationPresenter.getMessages(dialogID);
        } else {
            mConversationPresenter.fillAdapterListWithMessages(dialogID);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setReverseLayout(true);
//        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new EndlessScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                mConversationPresenter.loadMoreFromDataBase(dialogID, page);
            }
        });

        mButtonSend.setOnClickListener(v -> mConversationPresenter.sendMessage(dialogID, mMessage.getEditableText().toString(), mucToJID));
        mButtonSmiles.setOnClickListener(v -> {});

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                switch (action) {
                    case ConversationService.NEW_MESSAGE:
                        String from = intent.getStringExtra(ConversationService.BUNDLE_FROM_JID);
                        String body = intent.getStringExtra(ConversationService.BUNDLE_MESSAGE_BODY);

                        if (from.equals(ownerJID)) {
                            //do nothing
                            Log.d("AAA", "Got a message from myself");
                        } else {
                            //just for test
                            ItemMessage item = new ItemMessage();
                            item.setMessage(body);
                            item.setSender_id(111111);
                            item.setCreatedAt("11:11T11:11");

                            mAdapter.addFirst(item);
                            mRecyclerView.scrollToPosition(0);
                        }
                        return;
                }
            }
        };
        IntentFilter filter = new IntentFilter(ConversationService.NEW_MESSAGE);
        getActivity().registerReceiver(mBroadcastReceiver, filter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
            case R.id.toolbar_edit:
                //check up dialog_type
                if (true) {
                    //navigate to Edit Chat Screen
                    Toast.makeText(getActivity(), "edit", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(this, EditChatActivity.class);
//                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "You can not edit current chat", Toast.LENGTH_SHORT).show();
                }
                break;
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
        super.onDestroyView();
        mConversationPresenter.onDestroy();
    }

    private void initView(View view) {
        mToolbar = (Toolbar) view.findViewById(R.id.conversation_toolbar);

        mMessage = (EditText) view.findViewById(R.id.conversation_edittext);
        mButtonSend = (ImageButton) view.findViewById(R.id.conversation_send);
        mButtonSmiles = (ImageButton) view.findViewById(R.id.conversation_smiles);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.conversation_list);
        mTextViewError = (TextView) view.findViewById(R.id.conversation_error);
        mProgressBar = (ProgressBar) view.findViewById(R.id.conversation_progress_bar);
    }

    private void configurateToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Conversation");
        }
    }

    @Override
    public void fillConversationAdapter(List<ItemMessage> list) {
//        mMessageList.addAll(list);
        Log.e("AAA", String.valueOf(mMessageList.size()));
        mAdapter.addAll(list, 0, 10);
//        mRecyclerView.scrollToPosition(mMessageList.size() - 1);
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
}
