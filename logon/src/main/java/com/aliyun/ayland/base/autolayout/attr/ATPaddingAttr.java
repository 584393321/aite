package com.aliyun.ayland.base.autolayout.attr;

import android.view.View;

/**
 * Created by zhy on 15/12/5.
 */
public class ATPaddingAttr extends ATAutoAttr {
    public ATPaddingAttr(int pxVal, int baseWidth, int baseHeight) {
        super(pxVal, baseWidth, baseHeight);
    }

    @Override
    protected int attrVal() {
        return ATAttrs.PADDING;
    }

    @Override
    public void apply(View view) {
        int l, t, r, b;
        if (useDefault()) {
            l = r = getPercentWidthSize();
            t = b = getPercentHeightSize();
            view.setPadding(l, t, r, b);
            return;
        }
        super.apply(view);
    }

    @Override
    protected boolean defaultBaseWidth() {
        return false;
    }

    @Override
    protected void execute(View view, int val) {
        view.setPadding(val, val, val, val);
    }
}