package com.aliyun.ayland.widget.contrarywind.listener;

import android.view.MotionEvent;

import com.aliyun.ayland.widget.contrarywind.view.WheelView;


/**
 * 手势监听
 */
public final class ATLoopViewGestureListener extends android.view.GestureDetector.SimpleOnGestureListener {

    private final WheelView mWheelView;


    public ATLoopViewGestureListener(WheelView WheelView) {
        this.mWheelView = WheelView;
    }

    @Override
    public final boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        mWheelView.scrollBy(velocityY);
        return true;
    }
}
