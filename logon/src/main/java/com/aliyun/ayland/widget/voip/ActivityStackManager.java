package com.aliyun.ayland.widget.voip;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import com.aliyun.ayland.ui.activity.ATMainActivity;
import com.aliyun.ayland.ui.activity.ATMonitorCallingActivity;

import java.util.Stack;


// Activity管理器
public class ActivityStackManager {

    private static final String TAG = ActivityStackManager.class.getSimpleName();

    private static ActivityStackManager mActivityManager = null;

    public static ActivityStackManager getInstance() {
        if (mActivityManager == null) {
            mActivityManager = new ActivityStackManager();
        }
        return mActivityManager;
    }

    private Stack<Activity> activityStack = null;// activity栈

    // 把一个activity压入栈中
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<>();
        }
        activityStack.add(activity);
        Log.d(TAG, "Stack size = " + activityStack.size());
    }

    // 获取栈顶的activity，先进后出原则
    public Activity getTopActivity() {
        if (activityStack != null && activityStack.size() > 0) {
            return activityStack.lastElement();
        }
        return null;
    }

    // 移除一个activity
    public void removeActivity(Activity activity) {
        if (activityStack != null && activityStack.size() > 0) {
            if (activity != null) {
                activityStack.remove(activity);
                Log.d(TAG, "Stack size = " + activityStack.size());
            }
        }
    }

    public void finishMonitorActivity() {
        if (activityStack != null && activityStack.size() > 0) {
            for (Activity activity : activityStack) {
                if (activity instanceof ATMonitorCallingActivity) {
                    activity.finish();
                }
            }
        }
    }

    public boolean isIncomingActivityAlive() {
        if (activityStack != null && activityStack.size() > 0) {
            for (Activity activity : activityStack) {
//                if (activity instanceof InComingCallingActivity) {
//                    return true;
//                }
            }
        }
        return false;
    }

    public boolean isMainActivityAlive() {
        if (activityStack != null && activityStack.size() > 0) {
            for (Activity activity : activityStack) {
                if (activity instanceof ATMainActivity) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getCount() {
        if (activityStack != null)
            return activityStack.size();
        return 0;
    }

    public void finishIncomingActivity() {
        if (activityStack != null && activityStack.size() > 0) {
            for (Activity activity : activityStack) {
//                if (activity instanceof InComingCallingActivity) {
//                    activity.finish();
//                }
            }
        }
    }

    // 退出所有activity
    public void finishAllActivity() {
        if (activityStack != null) {
            while (activityStack.size() > 0) {
                Activity activity = getTopActivity();
                if (activity == null)
                    break;
                activity.finish();
                removeActivity(activity);
            }
        }
    }

    public void moveTaskToFront() {
        Activity topActivity = getTopActivity();
        if (topActivity == null)
            return;
        ActivityManager activityManager = (ActivityManager) topActivity.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.moveTaskToFront(topActivity.getTaskId(), 0);
    }
}
