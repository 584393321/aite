package com.aliyun.ayland.service;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.aliyun.ayland.global.ATApplication;
import com.aliyun.ayland.global.DataDispatcher;
import com.aliyun.ayland.global.DataUpPretreatment;
import com.aliyun.ayland.ui.activity.WelcomeActivity;
import com.anthouse.xuhui.R;
import com.xhc.sbh.tool.lists.logcats.LogUitl;

import org.json.JSONException;
import org.json.JSONObject;

import at.smarthome.HttpConnectServer;
import at.smarthome.IHttpConnectServer;

/**
 * 查找协调器和与协调器通讯服务
 *
 * @author th
 */
public class SocketServer extends Service {
    private final static int SERVICE_ID = 1001;
    static String targetAccount = null;// 目标账号
    private static NotificationCompat.Builder mBuilder;
    private static int notifyId = 100;
    private static NotificationManager mNotificationManager;
    static HttpConnectServer mhConnectServer;
    static String ip;
    private static Handler handler = new Handler();

    private static String getVid() {
        return "0000";
    }

    public static String getPid() {
        return "0000";
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initNotify();
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChange, filter);
    }

    BroadcastReceiver networkChange = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            boolean isBreak = intent.getBooleanExtra(connManager.EXTRA_NO_CONNECTIVITY, false);
            LogUitl.d("SocketServer isBreak=======" + isBreak);
            if (!isBreak) {// 有网络
                handlerConnect.sendEmptyMessage(3);
                handlerConnect.sendEmptyMessageDelayed(3, 5000);
            } else {

            }
        }
    };

    /**
     * 获取当前网络类型
     *
     * @return 0：没有网络 1：WIFI网络 2：NET网络
     */
    public static int getNetworkType() {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) ATApplication.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE) {
            String extraInfo = networkInfo.getExtraInfo();
            if (null != extraInfo) {
                netType = 2;
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = 1;
        } else if (nType == ConnectivityManager.TYPE_ETHERNET) {
            netType = 1;
        }
        LogUitl.d(nType + " netType====" + netType);
        return netType;
    }

    public static String getCurrConnectDeviceIp() {
        return ip;
    }

    /**
     * 守护连接
     */
    Handler handlerConnect = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 1) {
                int netType = getNetworkType();
                if (netType == 1) {
                    if (mhConnectServer != null && mhConnectServer.isConnect()) {

                    } else {
                        if (mhConnectServer == null) {
                            mhConnectServer = new HttpConnectServer(ATApplication.getContext(), ATApplication.getLoginBean().getPersonCode()
                                    , ATApplication.getIoTToken(), ATApplication.getAccessToken());
                            mhConnectServer.setCallListener(mihttpConnectServer);
                        }
                    }
                } else if (netType == 2) {
                    if (mhConnectServer == null) {
                        mhConnectServer = new HttpConnectServer(ATApplication.getContext(), ATApplication.getLoginBean().getPersonCode()
                                , ATApplication.getIoTToken(), ATApplication.getAccessToken());
                        mhConnectServer.setCallListener(mihttpConnectServer);
                    }
                }
                handlerConnect.sendEmptyMessageDelayed(1, 30000);
            }
        }
    };

    /**
     * 初始化通知栏
     */
    private void initNotify() {
        mBuilder = new NotificationCompat.Builder(this);
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel("notification_id", "notification_name", NotificationManager.IMPORTANCE_LOW);
//            mNotificationManager.createNotificationChannel(channel);
//        }
    }

    public static void showNotify(String title, String content) {
        Intent intent = new Intent();
        intent.setClass(ATApplication.getContext(), WelcomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(ATApplication.getContext(), 1,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentTitle(title).setContentText(content)
                .setContentIntent(pendingIntent).setNumber(9)// 显示数量
                .setTicker(title/* getString(R.string.newupdevice) */)// 通知首次出现在通知栏，带上升动画效果的
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
//        mBuilder.setContentTitle(title).setContentText(content)
//                .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL)).setNumber(1)// 显示数量
//                .setTicker(title/* getString(R.string.newupdevice) */)// 通知首次出现在通知栏，带上升动画效果的
//                .setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
//                .setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
//                .setAutoCancel(true)// 设置这个标志当用户单击面板就可以让通知将自动取消
//                .setOngoing(false)// ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
//                // .setDefaults(Notification.DEFAULT_VIBRATE)//
//                // 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合：
//                .setSmallIcon(R.drawable.logo);
//        mNotificationManager.notify(notifyId, mBuilder.build());
    }

    /**
     * @获取默认的pendingIntent,为了防止2.3及以下版本报错
     * @flags属性: 在顶部常驻:Notification.FLAG_ONGOING_EVENT 点击去除：
     * Notification.FLAG_AUTO_CANCEL
     */
    public static PendingIntent getDefalutIntent(int flags) {
        PendingIntent pendingIntent = PendingIntent.getActivity(ATApplication.getContext(), 1,
                new Intent(ATApplication.getContext(), WelcomeActivity.class), flags);
        return pendingIntent;
    }

    private static boolean isRunningForeground() {
        try {
            ActivityManager am = (ActivityManager) ATApplication.getContext().getSystemService(Context.ACTIVITY_SERVICE);
            ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
            String currentPackageName = cn.getPackageName();
            if (!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(ATApplication.getContext().getPackageName())) {
                return true;
            }

            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (Build.VERSION.SDK_INT < 18) {
//            startForeground(SERVICE_ID, new Notification());//API < 18 ，此方法能有效隐藏Notification上的图标
//        } else {
//            Intent innerIntent = new Intent(this, InnerService.class);
////            startService(innerIntent);
//            startForegroundService(innerIntent);
//            startForeground(SERVICE_ID, new Notification());
//        }
        if (Build.VERSION.SDK_INT < 18)
            startForeground(SERVICE_ID, new Notification());

        if (intent != null) {
            if (intent.hasExtra("type")) {
                if (intent.getStringExtra("type").equals("stop")) {
                    String mac = intent.getStringExtra("mac");
                }
            } else {
                if (mhConnectServer == null) {
                    mhConnectServer = new HttpConnectServer(ATApplication.getContext(), ATApplication.getLoginBean().getPersonCode()
                            , ATApplication.getIoTToken(), ATApplication.getAccessToken());
                    mhConnectServer.setCallListener(mihttpConnectServer);
                }

            }
        }
        handlerConnect.sendEmptyMessageDelayed(1, 30000);
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    IHttpConnectServer mihttpConnectServer = new IHttpConnectServer() {
        @Override
        public void serverResult(JSONObject jsonObject) {
            try {
                String message = jsonObject.getString("message");
                if (DataDispatcher.ServerMessageType.TALKBACK.equals(message)) {
//                    showNotify(jsonObject);
                    String iotId = jsonObject.getString("iotId");
                    String recordId = jsonObject.getString("recordId");
                    boolean hangup = jsonObject.getBoolean("Hangup");
                    if (hangup) {
                        //已挂断
                        showNotify("对讲通知","您有一条未接呼叫，于"+ jsonObject.getString("time"));
                    } else
                        handler.postDelayed(() -> {
                            Intent incomeIntent = new Intent();
//                            incomeIntent.setClass(SocketServer.this, CallActivityTransferActivity.class);
                            incomeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            incomeIntent.putExtra("iotId", iotId);
                            incomeIntent.putExtra("recordId", recordId);
                            startActivity(incomeIntent);
                        }, 500);
                }else if(DataDispatcher.ServerMessageType.HANGUPNUMBER.equals(message)) {
//                    EventBus.getDefault().post(new EventClazz("VideoCallActivity"));
                }
//                DataDispatcher.dispatchServerMsg(jsonObject);
//                DataUpPretreatment.pretreatment(jsonObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void connectSuccess() {
            LogUitl.d("connectSuccess=================");
            connectState(true);
        }

        private void connectState(boolean state) {
            try {
                JSONObject jsonO = new JSONObject();
                jsonO.put("cmd", "connect_state");
                jsonO.put("remote", state);
                DataUpPretreatment.pretreatment(jsonO);
            } catch (Exception e) {

            }
        }

        @Override
        public void disconnectError() {
            // TODO Auto-generated method stub
            mhConnectServer.setCallListener(null);
            mhConnectServer.close();
            mhConnectServer = null;
            connectState(false);
        }

        @Override
        public void someone_login() {
            LogUitl.d("someone_login=========================");
            mhConnectServer.setCallListener(null);
            mhConnectServer.close();
            mhConnectServer = null;
            ATApplication.setAccount(null);
            JSONObject json = new JSONObject();
            try {
                json.put("msg_type", "someone_login");
                json.put("from_role", "0");
                json.put("from_account", "");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            connectState(false);
            DataUpPretreatment.pretreatment(json);
        }

        @Override
        public void sendResultBackCall(int result) {
            DataUpPretreatment.resultCallBack(result);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChange);
        handlerConnect.removeCallbacksAndMessages(null);
        if (ATApplication.getLoginBean().getPersonCode() != null && ATApplication.getIoTToken() != null
                && ATApplication.getLoginBean().getPersonCode().length() > 0 && ATApplication.getIoTToken().length() > 0) {
            startService(new Intent(this, SocketServer.class));
        } else {
            if (mhConnectServer != null) {
                mhConnectServer.setCallListener(null);
                mhConnectServer.close();
            }
            mhConnectServer = null;
        }
    }

    /**
     * 给 API >= 18 的平台上用的灰色保活手段
     */
    public static class InnerService extends Service {
        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            startForeground(SERVICE_ID, new Notification());
            stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
        }
    }
}