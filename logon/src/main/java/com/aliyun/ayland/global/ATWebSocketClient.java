package com.aliyun.ayland.global;

import com.aliyun.ayland.utils.ATL;

import org.java_websocket.WebSocketListener;
import org.java_websocket.drafts.Draft;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import at.smarthome.AT_Aes;

public class ATWebSocketClient extends org.java_websocket.client.WebSocketClient
        implements WebSocketListener {
    public static final String Tag = "websock";
    public static final int STATE_OPEN = 1;
    public static final int STATE_CLOSE = 2;
    public static final int STATE_ERROR = 3;
    public static final int STATE_HEARTBEAT = 4;

    private static int heartbeat = 0;
    private BlockingQueue<String> msgQueue = new LinkedBlockingQueue<>();
    private Executor mExecutor = Executors.newSingleThreadExecutor();
    private volatile boolean bConnect = false;

    public ATWebSocketClient(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }

    @Override
    public void onOpen(ServerHandshake handShake) {
        bConnect = true;
//		ATRxBus.getDefault().post(new ATRxEvent(ATEventType.REC_MAIN, ATEventType.THREAD_UI,
//				ATConstants.ATEventType.WEBSOCK_STATE, Integer.valueOf(STATE_OPEN)));
        mExecutor.execute(recRunnable);
        ATL.d(Tag, "websock opened.");
    }

    private Runnable recRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                while (bConnect) {
                    String msg = msgQueue.take();
                    ATDataDispatcher.dispatchServerMsg(msg);
                    msgQueue.remove(msg);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    public int getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(int n) {
        heartbeat = n;
    }

    public void addHeartbeat() {
        ++heartbeat;
    }

    @Override
    public void onMessage(String data) {
        String result = AT_Aes.getDecodeByKey(data, ATConstants.Config.AESPWD);
        ATL.d(Tag, "websock recv data==>" + result);
        try {
            JSONObject jObj = new JSONObject(result);
            String message = jObj.getString("message");
            int code = jObj.getInt("code");
            if(code == 200) {
                if ("HeartBeat".equals(message)) {
                    if (heartbeat > 0) {
                        --heartbeat;
                    } else {
                        heartbeat = 0;
                    }
                } else {
//                    jObj.put("result", "ack");
//                    String reply = AT_Aes.setEncodeByKey(jObj.toString(), DoorDogApplication.getAESPwd());
//                    send(reply); //消息回复
                    msgQueue.add(result);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFragment(Framedata fragment) {

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        bConnect = false;
//		ATRxBus.getDefault().post(new ATRxEvent(ATEventType.REC_MAIN, ATEventType.THREAD_UI,
//				ATConstants.ATEventType.WEBSOCK_STATE, Integer.valueOf(STATE_CLOSE)));
        ATL.d(Tag, "websock closed.");
    }

    @Override
    public void onError(Exception ex) {
        bConnect = false;
//		ATRxBus.getDefault().post(new ATRxEvent(ATEventType.REC_MAIN, ATEventType.THREAD_UI,
//				ATConstants.ATEventType.WEBSOCK_STATE, Integer.valueOf(STATE_ERROR)));
        ATL.d(Tag, "websock error.");
    }

    public boolean isConnected() {
        return bConnect;
    }
}