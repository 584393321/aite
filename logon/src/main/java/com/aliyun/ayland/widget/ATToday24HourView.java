package com.aliyun.ayland.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.aliyun.ayland.data.ATEnvironmentOutsideBottom;
import com.aliyun.ayland.utils.ATDisplayUtil;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016/10/19.
 */
public class ATToday24HourView extends View {
    private static final String TAG = "Today24HourView";
    private final int ITEM_WIDTH = 200; //每个Item的宽度
    private final int ITEM_HEIGHT = 700; //每个Item的高度
    private static final int MARGIN_LEFT_ITEM = 15; //左边预留宽度
    private static final int MARGIN_RIGHT_ITEM = 15; //右边预留宽度
    private static final int bottomTextHeight = 60;
    private int mHeight, mWidth, ITEM_SIZE = 24;
    private int tempBaseTop;  //温度折线的上边Y坐标
    private int tempBaseBottom; //温度折线的下边Y坐标
    private Paint linePaint, pointPaint;
    private TextPaint textPaint, textPaint1;

    private List<ATEnvironmentOutsideBottom> list = new ArrayList<>();
    private int maxScrollOffset = 0;//滚动条最长滚动距离
    private int scrollOffset = 0; //滚动条偏移量
    private int currentItemIndex = 0; //当前滚动的位置所对应的item下标
    private int currentWeatherRes = -1;

    private double maxValue = 26;
    private double minValue = 21;


    public ATToday24HourView(Context context) {
        this(context, null);
    }

    public ATToday24HourView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ATToday24HourView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mWidth = MARGIN_LEFT_ITEM + MARGIN_RIGHT_ITEM + ITEM_SIZE * ITEM_WIDTH;
        mHeight = 500;
        tempBaseTop = (mHeight - bottomTextHeight) / 4;
        tempBaseBottom = (mHeight - bottomTextHeight) * 2 / 3;
        initPaint();
    }

    private void initPaint() {
        pointPaint = new Paint();
        pointPaint.setColor(getResources().getColor(R.color._EBB080));
        pointPaint.setAntiAlias(true);
        pointPaint.setStyle(Paint.Style.STROKE);
        pointPaint.setTextSize(8);
        pointPaint.setStrokeWidth(3);

        linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(5);

        textPaint = new TextPaint();
        textPaint.setTextSize(ATDisplayUtil.sp2px(getContext(), 10));
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(getResources().getColor(R.color._999999));
        textPaint.setAntiAlias(true);

        textPaint1 = new TextPaint();
        textPaint1.setTextAlign(Paint.Align.CENTER);
        textPaint1.setTextSize(ATDisplayUtil.sp2px(getContext(), 11));
        textPaint1.setColor(getResources().getColor(R.color._EBB080));
        textPaint1.setAntiAlias(true);
    }

    private Point calculateTempPoint(int left, int right, double temp) {
        double minHeight = tempBaseTop;
        double maxHeight = tempBaseBottom;
        double tempY = maxHeight - (temp - minValue) * 1.0 / (maxValue - minValue) * (maxHeight - minHeight);
        return new Point((left + right) / 2, (int) tempY);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < list.size(); i++) {
            onDrawText(canvas, i);
            onDrawLine(canvas, i);
            onDrawTemp(canvas, i);
        }
        linePaint.setColor(getResources().getColor(R.color._EEEEEE));
        canvas.drawLine(MARGIN_LEFT_ITEM, mHeight, mWidth - MARGIN_RIGHT_ITEM, mHeight, linePaint);
        canvas.drawLine(MARGIN_LEFT_ITEM, 0, mWidth - MARGIN_RIGHT_ITEM, 0, linePaint);
        canvas.drawLine(MARGIN_LEFT_ITEM, 0, MARGIN_LEFT_ITEM, mHeight, linePaint);
    }

    private void onDrawTemp(Canvas canvas, int i) {
        ATEnvironmentOutsideBottom item = list.get(i);
        Point point = item.getTempPoint();
        canvas.drawCircle(point.x, point.y, 9, pointPaint);

//        if (currentItemIndex == i) {
//            int Y = getTempBarY();
//            Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.hour_24_float);
//            drawable.setBounds(getScrollBarX(),
//                    Y - ATDisplayUtil.dip2px(getContext(), 24),
//                    getScrollBarX() + ITEM_WIDTH,
//                    Y - ATDisplayUtil.dip2px(getContext(), 4));
//            drawable.draw(canvas);
//        }
    }

    private void onDrawLine(Canvas canvas, int i) {
        linePaint.setColor(getResources().getColor(R.color._EBB080));
        linePaint.setStrokeWidth(3);
        Point point = list.get(i).getTempPoint();
        if (i != 0) {
            Point pointPre = list.get(i - 1).getTempPoint();
            Path path = new Path();
            path.moveTo(pointPre.x + 9, pointPre.y);
            path.lineTo(point.x - 9, point.y);

//            if (i % 2 == 0)
//                path.cubicTo((pointPre.x + point.x) / 2, (pointPre.y + point.y) / 2 - 7, (pointPre.x + point.x) / 2, (pointPre.y + point.y) / 2 + 7, point.x-9, point.y);
//            else
//                path.cubicTo((pointPre.x + point.x) / 2, (pointPre.y + point.y) / 2 + 7, (pointPre.x + point.x) / 2, (pointPre.y + point.y) / 2 - 7, point.x-9, point.y);
            canvas.drawPath(path, linePaint);
        }
        linePaint.setColor(getResources().getColor(R.color._EEEEEE));
        canvas.drawLine(point.x + ITEM_WIDTH / 2, 0, point.x + ITEM_WIDTH / 2, mHeight, linePaint);
    }

    private void onDrawText(Canvas canvas, int i) {
        Rect rect = list.get(i).getWindyBoxRect();
        Rect targetRect = new Rect(rect.left, rect.bottom - ATDisplayUtil.dipToPix(getContext(), 10), rect.right, rect.bottom - ATDisplayUtil.dipToPix(getContext(), 10) + bottomTextHeight);
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;

        String text = list.get(i).getTime() == null ? "" : list.get(i).getTime();
        canvas.drawText(text, targetRect.centerX(), baseline, textPaint);

        String text2 = String.valueOf(list.get(i).getNum());
        Rect targetRect1 = new Rect(rect.left, ATDisplayUtil.dipToPix(getContext(), 10), rect.right, ATDisplayUtil.dipToPix(getContext(), 10) + bottomTextHeight);
        int baseline1 = (targetRect1.bottom + targetRect1.top - fontMetrics.bottom - fontMetrics.top) / 2;
        canvas.drawText(text2, targetRect1.centerX(), baseline1, textPaint1);
    }

    public void setScrollOffset(int offset, int maxScrollOffset) {
        this.maxScrollOffset = maxScrollOffset;
        scrollOffset = offset;
        currentItemIndex = calculateItemIndex(offset);
        invalidate();
    }

    //通过滚动条偏移量计算当前选择的时刻
    private int calculateItemIndex(int offset) {
        int x = getScrollBarX();
        int sum = MARGIN_LEFT_ITEM - ITEM_WIDTH / 2;
        for (int i = 0; i < ITEM_SIZE; i++) {
            sum += ITEM_WIDTH;
            if (x < sum)
                return i;
        }
        return ITEM_SIZE - 1;
    }

    private int getScrollBarX() {
        int x = (ITEM_SIZE - 1) * ITEM_WIDTH * scrollOffset / (maxScrollOffset == 0 ? 1 : maxScrollOffset);
        x = x + MARGIN_LEFT_ITEM;
        return x;
    }

    public void initEnvironmentOutsideBottoms(List<ATEnvironmentOutsideBottom> historyData, double max, double min) {
        list.clear();
        ITEM_SIZE = historyData.size();
        maxValue = max;
        minValue = min;
        if(maxValue == minValue){
            maxValue = minValue + 20;
        }
        for (int i = 0; i < ITEM_SIZE; i++) {
            int left = MARGIN_LEFT_ITEM + i * ITEM_WIDTH;
            int right = left + ITEM_WIDTH - 1;
            int top = mHeight + ITEM_HEIGHT;
            int bottom = mHeight - bottomTextHeight;
            Rect rect = new Rect(left, top, right, bottom);
            Point point = calculateTempPoint(left, right, historyData.get(i).getNum());
            historyData.get(i).setWindyBoxRect(rect);
            historyData.get(i).setTempPoint(point);
            list.add(historyData.get(i));
        }
        invalidate();
    }
}