package com.aliyun.ayland.ui.viewholder;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATSceneManualTitle;
import com.anthouse.xuhui.R;

/**
 * @author guikong on 18/4/8.
 */

public class ATSceneTitleIfViewHolder extends ATSettableViewHolder {
    private TextView tvIfAuto;
    private Activity mContext;

    public ATSceneTitleIfViewHolder(View view) {
        super(view);
        mContext = (Activity) view.getContext();
        tvIfAuto = view.findViewById(R.id.tv_if_auto);
        ATAutoUtils.autoSize(view);
    }

    @Override
    public void setData(Object object, int position, int count) {
        if (!(object instanceof ATSceneManualTitle)) {
            return;
        }
        ATSceneManualTitle mATSceneManualTitle = (ATSceneManualTitle) object;
        if (mATSceneManualTitle.isAuto()) {
            tvIfAuto.setText(mContext.getString(R.string.at_linkage));
            tvIfAuto.setTextColor(mContext.getResources().getColor(R.color._2779CB));
            tvIfAuto.setBackground(mContext.getResources().getDrawable(R.drawable.shape_12pxc9dae9));
        } else {
            tvIfAuto.setText(mContext.getString(R.string.at_manual));
            tvIfAuto.setTextColor(mContext.getResources().getColor(R.color._CA903A));
            tvIfAuto.setBackground(mContext.getResources().getDrawable(R.drawable.shape_12pxf6dcb1));
        }
    }
}
