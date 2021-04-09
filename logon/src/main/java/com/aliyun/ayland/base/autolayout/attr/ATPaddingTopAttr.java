package com.aliyun.ayland.base.autolayout.attr;

import android.view.View;

/**
 * Created by zhy on 15/12/5.
 */
public class ATPaddingTopAttr extends ATAutoAttr {
    public ATPaddingTopAttr(int pxVal, int baseWidth, int baseHeight) {
        super(pxVal, baseWidth, baseHeight);
    }

    @Override
    protected int attrVal() {
        return ATAttrs.PADDING_TOP;
    }

    @Override
    protected boolean defaultBaseWidth() {
        return false;
    }

    @Override
    protected void execute(View view, int val) {
        int l = view.getPaddingLeft();
        int t = val;
        int r = view.getPaddingRight();
        int b = view.getPaddingBottom();
        view.setPadding(l, t, r, b);
    }
}