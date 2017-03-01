package com.example.lsdchat.ui.conversation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.lsdchat.R;
import com.example.lsdchat.api.dialog.model.ItemMessage;
import com.example.lsdchat.model.RealmMessage;
import com.example.lsdchat.ui.MainActivity;
import com.example.lsdchat.util.Network;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.recyclerview.R.attr.layoutManager;

public class ConversationActivity extends AppCompatActivity implements ConversationContract.View {
    private ConversationPresenter mConversationPresenter;
    private Toolbar mToolbar;

    private EditText mMessage;
    private ImageButton mButtonSend;
    private ImageButton mButtonSmiles;
    private ArrayList<RealmMessage> mMessageList = new ArrayList<>();

    private ConversationRecyclerAdapter mAdapter;
    private RecyclerView mListView;

    private static final String TAG = "ChatActivity";
    //who send message, e.t. who owner of current app
    private String contactJidOwner = "23163511-52350@chat.quickblox.com";

    //there we have to pass app_id + chat_id of chosen dialog
    private String contactJid = "52350_589f6bfda0eb47ea8400026a@muc.chat.quickblox.com";
    private BroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        mConversationPresenter = new ConversationPresenter(this);

        initView();

        Intent intentService = new Intent(this, ConversationService.class);
        startService(intentService);

        mAdapter = new ConversationRecyclerAdapter(mMessageList);
        mAdapter.setListener(new ConversationRecyclerAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onItemClicked(String id, int position, int type) {
                Toast.makeText(ConversationActivity.this, "Click " + id, Toast.LENGTH_SHORT).show();
            }
        });
        mListView.setLayoutManager(new LinearLayoutManager(this));
        mListView.setAdapter(mAdapter);

        /* There we have to get from intent dialog_name AND dialog_id, calc contactJid app_id + "_" + dialog_id +
        *  "@" + "muc.chat.quickblox.com" to it.
        * dialog_name we use for Toolbar title */


        //pass dialog_id
        String dialogIdIntent = "589f6bfda0eb47ea8400026a";
        if (Network.isOnline(this)) {
            mConversationPresenter.getMessages(dialogIdIntent);
        } else {
            fillListOfMessages(mConversationPresenter.getMessagesFromDataBase(dialogIdIntent));
        }

        mButtonSend.setOnClickListener(view -> sendTextMessage());
    }

    public void sendTextMessage() {
        if (ConversationService.getState().equals(ConversationConnection.ConnectionState.CONNECTED)) {
            String message = mMessage.getEditableText().toString();

            if (!message.equalsIgnoreCase("")) {
                Log.d(TAG, "The client is connected to the server, sending Message");

                RealmMessage chatMessage = new RealmMessage(true);
                chatMessage.setMessage(message);
                //get app owner id from db
                chatMessage.setSenderId(23163511);
                chatMessage.setDateSent(System.currentTimeMillis());

                mAdapter.add(chatMessage);
                mListView.scrollToPosition(mMessageList.size() - 1);

                mMessage.setText("");

                //Send the message to the server
                Intent intent = new Intent(ConversationService.SEND_MESSAGE);
                intent.putExtra(ConversationService.BUNDLE_MESSAGE_BODY, message);
                intent.putExtra(ConversationService.BUNDLE_TO, contactJid);
                sendBroadcast(intent);
            }
        } else {
            Toast.makeText(getApplicationContext(),
                    "Client not connected to server, message not sent!",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.conversation_toolbar);
        configToolbar();

        mMessage = (EditText) findViewById(R.id.conversation_edittext);
        mButtonSend = (ImageButton) findViewById(R.id.conversation_send);
        mButtonSmiles = (ImageButton) findViewById(R.id.conversation_smiles);

        mListView = (RecyclerView) findViewById(R.id.conversation_list);
    }

    private void configToolbar() {
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("Conversation");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.toolbar_edit:
                //check up type of chat
                if (true) {
                    //navigate to Edit Chat Screen
                    Toast.makeText(this, "edit", Toast.LENGTH_SHORT).show();

//                    Intent intent = new Intent(this, EditChatActivity.class);
//                    startActivity(intent);
                } else {
                    Toast.makeText(this, "You can not edit current chat", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_toolbar_options_menu, menu);
        return true;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void fillListOfMessages(List<RealmMessage> list) {
        for (RealmMessage rm : list) {
            mAdapter.add(rm);
        }
        mListView.scrollToPosition(mMessageList.size() - 1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                switch (action) {
                    case ConversationService.NEW_MESSAGE:
                        String from = intent.getStringExtra(ConversationService.BUNDLE_FROM_JID);
                        String body = intent.getStringExtra(ConversationService.BUNDLE_MESSAGE_BODY);

                        if (from.equals(contactJidOwner)) {
                            //do nothing
                            Log.d(TAG, "Got a message from myself");
                        } else {
                            RealmMessage chatMessage = new RealmMessage(true);
                            chatMessage.setMessage(body);

                            mAdapter.add(chatMessage);
                            mListView.scrollToPosition(mMessageList.size() - 1);
                        }
                        return;
                }
            }
        };
        IntentFilter filter = new IntentFilter(ConversationService.NEW_MESSAGE);
        registerReceiver(mBroadcastReceiver, filter);
    }
}
