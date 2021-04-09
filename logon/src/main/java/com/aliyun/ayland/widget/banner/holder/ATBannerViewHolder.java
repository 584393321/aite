package com.aliyun.ayland.widget.banner.holder;

import android.content.Context;
import android.view.View;

public interface ATBannerViewHolder<T> {

    View createView(Context context);

    void onBind(Context context, int position, T data);
}
