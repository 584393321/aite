package com.aliyun.ayland.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

import com.aliyun.ayland.utils.ATDisplayUtil;

/**
 * Created by user on 2016/10/19.
 */
public class ATIndexHorizontalScrollView extends HorizontalScrollView {

    private static final String TAG = "IndexHorizontal";
    private Paint textPaint;
    private ATToday24HourView mATToday24HourView;

    public ATIndexHorizontalScrollView(Context context) {
        this(context, null);
    }

    public ATIndexHorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ATIndexHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        textPaint = new Paint();
        textPaint.setTextSize(ATDisplayUtil.sp2px(getContext(), 12));
        textPaint.setAntiAlias(true);
        textPaint.setColor(new Color().WHITE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int offset = computeHorizontalScrollOffset();
        int maxOffset = computeHorizontalScrollRange() - ATDisplayUtil.getScreenWidth(getContext());
        if(mATToday24HourView != null){
//            today24HourView.drawLeftTempText(canvas, offset);
            mATToday24HourView.setScrollOffset(offset, maxOffset);
        }
    }

    public void setATToday24HourView(ATToday24HourView ATToday24HourView){
        this.mATToday24HourView = ATToday24HourView;
    }
}