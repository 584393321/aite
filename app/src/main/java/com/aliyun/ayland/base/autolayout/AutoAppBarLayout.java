package com.aliyun.ayland.base.autolayout;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.aliyun.ayland.base.autolayout.util.AutoLayoutHelper;

public class AutoAppBarLayout extends AppBarLayout {
    private AutoLayoutHelper mHelper = new AutoLayoutHelper(this);

    public AutoAppBarLayout(Context context) {
        super(context);
    }

    public AutoAppBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)    {
        if (!isInEditMode())
            mHelper.adjustChildren();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)    {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new AutoAppBarLayout.LayoutParams(getContext(), attrs);
    }

    public static class LayoutParams extends AppBarLayout.LayoutParams
            implements AutoLayoutHelper.AutoLayoutParams    {
        private ATAutoLayoutInfo mATAutoLayoutInfo;

        public LayoutParams(Context c, AttributeSet attrs)        {
            super(c, attrs);
            mATAutoLayoutInfo = AutoLayoutHelper.getAutoLayoutInfo(c, attrs);
        }

        @Override
        public ATAutoLayoutInfo getATAutoLayoutInfo()        {
            return mATAutoLayoutInfo;
        }

        public LayoutParams(int width, int height)        {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source)        {
            super(source);
        }

        public LayoutParams(ViewGroup.MarginLayoutParams source)        {
            super(source);
        }
    }
}