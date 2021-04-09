package com.aliyun.ayland.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.anthouse.xuhui.R;

import static android.graphics.Shader.TileMode.CLAMP;

public class ATSemicircleProgressView extends View {
    private Context mContext;
    private static final int defaultPadding = 20;
    private int width;
    private int height;
    private static final float mStartAngle = 125.0F;
    private static final float mEndAngle = 290.0F;
    private Paint mMiddleArcPaint;
    private Paint mTextPaint;
    private Paint mTextPaint2;
    private Paint mArcProgressPaint;
    private int radius;
    private RectF mMiddleRect;
    private RectF mMiddleProgressRect;
    private int mMinNum;
    private int mMaxNum;
    private float mCurrentAngle;
    private float mTotalAngle;
    private String sesameLevel;
    private String Title;
    private String SubTile;
    private Bitmap bitmap;
    private float[] pos;
    private float[] tan;
    private Matrix matrix;
    private Paint mBitmapPaint;
    private int SemicircleSize;
    private int SemicirclelineSize;
    private int SemicircleBackgroundlineSize;
    private int backgroundLineColor;
    private int frontLineColor;
    private int titleColor;
    private int subtitleColor;
    private int maxWidth;
    private int maxHeight;
    private int semicircletitleSize;
    private int semicirclesubtitleSize;

    public ATSemicircleProgressView(Context context) {
        this(context, (AttributeSet)null);
    }

    public ATSemicircleProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ATSemicircleProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mMinNum = 0;
        mMaxNum = 40;
        mCurrentAngle = 0.0F;
        mTotalAngle = 290.0F;
        sesameLevel = "";
        Title = "";
        SubTile = "";
        mContext = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ATSemicircleProgressView);
        SemicircleSize = typedArray.getDimensionPixelSize(R.styleable.ATSemicircleProgressView_semicircleSize, dp2px(100));
        SemicirclelineSize = typedArray.getDimensionPixelSize(R.styleable.ATSemicircleProgressView_semicirclelineSize, dp2px(3));
        SemicircleBackgroundlineSize = typedArray.getDimensionPixelSize(R.styleable.ATSemicircleProgressView_semicirclebackgroundlineSize, dp2px(3));
        backgroundLineColor = typedArray.getColor(R.styleable.ATSemicircleProgressView_semicirclebackgroundLineColor, getResources().getColor(R.color._C7B8A4));
        frontLineColor = typedArray.getColor(R.styleable.ATSemicircleProgressView_semicirclefrontLineColor, getResources().getColor(R.color._505050));
        titleColor = typedArray.getColor(R.styleable.ATSemicircleProgressView_semicircletitleColor, getResources().getColor(R.color._505050));
        subtitleColor = typedArray.getColor(R.styleable.ATSemicircleProgressView_semicirclesubtitleColor, getResources().getColor(R.color._808080));
        semicircletitleSize = typedArray.getDimensionPixelSize(R.styleable.ATSemicircleProgressView_semicircletitleSize, sp2px(20.0F));
        semicirclesubtitleSize = typedArray.getDimensionPixelSize(R.styleable.ATSemicircleProgressView_semicirclesubtitleSize, sp2px(17.0F));
        Title = typedArray.getString(R.styleable.ATSemicircleProgressView_semicircletitleText);
        SubTile = typedArray.getString(R.styleable.ATSemicircleProgressView_semicirclesubtitleText);
        if (TextUtils.isEmpty(Title)) {
            Title = "";
        }

        if (TextUtils.isEmpty(SubTile)) {
            SubTile = "";
        }

        mMiddleArcPaint = new Paint(1);
        mMiddleArcPaint.setStrokeWidth((float)SemicirclelineSize);
        mMiddleArcPaint.setColor(backgroundLineColor);
        mMiddleArcPaint.setStyle(Style.STROKE);
        mMiddleArcPaint.setStrokeCap(Cap.ROUND);
        mMiddleArcPaint.setAlpha(90);
        mTextPaint = new Paint(1);
        mTextPaint.setColor(titleColor);
        mTextPaint.setTextAlign(Align.CENTER);
        mTextPaint2 = new Paint(1);
        mTextPaint2.setColor(subtitleColor);
        mTextPaint2.setTextAlign(Align.CENTER);
        mArcProgressPaint = new Paint(1);
        mArcProgressPaint.setStrokeWidth((float)SemicircleBackgroundlineSize);
//        mArcProgressPaint.setColor(frontLineColor);
        mArcProgressPaint.setShader(new LinearGradient(0, 0, 500, 500, Color.parseColor("#F9B49B"), frontLineColor,CLAMP));
        mArcProgressPaint.setStyle(Style.STROKE);
        mArcProgressPaint.setStrokeCap(Cap.ROUND);
        mBitmapPaint = new Paint();
        mBitmapPaint.setStyle(Style.FILL);
        mBitmapPaint.setAntiAlias(true);
        pos = new float[2];
        tan = new float[2];
        matrix = new Matrix();
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getSubTile() {
        return SubTile;
    }

    public void setSubTile(String subTile) {
        SubTile = subTile;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int minimumWidth = getSuggestedMinimumWidth();
        int minimumHeight = getSuggestedMinimumHeight();
        int computedWidth = resolveMeasured(widthMeasureSpec, minimumWidth);
        int computedHeight = resolveMeasured(heightMeasureSpec, minimumHeight);
        setMeasuredDimension(computedWidth, computedHeight);
    }

    private int resolveMeasured(int measureSpec, int desired) {
        int specSize = MeasureSpec.getSize(measureSpec);
        int result;
        switch(MeasureSpec.getMode(measureSpec)) {
            case -2147483648:
                result = Math.min(specSize, desired);
                break;
            case 0:
                result = desired;
                break;
            case 1073741824:
            default:
                result = specSize;
        }

        return result;
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        maxWidth = w;
        maxHeight = h;
        width = SemicircleSize;
        radius = width / 2;
        mMiddleRect = new RectF((float)(maxWidth / 2 - radius), (float)(maxHeight / 2 - radius), (float)(maxWidth / 2 + radius), (float)(maxHeight / 2 + radius));
        mMiddleProgressRect = new RectF((float)(maxWidth / 2 - radius), (float)(maxHeight / 2 - radius), (float)(maxWidth / 2 + radius), (float)(maxHeight / 2 + radius));
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawMiddleArc(canvas);
        drawCenterText(canvas);
        drawRingProgress(canvas);
    }

    private void drawRingProgress(Canvas canvas) {
        Path path = new Path();
        path.addArc(mMiddleProgressRect, 125.0F, mCurrentAngle);
        PathMeasure pathMeasure = new PathMeasure(path, false);
        pathMeasure.getPosTan(pathMeasure.getLength() * 1.0F, pos, tan);
        matrix.reset();
        canvas.drawPath(path, mArcProgressPaint);
    }

    private void drawCenterText(Canvas canvas) {
        mTextPaint.setTextSize((float)semicircletitleSize);
        canvas.drawText(Title, (float)(maxWidth / 2), (float)(maxHeight / 2 - dp2px(10)), mTextPaint);
        mTextPaint2.setTextSize((float)semicirclesubtitleSize);
        canvas.drawText(SubTile, (float)(maxWidth / 2), (float)(maxHeight / 2 + dp2px(10)), mTextPaint2);
        canvas.drawText(sesameLevel, (float)(maxWidth / 2), (float)(radius + maxHeight / 2), mTextPaint2);
    }

    private void drawMiddleArc(Canvas canvas) {
        canvas.drawArc(mMiddleRect, 125.0F, 290.0F, false, mMiddleArcPaint);
    }

    public void setSesameValues(int values, int totel) {
        if (values >= 0) {
            mMaxNum = values;
//            sesameLevel = values + "/" + totel;
            mTotalAngle = (float)values / (float)totel * 290.0F;
            startAnim();
        }

    }

    public void startAnim() {
        ValueAnimator mAngleAnim = ValueAnimator.ofFloat(new float[]{mCurrentAngle, mTotalAngle});
        mAngleAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        mAngleAnim.setDuration(1000L);
        mAngleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ATSemicircleProgressView.this.mCurrentAngle = (Float)valueAnimator.getAnimatedValue();
                ATSemicircleProgressView.this.postInvalidate();
            }
        });
        mAngleAnim.start();
        ValueAnimator mNumAnim = ValueAnimator.ofInt(new int[]{mMinNum, mMaxNum});
        mNumAnim.setDuration(1000L);
        mNumAnim.setInterpolator(new LinearInterpolator());
        mNumAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                ATSemicircleProgressView.this.mMinNum = (Integer)valueAnimator.getAnimatedValue();
                ATSemicircleProgressView.this.postInvalidate();
            }
        });
        mNumAnim.start();
    }

    public int dp2px(int values) {
        float density = getResources().getDisplayMetrics().density;
        return (int)((float)values * density + 0.5F);
    }

    public int sp2px(float spValue) {
        float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int)(spValue * fontScale + 0.5F);
    }
}