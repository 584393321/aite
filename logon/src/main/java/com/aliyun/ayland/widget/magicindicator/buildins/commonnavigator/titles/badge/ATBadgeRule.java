package com.aliyun.ayland.widget.magicindicator.buildins.commonnavigator.titles.badge;

/**
 * 角标的定位规则
 * Created by hackware on 2016/7/19.
 */
public class ATBadgeRule {
    private ATBadgeAnchor mAnchor;
    private int mOffset;

    public ATBadgeRule(ATBadgeAnchor anchor, int offset) {
        mAnchor = anchor;
        mOffset = offset;
    }

    public ATBadgeAnchor getAnchor() {
        return mAnchor;
    }

    public void setAnchor(ATBadgeAnchor anchor) {
        mAnchor = anchor;
    }

    public int getOffset() {
        return mOffset;
    }

    public void setOffset(int offset) {
        mOffset = offset;
    }
}
