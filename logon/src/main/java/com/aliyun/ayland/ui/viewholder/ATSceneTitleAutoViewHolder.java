package com.aliyun.ayland.ui.viewholder;

import android.view.View;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;

/**
 * @author guikong on 18/4/8.
 */

public class ATSceneTitleAutoViewHolder extends ATSettableViewHolder {

    public ATSceneTitleAutoViewHolder(View view) {
        super(view);
        ATAutoUtils.autoSize(view);
    }

    @Override
    public void setData(Object object, int position, int count) {
    }
}
