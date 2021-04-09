package com.aliyun.ayland.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Environment;
import android.os.PowerManager;
import android.text.TextUtils;

import com.aliyun.ayland.global.ATGlobalApplication;
import com.anthouse.xuhui.R;
import com.xhc.sbh.tool.lists.logcats.LogUitl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.aliyun.ayland.utils.ATResourceUtils.getString;


public final class ATXuhuiUtils {
    private static long lastClickTime;

    private ATXuhuiUtils() {
    }

    public static boolean isFastDoubleClick(int delay) {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < delay) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static void setGpio(String value) {
        File file = new File("/sys/class/gpioctrl/gpioctrl");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(value.getBytes());
            out.flush();
        } catch (Exception e) {
            LogUitl.d("gpio==" + e.getMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean isValidPhone(String phone) {
        String regex = "^(13\\d|14[5,7]|15[0-3,5-9]|17[0,6-8]|18\\d)\\d{8}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    public static String getProcessName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo proInfo : runningApps) {
            if (proInfo.pid == android.os.Process.myPid()) {
                if (proInfo.processName != null) {
                    return proInfo.processName;
                }
            }
        }
        return null;
    }

    public static void restartSystem() {
        ATGlobalApplication.getContext().sendBroadcast(new Intent("atte.restart.system"));
        PackageManager pm = ATGlobalApplication.getContext().getPackageManager();
        boolean bAccess = (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.REBOOT", ATGlobalApplication.getContext().getPackageName()));
        if (bAccess) {
            PowerManager pManager = (PowerManager) ATGlobalApplication.getContext().getSystemService(Context.POWER_SERVICE);  //重启到fastboot模式
            pManager.reboot("");
        } else {
            ATToastUtils.shortShow(getString(R.string.at_have_no_permission));
        }
    }

    //"http://192.168.9.168:2345/?operate=getweather"
//    public static String getWeatherUrl() {
//        StringBuilder url = new StringBuilder();
//        url.append("http://").append(AtSipKitEnvBuilder.getDefault().getServerIp()).append(":2345");
//        url.append("/?operate=getweather");
//        return url.toString();
//    }

    public static boolean isValidIP(String ipAddress) {
        if (TextUtils.isEmpty(ipAddress)) {
            return false;
        }
        String regex = "^(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)"
                + "\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }

    public static String toTimeStr(int time) {
        StringBuilder timeFmt = new StringBuilder();
        timeFmt.setLength(0);
        int hour = time / 3600;
        if (hour < 10) {
            timeFmt.append("0").append(hour);
        } else {
            timeFmt.append(hour);
        }
        timeFmt.append(":");
        int minute = (time % 3600) / 60;
        if (minute < 10) {
            timeFmt.append("0").append(minute);
        } else {
            timeFmt.append(minute);
        }
        timeFmt.append(":");
        int second = time % 60;
        if (second < 10) {
            timeFmt.append("0").append(second);
        } else {
            timeFmt.append(second);
        }
        return timeFmt.toString();
    }

    @SuppressWarnings("deprecation")
    public static int FindFrontCamera() {
        int cameraCount;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras(); // get cameras number

        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo); // get camerainfo
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置  
                return camIdx;
            }
        }
        return -1;
    }

    @SuppressWarnings("deprecation")
    public static int FindBackCamera() {
        int cameraCount;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras(); // get cameras number

        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo); // get camerainfo
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置  
                return camIdx;
            }
        }
        return -1;
    }

    public static String genRecordPicName() {
        StringBuilder builder = new StringBuilder();
        builder.append(Environment.getExternalStorageDirectory().getAbsolutePath())
                .append(File.separator).append("shotscreen");
        File dir = new File(builder.toString());
        if (!dir.exists()) {
            dir.mkdirs();
        }
        builder.append(File.separator).append(System.currentTimeMillis()).append(".jpg");
        return builder.toString();
    }
}