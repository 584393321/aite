package com.aliyun.ayland.ui.viewholder;

import android.content.Context;
import android.view.View;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATSceneDelete;

/**
 * @author guikong on 18/4/8.
 */
public class ATSceneDeleteViewHolder extends ATSettableViewHolder {
    private final Context mContext;

    public ATSceneDeleteViewHolder(View view) {
        super(view);
        ATAutoUtils.autoSize(view);
        mContext = view.getContext();
    }

    @Override
    public void setData(Object object, int position, int count) {
        if (!(object instanceof ATSceneDelete)) {
            return;
        }
    }
}
