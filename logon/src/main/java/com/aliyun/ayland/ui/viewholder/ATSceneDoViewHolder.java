package com.aliyun.ayland.ui.viewholder;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATLocalDevice;
import com.aliyun.ayland.ui.activity.ATLinkageAddConditionActivity;
import com.anthouse.xuhui.R;

/**
 * @author guikong on 18/4/8.
 */
public class ATSceneDoViewHolder extends ATSettableViewHolder {
    private RelativeLayout rlContent;
    private ImageView imgAdd;
    private final Context mContext;

    public ATSceneDoViewHolder(View view) {
        super(view);
        mContext = view.getContext();
        rlContent = view.findViewById(R.id.rl_content);
        imgAdd = view.findViewById(R.id.img_add_delete);
        ATAutoUtils.autoSize(view);
    }

    @Override
    public void setData(Object object, int position, int count) {
        if (!(object instanceof ATLocalDevice)) {
            return;
        }
        final ATLocalDevice ATLocalDevice = (ATLocalDevice) object;
        rlContent.setOnClickListener(view -> {
            mContext.startActivity(new Intent(mContext, ATLinkageAddConditionActivity.class));
        });
    }
}
