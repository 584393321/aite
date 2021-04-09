package com.aliyun.ayland.global;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.xhc.sbh.tool.lists.logcats.LogUitl;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import at.smarthome.AT_CommandFinalManager;
import at.smarthome.AT_MsgTypeFinalManager;

/**
 * 服务器数据上报及请求回复数据处理类
 *
 * @author th
 */
public class DataUpPretreatment {
    private static List<Handler> resourceControls = new ArrayList<Handler>();
    private static Gson gson = new Gson();

    public static void setDataUPAndCallBack(Handler handler) {
        if (!resourceControls.contains(handler)) {
            resourceControls.add(handler);
        }
    }

    public synchronized static void removeDataUPAndCallBack(Handler mDataUpAndCallback) {
        synchronized (resourceControls) {
            if (resourceControls.contains(mDataUpAndCallback)) {
                resourceControls.remove(mDataUpAndCallback);
            }
        }

    }

    public synchronized static void remoteMsgUp(JSONObject jsonObject) {

    }

    public synchronized static void resultCallBack(int result) {
        synchronized (resourceControls) {
            // 将数据送到界面上
            for (Handler dataUPAndCallback2 : resourceControls) {
                LogUitl.d("send to UI result=" + result);
                Message msg = dataUPAndCallback2.obtainMessage();
                msg.what = 1;
                msg.arg1 = result;
                dataUPAndCallback2.sendMessage(msg);
            }
        }
    }

    /**
     * 服务器上传数据 预处理及上报到界面，该函数将会在线程中运行，如有耗时操作必须另开线程处理，不允许直接在该线程中执行， 否则将影响数据接收
     *
     * @param jsonObject
     */
    public synchronized static void pretreatment(JSONObject jsonObject) {
        try {
            // 当有新数据上报的时候需要在这里处理
            String msg_type = jsonObject.has("msg_type") ? jsonObject.getString("msg_type") : null;
            if (msg_type != null) {

            }
            synchronized (resourceControls) {
                // 将数据送到界面上
                for (Handler dataUPAndCallback2 : resourceControls) {
                    Message msg = dataUPAndCallback2.obtainMessage();
                    msg.what = 0;
                    msg.obj = jsonObject;
                    dataUPAndCallback2.sendMessage(msg);
                }
            }
        } catch (Exception e) {
            LogUitl.d("pretreatent====" + e.getMessage());
            e.printStackTrace();
        }
    }
}