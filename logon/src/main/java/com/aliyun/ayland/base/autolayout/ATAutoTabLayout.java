package com.aliyun.ayland.base.autolayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.utils.ATDimenUtils;
import com.anthouse.xuhui.R;

public class ATAutoTabLayout extends TabLayout {
    private static final int NO_VALID = -1;
    private int mTextSize;
    private boolean mTextSizeBaseWidth = false;

    public ATAutoTabLayout(Context context) {
        this(context, null);
    }

    public ATAutoTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ATAutoTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabLayout,
                defStyleAttr, R.style.Widget_Design_TabLayout);
        int tabTextAppearance = a.getResourceId(R.styleable.TabLayout_tabTextAppearance,
                R.style.TextAppearance_Design_Tab);

        mTextSize = loadTextSizeFromTextAppearance(tabTextAppearance);
        a.recycle();
    }

    private int loadTextSizeFromTextAppearance(int textAppearanceResId) {
        TypedArray a = getContext().obtainStyledAttributes(textAppearanceResId,
                R.styleable.TextAppearance);
        try {
            if (!ATDimenUtils.isPxVal(a.peekValue(R.styleable.TextAppearance_android_textSize)))
                return NO_VALID;
            return a.getDimensionPixelSize(R.styleable.TextAppearance_android_textSize, NO_VALID);
        } finally {
            a.recycle();
        }
    }

    @Override
    public void addTab(Tab tab, int position, boolean setSelected) {
        super.addTab(tab, position, setSelected);
        setUpTabTextSize(tab);
    }

    @Override
    public void addTab(Tab tab, boolean setSelected) {
        super.addTab(tab, setSelected);
        setUpTabTextSize(tab);
    }

    private void setUpTabTextSize(Tab tab) {
        if (mTextSize == NO_VALID || tab.getCustomView() != null) return;

        ViewGroup tabGroup = (ViewGroup) getChildAt(0);
        ViewGroup tabContainer = (ViewGroup) tabGroup.getChildAt(tab.getPosition());
        TextView textView = (TextView) tabContainer.getChildAt(1);
        if (ATAutoUtils.autoed(textView)) {
            return;
        }
        int autoTextSize = 0;
        if (mTextSizeBaseWidth) {
            autoTextSize = ATAutoUtils.getPercentWidthSize(mTextSize);
        } else {
            autoTextSize = ATAutoUtils.getPercentHeightSize(mTextSize);
        }
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, autoTextSize);
    }
}