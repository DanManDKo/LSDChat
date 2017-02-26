package com.example.lsdchat.ui.conversation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.DefaultExtensionElement;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;

import java.io.IOException;
import java.util.Random;

public class ConversationConnection implements ConnectionListener {
    //back up with alpha dependence
    //http://pastebin.com/uewjWzYA

    //back up with multyChat
    //http://pastebin.com/fBEdcT3j

    private static final String TAG = "ConversationConnection";

    private final Context mApplicationContext;
    private final String mUsername;
    private final String mPassword;
    private final String mServiceName;
    private XMPPTCPConnection mConnection;
    //Receives messages from the ui thread.
    private BroadcastReceiver uiThreadMessageReceiver;

    String jid;
    MultiUserChat muc;
    MultiUserChatManager manager;
    //app_id + doalog_id
    public static final String mucChat = "52350_589f6bfda0eb47ea8400026a@muc.chat.quickblox.com";

    public static enum ConnectionState {
        CONNECTED, AUTHENTICATED, CONNECTING, DISCONNECTING, DISCONNECTED;
    }

    public static enum LoggedInState {
        LOGGED_IN, LOGGED_OUT;
    }

    public ConversationConnection(Context context) {
        Log.d(TAG, "RoosterConnection Constructor called.");
        mApplicationContext = context.getApplicationContext();

        /*There we have to retrieve user_id AND app_id AND password from DataBase
        *concat user_id + "-" + "ApiConstant.APP_ID";*/
        jid = "23163511-52350@chat.quickblox.com";
        //password from DataBase
        mPassword = "aaaaaaaa";

        if (jid != null) {
            mUsername = jid.split("@")[0];
            mServiceName = jid.split("@")[1];
        } else {
            mUsername = "";
            mServiceName = "";
        }
    }

    public void connect() throws IOException, XMPPException, SmackException {
        Log.d(TAG, "Connecting to server " + mServiceName);
        XMPPTCPConnectionConfiguration builder = XMPPTCPConnectionConfiguration.builder()
                .setServiceName(mServiceName)
                .setUsernameAndPassword(mUsername, mPassword)
                .setPort(5222)
                .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                .build();

        //Set up the ui thread broadcast message receiver.
        setupUiThreadBroadCastMessageReceiver();

        mConnection = new XMPPTCPConnection(builder);
        mConnection.addConnectionListener(this);
        mConnection.connect();
        mConnection.login();

        manager = MultiUserChatManager.getInstanceFor(mConnection);
        muc = manager.getMultiUserChat(mucChat);
        muc.join(mConnection.getUser());

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
        Message msg = new Message();
        //rewrite Strings below to Constants
        //These steps for saving messages to history on server
        DefaultExtensionElement extensionElement = new DefaultExtensionElement("extraParams", "jabber:client");
        extensionElement.setValue("save_to_history", "1");

        msg.setBody(body);
        msg.setStanzaId(String.valueOf(new Random(1000).nextInt()));
        msg.setType(Message.Type.groupchat);
        msg.setTo(toJid);
        msg.addExtension(extensionElement);
        try {
            muc.sendMessage(msg);
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        }
        //Bundle up the intent and send the broadcast.
        Intent intent = new Intent(ConversationService.NEW_MESSAGE);
        intent.setPackage(mApplicationContext.getPackageName());
        intent.putExtra(ConversationService.BUNDLE_FROM_JID, jid);
        intent.putExtra(ConversationService.BUNDLE_MESSAGE_BODY, msg.getBody());
        mApplicationContext.sendBroadcast(intent);
    }

    public void disconnect() {
        Log.d(TAG, "Disconnecting from server " + mServiceName);
        if (mConnection != null) {
            mConnection.disconnect();
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
    public void authenticated(XMPPConnection connection, boolean resumed) {
        ConversationService.sConnectionState = ConnectionState.CONNECTED;
        Log.d(TAG, "Authenticated Successfully");
    }

    @Override
    public void connectionClosed() {
        ConversationService.sConnectionState = ConnectionState.DISCONNECTED;
        Log.d(TAG, "Connection closed");
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        ConversationService.sConnectionState = ConnectionState.DISCONNECTED;
        Log.d(TAG, "ConnectionClosedOnError, error " + e.toString());
    }

    @Override
    public void reconnectionSuccessful() {
        ConversationService.sConnectionState = ConnectionState.CONNECTING;
        Log.d(TAG, "Reconnecting In");
    }

    @Override
    public void reconnectingIn(int seconds) {
        ConversationService.sConnectionState = ConnectionState.CONNECTED;
        Log.d(TAG, "Reconnection Successful");
    }

    @Override
    public void reconnectionFailed(Exception e) {
        ConversationService.sConnectionState = ConnectionState.DISCONNECTED;
        Log.d(TAG, "Reconnection Failed");
    }
}