package com.aliyun.ayland.widget;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

public class ATSmoothnessLayoutManage extends LinearLayoutManager {
    private double speedRatio = 0.5;

    public ATSmoothnessLayoutManage(Context context, int horizontal, boolean b) {
        super(context, horizontal, b);
    }

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return null;
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int a = super.scrollHorizontallyBy((int)(speedRatio*dx), recycler, state);
        if(a == (int)(speedRatio*dx)){
            return dx;
        }
        return a;
    }
}
