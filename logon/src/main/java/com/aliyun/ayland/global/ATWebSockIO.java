package com.aliyun.ayland.global;

import android.util.Log;

import com.aliyun.ayland.data.ATLoginBean;
import com.aliyun.ayland.utils.ATL;

import org.java_websocket.drafts.Draft_17;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.json.JSONObject;

import java.net.URI;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import at.smarthome.AT_Aes;


public class ATWebSockIO {
    private static ATWebSockIO sInstance;
    private Executor taskExecutor = Executors.newSingleThreadExecutor();
    private String personCode;

    private ATWebSockIO() {
    }

    private ATWebSocketClient mSockClient;

    public static ATWebSockIO getInstance() {
        if (sInstance == null) {
            sInstance = new ATWebSockIO();
        }
        return sInstance;
    }

    public void setUpConnect() {
        ATLoginBean ATLoginBean = ATGlobalApplication.getATLoginBean();
        if (ATLoginBean == null)
            return;
        personCode = ATLoginBean.getPersonCode();
        taskExecutor.execute(connRunnable);
    }

    public void closeSock() {
        if (mSockClient != null) {
            mSockClient.close();
            mSockClient = null;
        }
    }

    private ATWebSocketClient getWebSocketClient() {
        try {
            if (mSockClient == null) {
                String wspath = getWSPath();
                mSockClient = new ATWebSocketClient(new URI(wspath), new Draft_17());
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
                        ATL.d(ATWebSocketClient.Tag, "websock heartbeat = " + mSockClient.getHeartbeat());
                        if (mSockClient.getHeartbeat() >= 3) {
                            mSockClient.setHeartbeat(0);
                            closeSock();
                            ATL.d(ATWebSocketClient.Tag, "websock heartbeat no response , close socket ===");
                        }
                    } else {
                        getWebSocketClient();
                        ATL.d(ATWebSocketClient.Tag, "websock connect idle.");
                    }
                    Thread.sleep(60000);
                }
            } catch (Exception e) { // TODO Auto-generated catch block
                e.printStackTrace();
                ATL.d(ATWebSocketClient.Tag, "websock error==" + e.getMessage());
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
            data = AT_Aes.setEncodeByKey(data, ATConstants.Config.AESPWD);
            try {
                mSockClient.send(data);
            } catch (WebsocketNotConnectedException e) {
                e.printStackTrace();
                ATL.d(ATWebSocketClient.Tag, "send exception == >" + e.getMessage());
            }
        } else {
            ATL.d(ATWebSocketClient.Tag, "websocket not connect");
        }
    }

    private String getWSPath() { //"ws://alisaas.atsmartlife.com:9001/villagecenter/socket/personCode"
        return "ws://alisaas.atsmartlife.com:9001/villagecenter/socket/" + personCode;
    }
}