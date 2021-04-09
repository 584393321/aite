package com.aliyun.ayland.base.autolayout;

import android.view.View;

import com.aliyun.ayland.base.autolayout.attr.ATAutoAttr;

import java.util.ArrayList;
import java.util.List;

public class AutoLayoutInfo {
    private List<ATAutoAttr> mATAutoAttrs = new ArrayList<ATAutoAttr>();

    public void addAttr(ATAutoAttr ATAutoAttr) {
        mATAutoAttrs.add(ATAutoAttr);
    }

    public void fillAttrs(View view) {
        for (ATAutoAttr ATAutoAttr : mATAutoAttrs) {
            ATAutoAttr.apply(view);
        }
    }

    @Override
    public String toString() {
        return "ATAutoLayoutInfo{" +
                "mATAutoAttrs=" + mATAutoAttrs +
                '}';
    }
}