package com.aliyun.ayland.utils;

import android.widget.Toast;

import com.aliyun.ayland.global.ATApplication;

public class ToastUtils {
    private static Toast sToast;

    private ToastUtils() {
    }

    public static void shortShow(String msg) {
        if (sToast == null) {
            sToast = Toast.makeText(ATApplication.getContext(), msg,
                    Toast.LENGTH_SHORT);
        } else {
            sToast.setText(msg);
        }
        sToast.show();
    }

    public static void shortShow(int resId) {
        if (sToast == null) {
            sToast = Toast.makeText(ATApplication.getContext(),
                    ResourceUtils.getString(resId),
                    Toast.LENGTH_SHORT);
        } else {
            sToast.setText(resId);
        }
        sToast.show();
    }
}
