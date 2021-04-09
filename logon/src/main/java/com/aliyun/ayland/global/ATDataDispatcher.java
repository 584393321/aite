package com.aliyun.ayland.global;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.aliyun.alink.linksdk.tmp.device.panel.PanelDevice;
import com.aliyun.ayland.ui.activity.ATOldYoungCareRecordActivity;
import com.aliyun.ayland.ui.activity.ATVideoATCallActivity;
import com.aliyun.ayland.ui.activity.ATWarningNoticeActivity;
import com.aliyun.ayland.ui.activity.ATWelcomeActivity;
import com.anthouse.xuhui.R;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.NOTIFICATION_SERVICE;

public class ATDataDispatcher {
    private static NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ATGlobalApplication.getContext());
    private static NotificationManager mNotificationManager = (NotificationManager) ATGlobalApplication.getContext().getSystemService(NOTIFICATION_SERVICE);
    private static int notifyId = 100;

    public static final class ServerMessageType {
        public static final String TALKBACK = "talkback";
        public static final String HANGUPNUMBER = "HangupNumber";
        public static final String OUTALONE = "outAlone";
        public static final String SENSORALARM = "sensorAlarm";
        public static final String TALKBACK1 = "talkback1";
        public static final String SIP = "sip";
        public static final String PASSVISITOR = "passvisitor";
    }

    private ATDataDispatcher() {
    }

    /**
     * 小区服务器发送的消息 可以在派发前对消息做一些处理
     * @param
     */
    public static void dispatchServerMsg(String msg) {
        if (!TextUtils.isEmpty(msg)) {
            Log.e("websock: ", msg);
            boolean bHandled = true;
            try {
                JSONObject jsonObject = new JSONObject(msg);
                String message = jsonObject.getString("message");
                if (ServerMessageType.TALKBACK.equals(message) || ServerMessageType.OUTALONE.equals(message) || ServerMessageType.SENSORALARM.equals(message)) {
                    showNotify(jsonObject);
                } else if (ServerMessageType.TALKBACK1.equals(message)) {

                } else {
                    bHandled = false;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                bHandled = false;
            } finally {

            }
        }
    }

    /**
     * 小区服务器发送的消息 可以在派发前对消息做一些处理
     *
     * @param
     */
    public static void dispatchServerMsg(JSONObject jsonObject) {
        String type = null;
        try {
            type = jsonObject.getString("type");
            if (ServerMessageType.TALKBACK.equals(type) || ServerMessageType.PASSVISITOR.equals(type)) {
                showNotify(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void showNotify(JSONObject jsonObject) {
        try {
            String title = jsonObject.getString("title");
            Intent intent = new Intent();
            switch (jsonObject.getString("type")) {
                case ServerMessageType.PASSVISITOR:
                    //访客预约
                    intent.putExtra("id", jsonObject.getString("id"));
                    intent.putExtra("type", jsonObject.getString("type"));
                    break;
            }
            intent.setClass(ATGlobalApplication.getContext(), ATWelcomeActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(ATGlobalApplication.getContext(), 1,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentTitle(title).setContentText(jsonObject.getString("content"))
                    .setContentIntent(pendingIntent).setNumber(9)// 显示数量
                    .setTicker(title)// 通知首次出现在通知栏，带上升动画效果的
                    .setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
                    .setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
                    .setAutoCancel(true)// 设置这个标志当用户单击面板就可以让通知将自动取消
                    .setOngoing(false)// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                    .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)//
                    // 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
                    .setSmallIcon(R.drawable.at_logo);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                @SuppressLint("WrongConstant")
                NotificationChannel channel = new NotificationChannel("001", "my_channel", NotificationManager.IMPORTANCE_DEFAULT);
                channel.enableLights(true); //是否在桌面icon右上角展示小红点
                channel.setLightColor(Color.GREEN); //小红点颜色
                channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
                mNotificationManager.createNotificationChannel(channel);
                mBuilder.setChannelId("001");
            }
            mNotificationManager.notify(notifyId, mBuilder.build());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}