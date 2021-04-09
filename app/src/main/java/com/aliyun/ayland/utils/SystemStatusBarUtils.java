package com.aliyun.ayland.utils;

import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

import com.gyf.barlibrary.ImmersionBar;

/**
 * Created by JasonWang on 2017/10/12
 * 类说明 ：此工具类可以修改系统状态栏的背景颜色，而且系统状态栏额字体为黑色
 * 注意事项：  compileSdkVersion 24以上才可以使用
 */
public class SystemStatusBarUtils {
    /**
     * 初始化系统状态栏，并且修改字体颜色
     *
     * @param activity 该系统状态栏所属的界面
     */
    public static void init(Activity activity, boolean dark) {
        if (Build.VERSION.SDK_INT < 21)
            return;
        SystemStatusBarUtils.getInstance(activity)
                .transparentStatusBar()  //透明状态栏，不写默认透明色
                .keyboardEnable(true)  //解决软键盘与底部输入框冲突问题，默认为false，还有一个重载方法，可以指定软键盘mode
                .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)  //单独指定软键盘模式
                .navigationBarEnable(true)   //是否可以修改导航栏颜色，默认为true
                .navigationBarWithKitkatEnable(false)  //是否可以修改安卓4.4和emui3.1手机导航栏颜色，默认为true
//                .transparentNavigationBar()  //透明导航栏，不写默认黑色(设置此方法，fullScreen()方法自动为true)
//                .transparentBar()             //透明状态栏和导航栏，不写默认状态栏为透明色，导航栏为黑色（设置此方法，fullScreen()方法自动为true）
//                .fullScreen(false)      //有导航栏的情况下，activity全屏显示，也就是activity最下面被导航栏覆盖，不写默认非全屏
                .statusBarDarkFont(dark) //状态栏字体是黑色，不写默认为白色
                .init();
    }


    public static ImmersionBar getInstance(Activity activity) {
        return ImmersionBar.with(activity);
    }

    /**
     * 此方法要在Activity界面销毁的时候调用，也就是在onDestroy方法中调用此方法
     *
     * @param activity 该系统状态栏所属的界面
     */
    public static void destroy(Activity activity) {
        if (Build.VERSION.SDK_INT < 21)
            return;
        ImmersionBar.with(activity).destroy(); //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
    }

    /**
     * 修改系统状态栏的背景颜色
     *
     * @param activity         要修改的系统状态栏所在的Activity类
     * @param drawableResource 系统背景颜色的drawable文件
     */
    public static void changeSystemStatusBarBackground(Activity activity, int drawableResource) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = activity.getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            winParams.flags |= bits;
            win.setAttributes(winParams);
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(drawableResource);
        }
    }
}
