package com.aliyun.ayland.core;

import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.utils.ATToastUtils;

import java.util.Map;

public class ATMyMessageReceiver extends MessageReceiver {
    // 消息接收部分的LOG_TAG
    public static final String REC_TAG = "receiver";
    private static NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ATGlobalApplication.getContext());
//    private static NotificationManager mNotificationManager = (NotificationManager) ATGlobalApplication.getContext().getSystemService(NOTIFICATION_SERVICE);

    @Override
    public void onNotification(Context context, String title, String summary, Map<String, String> extraMap) {
        // TODO 处理推送通知
        Log.e("ATMyMessageReceiver", "Receive notification, title: " + title + ", summary: " + summary + ", extraMap: " + extraMap);
        ATToastUtils.shortShow(title+"---"+summary);
//        showNotify(title, summary);
    }

    @Override
    public void onMessage(Context context, CPushMessage cPushMessage) {
        Log.e("ATMyMessageReceiver", "onMessage, messageId: " + cPushMessage.getMessageId() + ", title: " + cPushMessage.getTitle() + ", content:" + cPushMessage.getContent());
//        showNotify(cPushMessage.getTitle(), cPushMessage.getContent());
    }

    @Override
    public void onNotificationOpened(Context context, String title, String summary, String extraMap) {
        Log.e("ATMyMessageReceiver", "onNotificationOpened, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
//        showNotify(title, summary);
    }

    @Override
    protected void onNotificationClickedWithNoAction(Context context, String title, String summary, String extraMap) {
        Log.e("ATMyMessageReceiver", "onNotificationClickedWithNoAction, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
//        showNotify(title, summary);
    }

    @Override
    protected void onNotificationReceivedInApp(Context context, String title, String summary, Map<String, String> extraMap, int openType, String openActivity, String openUrl) {
        Log.e("ATMyMessageReceiver", "onNotificationReceivedInApp, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap + ", openType:" + openType + ", openActivity:" + openActivity + ", openUrl:" + openUrl);
//        showNotify(title, summary);
    }

    @Override
    protected void onNotificationRemoved(Context context, String messageId) {
        Log.e("ATMyMessageReceiver", "onNotificationRemoved");
//        ATToastUtils.shortShow("---" + messageId);
    }
//
//    public static void showNotify(String title, String message) {
//        Intent intent = new Intent();
//        intent.setClass(ATGlobalApplication.getContext(), ATMainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(ATGlobalApplication.getContext(), 1,
//                intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        mBuilder.setContentTitle(title).setContentText(message)
//                .setContentIntent(pendingIntent).setNumber(9)// 显示数量
//                .setTicker(title/* getString(R.string.newupdevice) */)// 通知首次出现在通知栏，带上升动画效果的
//                .setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
//                .setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
//                .setAutoCancel(true)// 设置这个标志当用户单击面板就可以让通知将自动取消
//                .setOngoing(false)// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
//                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND)//
//                // 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
//                .setSmallIcon(R.drawable.ic_launcher);
//        getmNotificationManager().notify(100, mBuilder.build());
//    }
}