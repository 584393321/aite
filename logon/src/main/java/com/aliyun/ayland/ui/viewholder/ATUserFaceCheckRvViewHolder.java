package com.aliyun.ayland.ui.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.anthouse.xuhui.R;

/**
 * @author guikong on 18/4/8.
 */

public class ATUserFaceCheckRvViewHolder extends ATSettableViewHolder {
    private TextView mTvVillageName;
    private Context mContext;

    public ATUserFaceCheckRvViewHolder(View view) {
        super(view);
        mContext = view.getContext();
        mTvVillageName = view.findViewById(R.id.tv_village_name);
        ATAutoUtils.autoSize(view);
    }

    @Override
    public void setData(Object object, int position, int count) {
        if (object instanceof String) {
            mTvVillageName.setText((String)object);
        }
    }
}