package com.aliyun.ayland.rxbus;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public abstract class ATEventType {

    @IntDef({REC_ALL,REC_MAIN,REC_WELCOME,REC_ATFACESCAN,REC_SCAN,REC_FACE_USERLIST})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ReceiveType {
    }

    @IntDef({THREAD_ALL, THREAD_UI, THREAD_CHILD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ThreadType {
    }

    //接收事件的Modle类型
    public static final int REC_ALL = 0x10000;//所有model均可接收
    public static final int REC_MAIN = 0x10003;//主页面model接收
    public static final int REC_WELCOME = 0x10004;
    public static final int REC_ATFACESCAN = 0x10005;
    public static final int REC_SCAN = 0x10006;
    public static final int REC_FACE_USERLIST = 0x10007;

    //接收事件的线程
    public static final int THREAD_ALL = 0x20000;//所有线程均可接收
    public static final int THREAD_UI = 0x20001;//UI线程均可接收
    public static final int THREAD_CHILD = 0x20002;//子线程均可接收
}
