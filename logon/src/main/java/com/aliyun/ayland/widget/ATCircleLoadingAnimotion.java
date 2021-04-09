package com.aliyun.ayland.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.anthouse.xuhui.R;


public class ATCircleLoadingAnimotion extends View {

	private Paint paint;

	private int width;

	private int bigTime = 10000;
	// bar的宽度
	private int barWidth;
	// 通过改变这个属性让动画启动 , 最小是0， 最大360，
	private int progress;

	private int barColor = Color.parseColor("#F2E8D8");
	private int reachBarColor = Color.parseColor("#51EDA3");

	private int radius;
	private int cX, cY;

	private AnimotionEnd aniEnd;

	public interface AnimotionEnd {
		void end();
	}

	public void setAnimotionEnd(AnimotionEnd aniEnd) {
		this.aniEnd = aniEnd;
	}

	public ATCircleLoadingAnimotion(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.circle_loading_bar, defStyleAttr, 0);
		barWidth = (int) a.getDimension(R.styleable.circle_loading_bar_bar_width, 10);
		String barColorString = a.getString(R.styleable.circle_loading_bar_bar_color);
		String reachBarColorString = a.getString(R.styleable.circle_loading_bar_bar_color);
		// Color.parseColor("#AFAFAF")
//		if (TextUtils.isEmpty(barColorString)) {
//			barColor = Color.parseColor("#AFAFAF");
//		} else {
//			barColor = Color.parseColor(barColorString);
//		}
//		if (TextUtils.isEmpty(reachBarColorString)) {
//			reachBarColor = Color.parseColor("#009688");
//		} else {
//			reachBarColor = Color.parseColor(barColorString);
//		}

		a.recycle();
		init();
	}

	public ATCircleLoadingAnimotion(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ATCircleLoadingAnimotion(Context context) {
		this(context, null);
	}

	ObjectAnimator sin;

	private boolean endGoneFlag = true;

	// 动画启动
	public synchronized void animotionStart(int durtion) {
		if (durtion != -1) {
			bigTime = durtion;
		} else {
			bigTime = 10000;
		}
		Log.e("xhc", "动画start --> " + durtion);
		animotionStop();
		this.setVisibility(View.VISIBLE);
		sin = ObjectAnimator.ofInt(this, "progress", 0, 360);
		sin.setRepeatCount(0);
		sin.setDuration(bigTime);
		sin.start();
		sin.setInterpolator(new LinearInterpolator());
		sin.addListener(ani);
	}

	// 动画启动
	public synchronized void animotionStart(int durtion, boolean goneFlag) {
		animotionStart(durtion);
		endGoneFlag = goneFlag;
	}

	AnimatorListener ani = new AnimatorListener() {

		@Override
		public void onAnimationStart(Animator animation) {
			// TODO Auto-generated method stub
			Log.e("xhc", "------------onAnimationStart-------------");
		}

		@Override
		public void onAnimationRepeat(Animator animation) {
			// TODO Auto-generated method stub
			Log.e("xhc", "------------onAnimationRepeat-------------");
		}

		@Override
		public void onAnimationEnd(Animator animation) {
			// TODO Auto-generated method stub
			Log.e("xhc", "------------onAnimationEnd-------------");
			// bigTime = 10000;
			if (sin != null) {
				sin.removeAllListeners();
				if (endGoneFlag) {
					ATCircleLoadingAnimotion.this.setVisibility(View.GONE);
				}
				if (aniEnd != null) {
					aniEnd.end();
				}
			}
		}

		@Override
		public void onAnimationCancel(Animator animation) {
			Log.e("xhc", "------------onAnimationCancel-------------");
		}
	};

	// 动画停止
	public void animotionStop() {
		if (sin != null) {
			sin.cancel();
			bigTime = 10000;
			sin = null;
		}
	}

	@Override
	public void setVisibility(int visibility) {
		super.setVisibility(visibility);
		if (visibility == View.GONE) {
			animotionStop();
		}
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
		invalidate();
	}

	private RectF oval = null;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int measureHeight = getMeasuredHeight();
		int measureWidth = getMeasuredWidth();
		width = Math.min(measureHeight, measureWidth);
		cX = measureWidth / 2;
		cY = measureHeight / 2;
		int paddingLeft = getPaddingLeft();
		int paddingRight = getPaddingRight();
		int paddingTop = getPaddingTop();
		int paddingBottom = getPaddingBottom();
		int padding = Math.max((paddingLeft + paddingRight), (paddingTop + paddingBottom));
		width -= padding;
		radius = width / 2 - barWidth;
		if (measureHeight > measureWidth) {
			// 高度大于宽度
			oval = new RectF(0 + barWidth + paddingLeft, (measureHeight - measureWidth) / 2 + barWidth + paddingTop,
					measureWidth - barWidth - paddingRight,
					(measureHeight - (measureHeight - measureWidth) / 2) - barWidth - paddingBottom);
		} else if (measureHeight < measureWidth) {
			// 宽度大于高度
			oval = new RectF((measureWidth - measureHeight) / 2 + barWidth + paddingLeft, 0 + barWidth + paddingTop,
					(measureWidth - (measureWidth - measureHeight) / 2) - barWidth - paddingRight,
					measureHeight - barWidth - paddingBottom);
		} else {
			oval = new RectF(0 + barWidth + paddingLeft, 0 + barWidth + paddingTop, width - barWidth - paddingRight,
					width - barWidth - paddingBottom);
		}
	}

	private void init() {
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStrokeWidth(barWidth);
		paint.setStyle(Style.STROKE);
	}

	// 是否成功
	boolean success = false;

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawBar(canvas);
		drawReachBar(canvas);
	}

	private void drawBar(Canvas canvas) {
		paint.setColor(barColor);
		canvas.drawCircle(cX, cY, radius, paint);
	}

	private void drawReachBar(Canvas canvas) {
		paint.setColor(reachBarColor);
		canvas.drawArc(oval, -90, progress, false, paint);
	}
}
