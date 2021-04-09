package com.aliyun.ayland.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.WindowManager;

/**
 * Created by Administrator on 2017/8/22.
 */

public class ATScreenUtils {
    private static int SCREENWIDTH;
    private static int SCREENHEIGHT;
    // 从android manifest 读取的
    private static int DESIGNSCREENWIDTH;
    private static int DESIGNSCREENHEIGHT;
    private static int STATUSHEIGHT;

    public static void getScreenData(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        SCREENWIDTH = wm.getDefaultDisplay().getWidth();
        SCREENHEIGHT = wm.getDefaultDisplay().getHeight();
        ApplicationInfo appInfo;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            DESIGNSCREENWIDTH = appInfo.metaData.getInt("design_width");
            DESIGNSCREENHEIGHT = appInfo.metaData.getInt("design_height");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getSCREENWIDTH(Context context) {
        if (SCREENWIDTH == 0)
            getScreenData(context);
        return SCREENWIDTH;
    }

    public static int getSCREENHEIGHT(Context context) {
        if (SCREENHEIGHT == 0)
            getScreenData(context);
        return SCREENHEIGHT;
    }

    public static int getDESIGNSCREENWIDTH(Context context) {
        if (DESIGNSCREENWIDTH == 0)
            getScreenData(context);
        return DESIGNSCREENWIDTH;
    }

    public static int getDESIGNSCREENHEIGHT(Context context) {
        if (DESIGNSCREENHEIGHT == 0)
            getScreenData(context);
        return DESIGNSCREENHEIGHT;
    }

    public static int getSTATUSHEIGHT() {
        return STATUSHEIGHT;
    }
}
