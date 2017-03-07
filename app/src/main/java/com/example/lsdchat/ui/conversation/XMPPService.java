package com.example.lsdchat.ui.conversation;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.io.IOException;

public class XMPPService extends Service {
    private static final String TAG = "RoosterService";

    public static final String UI_AUTHENTICATED = "com.example.lsdchat.uiauthenticated";
    public static final String SEND_MESSAGE = "com.example.lsdchat.sendmessage";
    public static final String BUNDLE_MESSAGE_BODY = "b_body";
    public static final String BUNDLE_TO = "b_to";

    public static final String NEW_MESSAGE = "com.example.lsdchat.newmessage";
    public static final String BUNDLE_FROM_JID = "b_from";
    public static final String MESSAGE_ID = "message_id";

    public static XMPPConnection.ConnectionState sConnectionState;
    public static XMPPConnection.LoggedInState sLoggedInState;
    private boolean mActive;//Stores whether or not the thread is active
    private Thread mThread;
    private Handler mTHandler;//Handler to post messages to the background thread.
    private XMPPConnection mConnection;


    public XMPPService() {
    }

    public static XMPPConnection.ConnectionState getState() {
        if (sConnectionState == null) {
            return XMPPConnection.ConnectionState.DISCONNECTED;
        }
        return sConnectionState;
    }

    public static XMPPConnection.LoggedInState getLoggedInState() {
        if (sLoggedInState == null) {
            return XMPPConnection.LoggedInState.LOGGED_OUT;
        }
        return sLoggedInState;
    }

    private void initConnection() {
        Log.d(TAG, "initConnection()");
        if (mConnection == null) {
            mConnection = new XMPPConnection(this);
        }
        try {
            mConnection.connect();

        } catch (IOException | SmackException | XMPPException e) {
            Log.d(TAG, "Something went wrong while connecting ,make sure the credentials are right and try again");
            e.printStackTrace();
            //Stop the service all together.
            stopSelf();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate()");
    }

    public void start() {
        Log.d(TAG, "Service Start() function called");
        if (!mActive) {
            mActive = true;
            if (mThread == null || !mThread.isAlive()) {
                mThread = new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Looper.prepare();
                        mTHandler = new Handler();
                        initConnection();
                        //THE CODE HERE RUNS IN A BACKGROUND THREAD.
                        Looper.loop();
                    }
                });
                mThread.start();
            }
        }
    }

    public void stop() {
        Log.d(TAG, "stop()");
        mActive = false;
        mTHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mConnection != null) {
                    mConnection.disconnect();
                }
            }
        });

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand()");
        start();
        return Service.START_STICKY;
        //RETURNING START_STICKY CAUSES OUR CODE TO STICK AROUND WHEN THE APP ACTIVITY HAS DIED.
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy()");
        super.onDestroy();
        stop();
    }
}
