package com.aliyun.ayland.utils;

import android.widget.Toast;

import com.aliyun.ayland.global.ATGlobalApplication;

public class ATToastUtils {
    private static Toast sToast;

    private ATToastUtils() {
    }

    public static void shortShow(String msg) {
        if (sToast == null) {
            sToast = Toast.makeText(ATGlobalApplication.getContext(), msg,
                    Toast.LENGTH_SHORT);
        } else {
            sToast.setText(msg);
        }
        sToast.show();
    }

    public static void shortShow(int resId) {
        if (sToast == null) {
            sToast = Toast.makeText(ATGlobalApplication.getContext(),
                    ATResourceUtils.getString(resId),
                    Toast.LENGTH_SHORT);
        } else {
            sToast.setText(resId);
        }
        sToast.show();
    }
}
