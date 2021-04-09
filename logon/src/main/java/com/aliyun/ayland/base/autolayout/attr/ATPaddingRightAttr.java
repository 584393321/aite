package com.aliyun.ayland.base.autolayout.attr;

import android.view.View;

/**
 * Created by zhy on 15/12/5.
 */
public class ATPaddingRightAttr extends ATAutoAttr {
    public ATPaddingRightAttr(int pxVal, int baseWidth, int baseHeight) {
        super(pxVal, baseWidth, baseHeight);
    }

    @Override
    protected int attrVal() {
        return ATAttrs.PADDING_RIGHT;
    }

    @Override
    protected boolean defaultBaseWidth() {
        return true;
    }

    @Override
    protected void execute(View view, int val) {
        int l = view.getPaddingLeft();
        int t = view.getPaddingTop();
        int r = val;
        int b = view.getPaddingBottom();
        view.setPadding(l, t, r, b);
    }
}