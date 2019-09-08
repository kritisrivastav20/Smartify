package com.smartyfy.library;

import android.os.Handler;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import static java.lang.Thread.sleep;

/**
 * Created by papafit on 26/02/18.
 */

public class ServerConnect implements Runnable {

    public static final int WHAT_MESSAGE = 2;
    public static final int WHAT_CONNECTION = 6;
    public static final int ARG1_CONNECTED = 1;
    public static final int ARG1_SOCKET_CLOSED = 2;
    public static final int ARG1_CONNECTION_FAILED = 0;
    public static final int NOTHING = -1;

    private Socket socket;
    private volatile boolean exit = false;
    private String ip = "192.168.43.70";
    private int port = 8081;
    private Handler handler;
    private PrintWriter out;
    private boolean isConnected = false;

    private Thread thread;


    public ServerConnect(Handler handler, String serverIp, int serverPort) {
        this.handler = handler;
        if (!serverIp.isEmpty()) this.ip = serverIp;
        if (serverPort != 0) this.port = serverPort;
    }

    public void sendJSON(final JSONObject jo_send) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (ServerConnect.this.out != null && !ServerConnect.this.out.checkError()) {
                        ServerConnect.this.out.println(jo_send.toString());
                        ServerConnect.this.out.flush();
                    }
                } catch (Exception e) {
                    stopClient();
                }
            }
        }).start();
    }

    public void stopClient() {
        exit = true;
        try {
            if (socket != null)
                socket.close();
            isConnected = false;
            handler.obtainMessage(WHAT_CONNECTION,ARG1_SOCKET_CLOSED,NOTHING,"Connection Closed").sendToTarget();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            InetAddress address = InetAddress.getByName(ip);
            socket = new Socket(address,port);
            socket.setSoTimeout(4000);
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //Send connected message to handler
            isConnected = true;
            handler.obtainMessage(WHAT_CONNECTION, ARG1_CONNECTED, NOTHING, "Connected").sendToTarget();
            while (!exit) {
                try {
                    sleep(1);

                    String msg = null;
                    if (in.ready()) {
                        char[] data;
                        data = in.readLine().toCharArray();
                        msg = String.valueOf(data);
                    }
                    if (!(msg == null || handler == null)) {
                        handler.obtainMessage(WHAT_MESSAGE, NOTHING, NOTHING, msg).sendToTarget();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    socket.close();
                    //send socket closed
                    isConnected = false;
                    handler.obtainMessage(WHAT_CONNECTION, ARG1_SOCKET_CLOSED, NOTHING, "Connection Closed").sendToTarget();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            isConnected = false;
            handler.obtainMessage(WHAT_CONNECTION, ARG1_CONNECTION_FAILED, NOTHING, "Connection Failed").sendToTarget();
        }
    }

    public void start() {
        if (thread != null) {
            stopClient();
            thread = null;
        }
        exit = false;
        thread = new Thread(this);
        thread.start();
    }

    public boolean isConnected() {
        return isConnected;
    }
}
