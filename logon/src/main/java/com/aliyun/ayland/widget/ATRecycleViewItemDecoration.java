package com.aliyun.ayland.widget;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ATRecycleViewItemDecoration extends RecyclerView.ItemDecoration {
    private int marginBottom = -1;

    public ATRecycleViewItemDecoration(int marginBottom) {
        this.marginBottom = marginBottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if(marginBottom != -1){
            outRect.bottom = marginBottom;
        }
    }
}
