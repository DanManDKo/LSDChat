package com.example.lsdchat.ui.main.conversation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.example.lsdchat.App;
import com.example.lsdchat.constant.ApiConstant;
import com.example.lsdchat.model.User;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.DefaultExtensionElement;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.PacketParserUtils;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.MultiUserChatManager;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.Random;

public class XMPPConnection implements ConnectionListener {

    private final Context mApplicationContext;
    private final String mUsername;
    private final String mPassword;
    private final String mServiceName;
    public String mucChat;

    MultiUserChat muc;
    MultiUserChatManager manager;
    private XMPPTCPConnection mConnection;
    //Receives messages from the ui thread.
    private BroadcastReceiver uiThreadMessageReceiver;

    public XMPPConnection(Context context, String userID, String password, String dialogID) {
        Log.e("AAA", "RoosterConnection Constructor called.");
        mApplicationContext = context.getApplicationContext();
//        jid = "23163511-52350@chat.quickblox.com";
        mPassword = password;
        mUsername = userID + "-" + ApiConstant.APP_ID;
        mServiceName = ApiConstant.MessageRequestParams.USER_CHAT;
        mucChat = ApiConstant.APP_ID + "_" + dialogID + ApiConstant.MessageRequestParams.MULTI_USER_CHAT;
    }

    public void connect() throws IOException, XMPPException, SmackException {
        Log.e("AAA", "Connecting to server " + mServiceName);
        XMPPTCPConnectionConfiguration builder = XMPPTCPConnectionConfiguration.builder()
                .setServiceName(mServiceName)
                .setUsernameAndPassword(mUsername, mPassword)
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
        muc.addMessageListener(new MessageListener() {
            @Override
            public void processMessage(Message message) {
                String contactJid = null;

                String from = message.getFrom();
                String messageID = getMessageID(message);

                if (from.contains("/")) {
                    contactJid = from.split("/")[1];
                } else {
                    contactJid = from;
                }
                Log.e("AAA", "The real jid is :" + contactJid);

                //Bundle up the intent and send the broadcast.
                Intent intent = new Intent(XMPPService.NEW_MESSAGE);
                intent.putExtra(XMPPService.BUNDLE_FROM_JID, contactJid);
                intent.putExtra(XMPPService.MESSAGE_ID, messageID);
                intent.putExtra(XMPPService.BUNDLE_MESSAGE_BODY, message.getBody());
                mApplicationContext.sendBroadcast(intent);
            }
        });

        ReconnectionManager reconnectionManager = ReconnectionManager.getInstanceFor(mConnection);
        reconnectionManager.setEnabledPerDefault(true);
        reconnectionManager.enableAutomaticReconnection();
    }

    private String getMessageID(Message message) {
        ExtensionElement extensionElement = message.getExtension("extraParams", "jabber:client");
        String temp = extensionElement.toXML().toString();
        String messageID = null;

        try {
            XmlPullParser parser = PacketParserUtils.getParserFor(temp);
            CharSequence name = PacketParserUtils.parseContent(parser);
            XmlPullParser parser2 = PacketParserUtils.getParserFor(String.valueOf(name));
            messageID = PacketParserUtils.parseElementText(parser2);

            Log.e("AAA", messageID);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return messageID;
    }

    private void setupUiThreadBroadCastMessageReceiver() {
        uiThreadMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Check if the Intents purpose is to send the message.
                String action = intent.getAction();
                if (action.equals(XMPPService.SEND_MESSAGE)) {
                    //Send the message.
                    sendMessage(intent.getStringExtra(XMPPService.BUNDLE_MESSAGE_BODY),
                            intent.getStringExtra(XMPPService.BUNDLE_TO));
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(XMPPService.SEND_MESSAGE);
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
    }

    public void disconnect() {
        Log.e("AAA", "Disconnecting from server " + mServiceName);
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
    public void connected(org.jivesoftware.smack.XMPPConnection connection) {
        XMPPService.sConnectionState = ConnectionState.CONNECTED;
        Log.e("AAA", "Connected Successfully");
    }

    @Override
    public void authenticated(org.jivesoftware.smack.XMPPConnection connection, boolean resumed) {
        XMPPService.sConnectionState = ConnectionState.CONNECTED;
        Log.e("AAA", "Authenticated Successfully");
    }

    @Override
    public void connectionClosed() {
        XMPPService.sConnectionState = ConnectionState.DISCONNECTED;
        Log.e("AAA", "Connection closed");
    }

    @Override
    public void connectionClosedOnError(Exception e) {
        XMPPService.sConnectionState = ConnectionState.DISCONNECTED;
        Log.e("AAA", "ConnectionClosedOnError, error " + e.toString());
    }

    @Override
    public void reconnectionSuccessful() {
        XMPPService.sConnectionState = ConnectionState.CONNECTING;
        Log.e("AAA", "Reconnecting In");
    }

    @Override
    public void reconnectingIn(int seconds) {
        XMPPService.sConnectionState = ConnectionState.CONNECTED;
        Log.e("AAA", "Reconnection Successful");
    }

    @Override
    public void reconnectionFailed(Exception e) {
        XMPPService.sConnectionState = ConnectionState.DISCONNECTED;
        Log.e("AAA", "Reconnection Failed");
    }

    public static enum ConnectionState {
        CONNECTED, AUTHENTICATED, CONNECTING, DISCONNECTING, DISCONNECTED;
    }

    public static enum LoggedInState {
        LOGGED_IN, LOGGED_OUT;
    }
}