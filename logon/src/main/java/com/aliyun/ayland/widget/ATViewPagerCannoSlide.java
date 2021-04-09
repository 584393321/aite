package com.aliyun.ayland.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;

public class ATViewPagerCannoSlide extends ViewPager {
    private ViewGroup parent;
    private boolean isSlide = false;

    public void setSlide(boolean slide) {
        isSlide = slide;
    }

    public ATViewPagerCannoSlide(Context context) {
        super(context);
    }

    public ATViewPagerCannoSlide(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isSlide;
    }
}