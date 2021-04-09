package com.aliyun.ayland.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:权限检测
 * @author: liuxin
 * @date: 2017/10/26
 */
public class ATPermissionUtils {
    public static final String TAG = ATPermissionUtils.class.getSimpleName();
    public static final int WRITE_PERMISSION_REQ_CODE = 100;


    /**
     * SD卡读写权限
     *
     * @param context
     * @return
     */
    public static boolean checkScanPermission(Activity context) {
        try {
            List<String> permissions = new ArrayList<>();
            if (Build.VERSION.SDK_INT >= 23) {
                if (PackageManager.PERMISSION_GRANTED != PermissionChecker.checkCallingOrSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
            }
            if (PackageManager.PERMISSION_GRANTED != PermissionChecker.checkCallingOrSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (PackageManager.PERMISSION_GRANTED != PermissionChecker.checkCallingOrSelfPermission(context, Manifest.permission.CAMERA)) {
                permissions.add(Manifest.permission.CAMERA);
            }
            if (permissions.size() != 0) {
                ActivityCompat.requestPermissions(context,
                        (String[]) permissions.toArray(new String[permissions.size()]),
                        WRITE_PERMISSION_REQ_CODE);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * SD卡读写权限
     *
     * @param context
     * @return
     */
    public static boolean checkWriteSDPermission(Activity context) {
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                List<String> permissions = new ArrayList<>();
                if (PackageManager.PERMISSION_GRANTED != PermissionChecker.checkCallingOrSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }

                if (permissions.size() != 0) {
                    ActivityCompat.requestPermissions(context,
                            (String[]) permissions.toArray(new String[permissions.size()]),
                            WRITE_PERMISSION_REQ_CODE);
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * 必要的权限
     *
     * @param context
     * @return
     */
    public static boolean checkMustPermission(Activity context) {
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                List<String> permissions = new ArrayList<>();
                if (PackageManager.PERMISSION_GRANTED != PermissionChecker.checkCallingOrSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                if (PackageManager.PERMISSION_GRANTED != PermissionChecker.checkCallingOrSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                }
                if (permissions.size() != 0) {
                    ActivityCompat.requestPermissions(context,
                            (String[]) permissions.toArray(new String[permissions.size()]),
                            WRITE_PERMISSION_REQ_CODE);
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }


    /**
     * 只是檢測必要的权限
     *
     * @param context
     * @return
     */
    public static boolean justcheckMustPermission(Activity context) {
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                List<String> permissions = new ArrayList<>();
                if (PackageManager.PERMISSION_GRANTED != PermissionChecker.checkCallingOrSelfPermission(context, Manifest.permission.RECORD_AUDIO)) {
                    permissions.add(Manifest.permission.RECORD_AUDIO);
                }


                if (permissions.size() != 0) {
                    ActivityCompat.requestPermissions(context,
                            (String[]) permissions.toArray(new String[permissions.size()]),
                            WRITE_PERMISSION_REQ_CODE);
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * 视频录制权限申请
     *
     * @param context
     * @return
     */
    public static boolean justcheckVideoRecordPermission(Activity context) {
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                List<String> permissions = new ArrayList<>();

                if (PackageManager.PERMISSION_GRANTED != PermissionChecker.checkCallingOrSelfPermission(context, Manifest.permission.CAMERA)) {
                    permissions.add(Manifest.permission.CAMERA);
                }
                if (PackageManager.PERMISSION_GRANTED != PermissionChecker.checkCallingOrSelfPermission(context, Manifest.permission.RECORD_AUDIO)) {
                    permissions.add(Manifest.permission.RECORD_AUDIO);
                }

                if (permissions.size() != 0) {
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }


    /**
     * 视频录制权限申请
     *
     * @param context
     * @return
     */
    public static boolean checkVideoRecordPermission(Activity context) {
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                List<String> permissions = new ArrayList<>();

                if (PackageManager.PERMISSION_GRANTED != PermissionChecker.checkCallingOrSelfPermission(context, Manifest.permission.CAMERA)) {
                    permissions.add(Manifest.permission.CAMERA);
                }
                if (PackageManager.PERMISSION_GRANTED != PermissionChecker.checkCallingOrSelfPermission(context, Manifest.permission.RECORD_AUDIO)) {
                    permissions.add(Manifest.permission.RECORD_AUDIO);
                }

                if (permissions.size() != 0) {
                    ActivityCompat.requestPermissions(context,
                            (String[]) permissions.toArray(new String[permissions.size()]),
                            WRITE_PERMISSION_REQ_CODE);
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * 视频相机权限申请
     *
     * @param context
     * @return
     */
    public static boolean justcheckCameraPermission(Activity context) {
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                List<String> permissions = new ArrayList<>();
                if (PackageManager.PERMISSION_GRANTED != PermissionChecker.checkCallingOrSelfPermission(context, Manifest.permission.CAMERA)) {
                    permissions.add(Manifest.permission.CAMERA);
                }
                if (permissions.size() != 0) {
                    ActivityCompat.requestPermissions(context,
                            (String[]) permissions.toArray(new String[permissions.size()]),
                            WRITE_PERMISSION_REQ_CODE);
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * 视频相机权限申请
     *
     * @param context
     * @return
     */
    public static boolean checkCameraPermission(Activity context) {
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                List<String> permissions = new ArrayList<>();

                if (PackageManager.PERMISSION_GRANTED != PermissionChecker.checkCallingOrSelfPermission(context, Manifest.permission.CAMERA)) {
                    permissions.add(Manifest.permission.CAMERA);
                }
                if (permissions.size() != 0) {
                    ActivityCompat.requestPermissions(context,
                            (String[]) permissions.toArray(new String[permissions.size()]),
                            WRITE_PERMISSION_REQ_CODE);
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public static boolean justcheckVoiceRecordPermission(Activity context) {
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                List<String> permissions = new ArrayList<>();

                if (PackageManager.PERMISSION_GRANTED != PermissionChecker.checkCallingOrSelfPermission(context, Manifest.permission.RECORD_AUDIO)) {
                    permissions.add(Manifest.permission.RECORD_AUDIO);
                }
                if (permissions.size() != 0) {

                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }


    /**
     * 音频录制权限申请
     *
     * @param context
     * @return
     */
    public static boolean checkVoiceRecordPermission(Activity context) {
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                List<String> permissions = new ArrayList<>();

                if (PackageManager.PERMISSION_GRANTED != PermissionChecker.checkCallingOrSelfPermission(context, Manifest.permission.RECORD_AUDIO)) {
                    permissions.add(Manifest.permission.RECORD_AUDIO);
                }
                if (permissions.size() != 0) {
                    ActivityCompat.requestPermissions(context,
                            (String[]) permissions.toArray(new String[permissions.size()]),
                            WRITE_PERMISSION_REQ_CODE);
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * 定位权限获取
     *
     * @param context
     * @return
     */
    public static boolean checkLocationPermission(Activity context) {
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                List<String> permissions = new ArrayList<>();
                if (PackageManager.PERMISSION_GRANTED != PermissionChecker.checkCallingOrSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
                }
                if (PackageManager.PERMISSION_GRANTED != PermissionChecker.checkCallingOrSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
                }

                if (permissions.size() != 0) {
                    ActivityCompat.requestPermissions(context,
                            (String[]) permissions.toArray(new String[permissions.size()]),
                            WRITE_PERMISSION_REQ_CODE);
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * 只是判斷下定位权限获取
     *
     * @param context
     * @return
     */
    public static boolean justcheckLocationPermission(Activity context) {
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                List<String> permissions = new ArrayList<>();
                if (PackageManager.PERMISSION_GRANTED != PermissionChecker.checkCallingOrSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
                }

                if (permissions.size() != 0) {

                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * 手机状态相关
     *
     * @param context
     * @return
     */
    public static boolean checkPhoneStatePermission(Activity context) {
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                List<String> permissions = new ArrayList<>();
                if (PackageManager.PERMISSION_GRANTED != PermissionChecker.checkCallingOrSelfPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                    permissions.add(Manifest.permission.READ_PHONE_STATE);
                }
                if (permissions.size() != 0) {
                    ActivityCompat.requestPermissions(context,
                            (String[]) permissions.toArray(new String[permissions.size()]),
                            WRITE_PERMISSION_REQ_CODE);
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * 获取手机短信
     *
     * @param context
     * @return
     */
    public static boolean checkPhoneMessagePermission(Activity context) {
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                List<String> permissions = new ArrayList<>();
                if (PackageManager.PERMISSION_GRANTED != PermissionChecker.checkCallingOrSelfPermission(context, Manifest.permission.READ_SMS)) {
                    permissions.add(Manifest.permission.READ_SMS);
                }
                if (permissions.size() != 0) {
                    ActivityCompat.requestPermissions(context,
                            (String[]) permissions.toArray(new String[permissions.size()]),
                            WRITE_PERMISSION_REQ_CODE);
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * 获取手机通讯录
     *
     * @param context
     * @return
     */
    public static boolean checkPhoneContactsPermission(Activity context) {
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                List<String> permissions = new ArrayList<>();
                if (PackageManager.PERMISSION_GRANTED != PermissionChecker.checkCallingOrSelfPermission(context, Manifest.permission.READ_CONTACTS)) {
                    permissions.add(Manifest.permission.READ_CONTACTS);
                }
                if (permissions.size() != 0) {
                    ActivityCompat.requestPermissions(context,
                            (String[]) permissions.toArray(new String[permissions.size()]),
                            WRITE_PERMISSION_REQ_CODE);
                    return false;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public static void jumpPermssionSetting(Activity context) {
        int sdk = Build.VERSION.SDK_INT; // SDK号

        String model = Build.MODEL; // 手机型号

        String release = Build.VERSION.RELEASE; // android系统版本号
        String brand = Build.BRAND;//手机厂商
        if (TextUtils.equals(brand.toLowerCase(), "redmi") || TextUtils.equals(brand.toLowerCase(), "xiaomi")) {
            gotoMiuiPermission(context);//小米
        } else if (TextUtils.equals(brand.toLowerCase(), "meizu")) {
            gotoMeizuPermission(context);
        } else if (TextUtils.equals(brand.toLowerCase(), "huawei") || TextUtils.equals(brand.toLowerCase(), "honor")) {
            gotoHuaweiPermission(context);
        } else {
            context.startActivity(getAppDetailSettingIntent(context));

        }
    }

    /**
     * 跳转到miui的权限管理页面
     */
    private static void gotoMiuiPermission(Activity context) {
        try { // MIUI 8
            Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
            localIntent.putExtra("extra_pkgname", context.getPackageName());
            context.startActivity(localIntent);
        } catch (Exception e) {
            try { // MIUI 5/6/7
                Intent localIntent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                localIntent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                localIntent.putExtra("extra_pkgname", context.getPackageName());
                context.startActivity(localIntent);
            } catch (Exception e1) { // 否则跳转到应用详情
                context.startActivity(getAppDetailSettingIntent(context));
            }
        }
    }

    /**
     * 跳转到魅族的权限管理系统
     */
    private static void gotoMeizuPermission(Activity context) {
        try {
            Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra("packageName", context.getPackageName());
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            context.startActivity(getAppDetailSettingIntent(context));
        }
    }

    /**
     * 华为的权限管理页面
     */
    private static void gotoHuaweiPermission(Activity context) {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity1");//华为权限管理
            intent.setComponent(comp);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            context.startActivity(getAppDetailSettingIntent(context));
        }

    }

    /**
     * 获取应用详情页面intent（如果找不到要跳转的界面，也可以先把用户引导到系统设置页面）
     *
     * @return
     */
    private static Intent getAppDetailSettingIntent(Activity context) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", context.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", context.getPackageName());
        }
        return localIntent;
    }

}

