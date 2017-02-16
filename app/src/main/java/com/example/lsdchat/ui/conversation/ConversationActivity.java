package com.example.lsdchat.ui.conversation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lsdchat.R;
import com.example.lsdchat.api.dialog.model.ItemMessage;

import java.util.ArrayList;
import java.util.List;

public class ConversationActivity extends AppCompatActivity implements ConversationContract.View {
    private ConversationPresenter mConversationPresenter;
    private Toolbar mToolbar;

    private EditText mMessage;
    private ImageButton mButtonSend;
    private ImageButton mButtonSmiles;
    private ArrayList<ItemMessage> mMessageList;

    private ConversationAdapter adapter;
    private ListView mListView;

    private static final String TAG = "ChatActivity";
    private String contactJid = "23163511-52350@chat.quickblox.com";
    private BroadcastReceiver mBroadcastReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
//        mConversationPresenter = new ConversationPresenter(this);
        mMessageList = new ArrayList<>();
        initView();
        Intent intentService = new Intent(this, ConversationService.class);
        startService(intentService);

 //       String dialogId = "";
 //       mConversationPresenter.getMessages(dialogId);

        mButtonSend.setOnClickListener(view -> sendTextMessage());

        mListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        mListView.setStackFromBottom(true);

        adapter = new ConversationAdapter(this, mMessageList);
        mListView.setAdapter(adapter);
    }

    public void sendTextMessage() {
        if (ConversationService.getState().equals(ConversationConnection.ConnectionState.CONNECTED)) {
            String message = mMessage.getEditableText().toString();

            if (!message.equalsIgnoreCase("")) {
                Log.d(TAG, "The client is connected to the server,Sending Message");

                ItemMessage chatMessage = new ItemMessage();
                chatMessage.setMessage(message);

                //Send the message to the server
                Intent intent = new Intent(ConversationService.SEND_MESSAGE);
                intent.putExtra(ConversationService.BUNDLE_MESSAGE_BODY, message);
                intent.putExtra(ConversationService.BUNDLE_TO, contactJid);
                sendBroadcast(intent);

                adapter.add(chatMessage);
                adapter.notifyDataSetChanged();

                mMessage.setText("");
            }
        } else {
            Toast.makeText(getApplicationContext(),
                    "Client not connected to server ,Message not sent!",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.conversation_toolbar);
        configToolbar();

        mMessage = (EditText) findViewById(R.id.conversation_edittext);
        mButtonSend = (ImageButton) findViewById(R.id.conversation_send);
        mButtonSmiles = (ImageButton) findViewById(R.id.conversation_smiles);

        mListView = (ListView) findViewById(R.id.conversation_list);
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
                //call edit method
                Toast.makeText(this, "edit", Toast.LENGTH_SHORT).show();
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
    public void fillListOfMessages(List<ItemMessage> list) {
        mMessageList = (ArrayList<ItemMessage>) list;
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

                        if (from.equals(contactJid)) {
                            ItemMessage chatMessage = new ItemMessage();
                            chatMessage.setMessage(body);

                            adapter.add(chatMessage);
                            adapter.notifyDataSetChanged();

                        } else {
                            Log.d(TAG, "Got a message from jid :" + from);
                        }
                        return;
                }
            }
        };
        IntentFilter filter = new IntentFilter(ConversationService.NEW_MESSAGE);
        registerReceiver(mBroadcastReceiver, filter);
    }
}
