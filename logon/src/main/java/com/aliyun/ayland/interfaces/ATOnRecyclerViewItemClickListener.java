package com.aliyun.ayland.interfaces;

import android.view.View;

public interface ATOnRecyclerViewItemClickListener<T> {
    void onItemClick(View view, T t, int position);
}