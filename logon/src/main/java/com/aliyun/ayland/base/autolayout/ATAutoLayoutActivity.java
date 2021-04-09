package com.aliyun.ayland.base.autolayout;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

;

/**
 * Created by zhy on 15/11/19.
 */
public class ATAutoLayoutActivity extends AppCompatActivity {
    private static final String LAYOUT_LINEARLAYOUT = "LinearLayout";
    private static final String LAYOUT_FRAMELAYOUT = "FrameLayout";
    private static final String LAYOUT_RELATIVELAYOUT = "RelativeLayout";
    private static final String LAYOUT_RADIOGROUP = "RadioGroup";
    private static final String LAYOUT_CARDVIEW = "CardView";
    private static final String LAYOUT_COLLAPSINGTOOLBARLAYOUT = "CollapsingToolbarLayout";
    private static final String LAYOUT_AUTOAPPBARLAYOUT = "AutoAppBarLayout";
    private static final String LAYOUT_AUTOTOOLBAR = "AutoToolbar";
    private static final String LAYOUT_AUTOTABLAYOUT = "AutoTabLayout";

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        View view = null;
        if (name.equals(LAYOUT_FRAMELAYOUT)) {
            view = new ATAutoFrameLayout(context, attrs);
        }
        if (name.equals(LAYOUT_LINEARLAYOUT)) {
            view = new ATAutoLinearLayout(context, attrs);
        }
        if (name.equals(LAYOUT_RELATIVELAYOUT)) {
            view = new ATAutoRelativeLayout(context, attrs);
        }
        if (name.equals(LAYOUT_RADIOGROUP)) {
            view = new ATAutoRadioGroup(context, attrs);
        }
        if (name.equals(LAYOUT_CARDVIEW)) {
            view = new ATAutoCardView(context, attrs);
        }
        if (name.equals(LAYOUT_COLLAPSINGTOOLBARLAYOUT)) {
            view = new ATAutoCollapsingToolbarLayout(context, attrs);
        }
        if (name.equals(LAYOUT_AUTOAPPBARLAYOUT)) {
            view = new ATAutoAppBarLayout(context, attrs);
        }
        if (name.equals(LAYOUT_AUTOTOOLBAR)) {
            view = new ATAutoToolbar(context, attrs);
        }
        if (name.equals(LAYOUT_AUTOTABLAYOUT)) {
            view = new ATAutoTabLayout(context, attrs);
        }

        if (view != null) return view;

        return super.onCreateView(name, context, attrs);
    }
}