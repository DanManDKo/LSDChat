package com.example.lsdchat.ui.conversation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatMessageListener;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import java.io.IOException;

public class ConversationConnection implements ConnectionListener, ChatMessageListener {
    private static final String TAG = "ConversationConnection";

    private final Context mApplicationContext;
    private final String mUsername;
    private final String mPassword;
    private final String mServiceName;
    private XMPPTCPConnection mConnection;
    private BroadcastReceiver uiThreadMessageReceiver;//Receives messages from the ui thread.

    public static enum ConnectionState {
        CONNECTED, AUTHENTICATED, CONNECTING, DISCONNECTING, DISCONNECTED;
    }

    public static enum LoggedInState {
        LOGGED_IN, LOGGED_OUT;
    }

    public ConversationConnection(Context context) {
        Log.d(TAG, "RoosterConnection Constructor called.");
        mApplicationContext = context.getApplicationContext();
        //userId+appId
        String jid = "23163511-52350@chat.quickblox.com";
        mPassword = "aaaaaaaa";

        if (jid != null) {
            mUsername = "23163511-52350";
            mServiceName = "chat.quickblox.com";
        } else {
            mUsername = "";
            mServiceName = "";
        }
    }

    public void connect() throws IOException, XMPPException, SmackException {
        Log.d(TAG, "Connecting to server " + mServiceName);
        XMPPTCPConnectionConfiguration.XMPPTCPConnectionConfigurationBuilder builder =
                XMPPTCPConnectionConfiguration.builder();
        builder.setServiceName(mServiceName);
        builder.setUsernameAndPassword(mUsername, mPassword);
        builder.setRosterLoadedAtLogin(true);
        builder.setResource("Rooster");

        //Set up the ui thread broadcast message receiver.
        setupUiThreadBroadCastMessageReceiver();

        mConnection = new XMPPTCPConnection(builder.build());
        mConnection.addConnectionListener(this);
        mConnection.connect();
        mConnection.login();

        ReconnectionManager reconnectionManager = ReconnectionManager.getInstanceFor(mConnection);
        reconnectionManager.setEnabledPerDefault(true);
        reconnectionManager.enableAutomaticReconnection();
    }

    private void setupUiThreadBroadCastMessageReceiver() {
        uiThreadMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Check if the Intents purpose is to send the message.
                String action = intent.getAction();
                if (action.equals(ConversationService.SEND_MESSAGE)) {
                    //Send the message.
                    sendMessage(intent.getStringExtra(ConversationService.BUNDLE_MESSAGE_BODY),
                            intent.getStringExtra(ConversationService.BUNDLE_TO));
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConversationService.SEND_MESSAGE);
        mApplicationContext.registerReceiver(uiThreadMessageReceiver, filter);
    }

    private void sendMessage(String body, String toJid) {
        Log.d(TAG, "Sending message to :" + toJid);
        Chat chat = ChatManager.getInstanceFor(mConnection)
                .createChat(toJid, this);
        try {
            chat.sendMessage(body);
        } catch (SmackException.NotConnectedException | XMPPException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processMessage(Chat chat, Message message) {

        Log.d(TAG, "message.getBody() :" + message.getBody());
        Log.d(TAG, "message.getFrom() :" + message.getFrom());
///!!!!!!!
        String from = String.valueOf(message.getFrom());
        String contactJid = "";
        if (from.contains("/")) {
            contactJid = from.split("/")[0];
            Log.d(TAG, "The real jid is :" + contactJid);
        } else {
            contactJid = from;
        }

        //Bundle up the intent and send the broadcast.
        Intent intent = new Intent(ConversationService.NEW_MESSAGE);
        intent.setPackage(mApplicationContext.getPackageName());
        intent.putExtra(ConversationService.BUNDLE_FROM_JID, contactJid);
        intent.putExtra(ConversationService.BUNDLE_MESSAGE_BODY, message.getBody());
        mApplicationContext.sendBroadcast(intent);
        Log.d(TAG, "Received message from :" + contactJid + " broadcast sent.");

    }

    public void disconnect() {
        Log.d(TAG, "Disconnecting from serser " + mServiceName);
        if (mConnection != null) {
            try {
                mConnection.disconnect();
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            }
        }
        mConnection = null;
        // Unregister the message broadcast receiver.
        if (uiThreadMessageReceiver != null) {
            mApplicationContext.unregisterReceiver(uiThreadMessageReceiver);
            uiThreadMessageReceiver = null;
        }

    }

    @Override
    public void connected(XMPPConnection connection) {
        ConversationService.sConnectionState = ConnectionState.CONNECTED;
        Log.d(TAG, "Connected Successfully");
    }

    @Override
    public void authenticated(XMPPConnection connection) {
        ConversationService.sConnectionState = ConnectionState.CONNECTED;
        Log.d(TAG, "Authenticated Successfully");
    }

    @Override
    public void connectionClosed() {
        ConversationService.sConnectionState = ConnectionState.DISCONNECTED;
        Log.d(TAG, "Connectionclosed()");
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        ConversationService.sConnectionState = ConnectionState.DISCONNECTED;
        Log.d(TAG, "ConnectionClosedOnError, error " + e.toString());
    }

    @Override
    public void reconnectionSuccessful() {
        ConversationService.sConnectionState = ConnectionState.CONNECTING;
        Log.d(TAG, "ReconnectingIn() ");
    }

    @Override
    public void reconnectingIn(int seconds) {
        ConversationService.sConnectionState = ConnectionState.CONNECTED;
        Log.d(TAG, "ReconnectionSuccessful()");
    }

    @Override
    public void reconnectionFailed(Exception e) {
        ConversationService.sConnectionState = ConnectionState.DISCONNECTED;
        Log.d(TAG, "ReconnectionFailed()");

    }

    private void showContactListActivityWhenAuthenticated() {
        Intent i = new Intent(ConversationService.UI_AUTHENTICATED);
        i.setPackage(mApplicationContext.getPackageName());
        mApplicationContext.sendBroadcast(i);
        Log.d(TAG, "Sent the broadcast that we are authenticated");
    }
}
