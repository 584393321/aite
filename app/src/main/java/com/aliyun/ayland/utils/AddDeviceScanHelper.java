package com.aliyun.ayland.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.aliyun.ayland.global.Constants;
import com.aliyun.iot.aep.component.router.Router;
import com.aliyun.iot.link.ui.component.LinkToast;
import com.anthouse.xuhui.R;

/**
 * The type Add device scan helper.
 *
 * @author zgb
 * @date 18 /1/16
 * @update sinyuk
 */
public class AddDeviceScanHelper {
    public static final String TAG = "AddDeviceScanHelper";
    public static final int REQUEST_CODE_CONFIG_WIFI = 0x1002;
    public static final int REQUEST_CODE_SCAN = 0x012;

    public static void wiFiConfig(Activity context, Intent data) {
        String url = data.getStringExtra("data");
        if (TextUtils.isEmpty(url)) {
            ToastUtils.shortShow("二维码不支持");
            return;
        }
        try {
            Uri uri = Uri.parse(url);
            String productKey = uri.getQueryParameter("pk");
            String deviceName = uri.getQueryParameter("dn");
            if (TextUtils.isEmpty(productKey)) {
                LinkToast.makeText(context, "配网失败", Toast.LENGTH_SHORT).show();
                return;
            }
            String code = Constants.RouterUrl.PLUGIN_ID_DEVICE_CONFIG;
            Bundle bundle = new Bundle();
            bundle.putString("productKey", productKey);
//            bundle.putString("deviceName", deviceName);
            Router.getInstance().toUrlForResult(context, code, REQUEST_CODE_CONFIG_WIFI, bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
