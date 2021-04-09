package com.aliyun.ayland.base.autolayout;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

import com.aliyun.ayland.base.autolayout.util.ATAutoLayoutHelper;

public class ATAutoCardView extends CardView {
    private final ATAutoLayoutHelper mHelper = new ATAutoLayoutHelper(this);

    public ATAutoCardView(Context context) {
        super(context);
    }

    public ATAutoCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ATAutoCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public ATAutoFrameLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new ATAutoFrameLayout.LayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!isInEditMode()) {
            mHelper.adjustChildren();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}