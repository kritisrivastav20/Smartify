package com.smartyfy.library;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by papafit on 26/02/18.
 */

public class ResponseHandler extends Handler {
    private HandleResponse response;
    WeakReference<Activity> ref;

    public ResponseHandler(Activity that) {
        ref = new WeakReference<Activity>(that);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case ServerConnect.WHAT_MESSAGE:
                if (response != null) {
                    response.onMsgReceived(String.valueOf(msg.obj));
                }
                break;
            case ServerConnect.WHAT_CONNECTION:
                switch (msg.arg1) {
                    case ServerConnect.ARG1_CONNECTED:
                        if (response != null)
                            response.onConnected();
                        break;
                    case ServerConnect.ARG1_SOCKET_CLOSED:
                        if (response != null)
                            response.onConnectionClosed();
                        break;
                    case ServerConnect.ARG1_CONNECTION_FAILED:
                        if (response != null)
                            response.onConnectionFailed();
                        break;
                }
                break;
        }
    }

    public void setResponse(HandleResponse response) {
        this.response = response;
    }

    public interface HandleResponse {
        public void onConnected();
        public void onConnectionClosed();
        public void onConnectionFailed();
        public void onMsgReceived(String msg);
    }
}
