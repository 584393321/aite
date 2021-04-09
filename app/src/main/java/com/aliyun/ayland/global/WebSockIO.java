package com.aliyun.ayland.global;

import android.util.Log;

import com.aliyun.ayland.data.LoginBean;
import com.aliyun.ayland.utils.L;
import com.google.gson.Gson;

import org.java_websocket.drafts.Draft_17;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.json.JSONObject;

import java.net.URI;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import at.smarthome.AT_Aes;


public class WebSockIO {
    private static WebSockIO sInstance;
    private Gson gson = new Gson();
    private Executor taskExecutor = Executors.newSingleThreadExecutor();
    private String personCode;

    private WebSockIO() {
    }

    private WebSocketClient mSockClient;

    public static WebSockIO getInstance() {
        if (sInstance == null) {
            sInstance = new WebSockIO();
        }
        return sInstance;
    }

    public void setUpConnect() {
        LoginBean loginBean = ATApplication.getLoginBean();
        if (loginBean == null)
            return;
        personCode = loginBean.getPersonCode();
        taskExecutor.execute(connRunnable);
    }

    public void closeSock() {
        if (mSockClient != null) {
            mSockClient.close();
            mSockClient = null;
        }
    }

    private WebSocketClient getWebSocketClient() {
        try {
            if (mSockClient == null) {
                String wspath = getWSPath();
                if (wspath != null) {
                    mSockClient = new WebSocketClient(new URI(wspath), new Draft_17());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mSockClient;
    }

    Runnable connRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                while (true) {
                    if (mSockClient != null && !mSockClient.isOpen()) {
                        closeSock();
                        getWebSocketClient().connect();
                    } else if (mSockClient != null && mSockClient.isOpen() && mSockClient.isConnected()) {
                        JSONObject jsonO = new JSONObject();
                        jsonO.put("message", "HeartBeat");
                        jsonO.put("code", 200);
                        jsonO.put("personCode", personCode);
                        sendToServer(jsonO.toString());
                        mSockClient.addHeartbeat();
                        L.d(WebSocketClient.Tag, "websock heartbeat = " + mSockClient.getHeartbeat());
                        if (mSockClient.getHeartbeat() >= 3) {
                            mSockClient.setHeartbeat(0);
                            closeSock();
                            L.d(WebSocketClient.Tag, "websock heartbeat no response , close socket ===");
                        }
                    } else {
                        getWebSocketClient();
                        L.d(WebSocketClient.Tag, "websock connect idle.");
                    }
                    Thread.sleep(60000);
                }
            } catch (Exception e) { // TODO Auto-generated catch block
                e.printStackTrace();
                L.d(WebSocketClient.Tag, "websock error==" + e.getMessage());
                e.printStackTrace();
                if (mSockClient != null) {
                    mSockClient.close();
                    mSockClient = null;
                }
            }
        }
    };

    public void sendToServer(String data) {
        if (mSockClient != null && mSockClient.isConnected()) {
            Log.e("websock", data);
            data = AT_Aes.setEncodeByKey(data, Constants.Config.AESPWD);
            try {
                mSockClient.send(data);
            } catch (WebsocketNotConnectedException e) {
                e.printStackTrace();
                L.d(WebSocketClient.Tag, "send exception == >" + e.getMessage());
            }
        } else {
            L.d(WebSocketClient.Tag, "websocket not connect");
        }
    }

    private String getWSPath() { //"ws://alisaas.atsmartlife.com:9001/villagecenter/socket/personCode"
        return "ws://alisaas.atsmartlife.com:9001/villagecenter/socket/" + personCode;
    }
}