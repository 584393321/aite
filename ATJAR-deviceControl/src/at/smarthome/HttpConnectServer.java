package at.smarthome;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import com.xhc.sbh.tool.lists.logcats.LogUitl;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpConnectServer {
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS", Locale.getDefault());
    private boolean getServerData;
    private Vector<JSONObject> serverCommands = new Vector<JSONObject>();
    private ExecutorService singleSendDataThread = Executors.newSingleThreadExecutor();
    private ExecutorService singlegetServerDataThread = Executors.newSingleThreadExecutor();
    private String personCode, iotToken, access_token;
    private Context context;
    private IHttpConnectServer mIHttpConnectServer;
    private boolean isConnect = false;
    private boolean isNetwork = false;// 判断有网没网
    private boolean isSending = false;// 发送线程是否正在执行
    private long rando = System.currentTimeMillis();
    private String loginHttp, postHttp, currAccount, account, password;
    private long sss = System.currentTimeMillis();
    private int remote_her = 0;
    private ArrayList<String> unDecode = new ArrayList<String>();
    private String TAG = "HttpConnectServer";

    /**
     * 获取当前网络类型
     */
    private void getNetworkType() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null) {
            isNetwork = false;
        } else {
            int nType = networkInfo.getType();
            if (nType == ConnectivityManager.TYPE_MOBILE) {
                String extraInfo = networkInfo.getExtraInfo();
                if (null != extraInfo) {
                    isNetwork = true;
                }
            } else if (nType == ConnectivityManager.TYPE_WIFI) {
                isNetwork = true;
            }
        }
    }

    public HttpConnectServer(Context context, String personCode, String iotToken, String access_token) {
        this.context = context;
        this.personCode = personCode;
        this.iotToken = iotToken;
        this.access_token = access_token;
        Log.e(TAG, personCode + "---" + access_token + "---" + iotToken);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(Intent.ACTION_TIME_TICK);
        context.registerReceiver(networkChange, filter);
        getNetworkType();
        getServerData = !TextUtils.isEmpty(personCode) && !TextUtils.isEmpty(iotToken);
        singlegetServerDataThread.execute(getThread);
    }

    private BroadcastReceiver networkChange = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_TIME_TICK.equals(intent.getAction())) {
                ++remote_her;
                LogUitl.d("remote_her====" + remote_her);
                if (remote_her > 2) {
                    if (mIHttpConnectServer != null) {
                        mIHttpConnectServer.disconnectError();
                    }
                }
            } else {
                ConnectivityManager connManager = (ConnectivityManager) context
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                boolean isBreak = intent.getBooleanExtra(connManager.EXTRA_NO_CONNECTIVITY, false);
                if (!isBreak) {
                    isNetwork = true;
                } else {
                    isNetwork = false;
                    if (mIHttpConnectServer != null) {
                        mIHttpConnectServer.disconnectError();
                    }
                }
            }
        }
    };

    public void setCallListener(IHttpConnectServer mIHttpConnectServer) {
        this.mIHttpConnectServer = mIHttpConnectServer;
    }

    public synchronized void close() {
        LogUitl.d("close HttpConnectServer by SocketServer");
        getServerData = false;
        isConnect = false;
        synchronized (serverCommands) {
            serverCommands.clear();
        }
        try {
            if (context != null)
                context.unregisterReceiver(networkChange);
        } catch (Exception e) {

        }
        singleSendDataThread.shutdown();
        singlegetServerDataThread.shutdown();
        this.context = null;
        isNetwork = false;
        mIHttpConnectServer = null;
        LogUitl.d("httpConnectserver is close");
    }

    public boolean isConnect() {
        return isConnect;
    }

    private void callbackResult(JSONObject jsonO) {
        if (mIHttpConnectServer != null) {
            mIHttpConnectServer.serverResult(jsonO);
        } else {
            LogUitl.d("mIHttpConnectServer  is null");
        }
    }

    private Runnable getThread = new Runnable() {
        @Override
        public void run() {
            String result;
            while (getServerData && isNetwork) {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("personCode", AT_Aes.setEncodeByKey(personCode, "atsmartlife12345"));
                    jsonObject.put("customerCode", AT_Aes.setEncodeByKey("10008", "atsmartlife12345"));
                    result = HttpUtils2.doHttpPostAccessToken("http://47.115.112.23/aiyun-village-test/messagepush/message/eventMessages",
                            jsonObject.toString(), access_token);
                    if (result.length() > 0) {
                        result = AT_Aes.getDecodeByKey(result, "atsmartlife12345");
                    }
                    Log.e("HttpConnectServermodel", result + "---------------");
                    if (!TextUtils.isEmpty(result)) {
                        if("401".equals(result)){
                            isConnect = false;
                            Thread.sleep(1000);
                        }else{
                            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
                            if(jsonResult.has("data") && "[]".equals(jsonResult.getString("data"))){
                                isConnect = false;
                                Thread.sleep(1000);
                            }else {
                                exeResult(result);
                            }
                        }
                    } else {
                        isConnect = false;
                        Thread.sleep(1000);
                    }
                } catch (Exception e) {
                    getServerData = false;
                    isConnect = false;
                    if (mIHttpConnectServer != null) {
                        mIHttpConnectServer.disconnectError();
                    }
                    e.printStackTrace();
                }

            }
        }
    };

    private void exeResult(String result) {
        try {
            JSONObject jsonResult = new JSONObject(result);
            if (jsonResult.has("result")) {
                if (jsonResult.getString("result").equals("token_error")) {
                    LogUitl.d("loginOut by token_error2");
                    isConnect = false;
                    getServerData = false;
                } else if (jsonResult.getString("result").equals("someone_login")) {
                    if (mIHttpConnectServer != null) {
                        LogUitl.d(rando + " someone_login");
                        LogUitl.d("loginOut by someone_login");
                        mIHttpConnectServer.someone_login();
                        getServerData = false;
                    } else {
                        LogUitl.d("mIHttpConnectServer is null");
                    }
                } else if (jsonResult.getString("result").equals("recv_timeout")) {
                    remote_her = 0;
                }
            }
            getServerData = true;
            remote_her = 0;
            isConnect = true;
            callbackResult(jsonResult);
        } catch (JSONException e) {
            LogUitl.d(e.getMessage());
        } catch (Exception e) {
            LogUitl.d("getThread error=" + e.getMessage());
            getServerData = false;
            isConnect = false;
            if (mIHttpConnectServer != null) {
                mIHttpConnectServer.disconnectError();
            }
            e.printStackTrace();
        }
    }
}