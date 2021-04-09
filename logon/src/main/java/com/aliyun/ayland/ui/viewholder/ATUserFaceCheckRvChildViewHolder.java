package com.aliyun.ayland.ui.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATEventInteger;
import com.aliyun.ayland.data.ATEventString;
import com.aliyun.ayland.data.ATUserFaceCheckBean;
import com.anthouse.xuhui.R;

import org.greenrobot.eventbus.EventBus;

/**
 * @author guikong on 18/4/8.
 */

public class ATUserFaceCheckRvChildViewHolder extends ATSettableViewHolder {
    private Context mContext;
    private View view1;
    private TextView mTvDeviceName, mTvDeviceAddress, mTvFaceStatus, mTvFaceStatusFailed;

    public ATUserFaceCheckRvChildViewHolder(View view) {
        super(view);
        mContext = view.getContext();
        mTvDeviceName = view.findViewById(R.id.tv_device_name);
        mTvDeviceAddress = view.findViewById(R.id.tv_device_address);
        mTvFaceStatus = view.findViewById(R.id.tv_face_status);
        mTvFaceStatusFailed = view.findViewById(R.id.tv_face_status_failed);
        view1 = view.findViewById(R.id.view);
        ATAutoUtils.autoSize(view);
    }

    @Override
    public void setData(Object object, int position, int count) {
        if (!(object instanceof ATUserFaceCheckBean))
            return;
        mTvDeviceName.setText(((ATUserFaceCheckBean) object).getDeviceName());
        mTvDeviceAddress.setText(((ATUserFaceCheckBean) object).getDeviceAddress());
        mTvFaceStatus.setOnClickListener(view -> {
            if (0 == ((ATUserFaceCheckBean) object).getFaceStatus() || -1 == ((ATUserFaceCheckBean) object).getFaceStatus())
                EventBus.getDefault().post(new ATEventInteger("ATUserFaceCheckFragment", position));
        });
        switch (((ATUserFaceCheckBean) object).getFaceStatus()) {
            case -1:
                //失败
                mTvFaceStatus.setText(mContext.getString(R.string.at_fail_to_apply));
                mTvFaceStatus.setTextColor(mContext.getResources().getColor(R.color._86523C));
                mTvFaceStatus.setBackground(mContext.getResources().getDrawable(R.drawable.shape_51px_f2f2f2));
                mTvFaceStatusFailed.setVisibility(View.VISIBLE);
                break;
            case 0:
                //未录入
                mTvFaceStatus.setText(mContext.getString(R.string.at_applying));
                mTvFaceStatus.setTextColor(mContext.getResources().getColor(R.color._86523C));
                mTvFaceStatus.setBackground(mContext.getResources().getDrawable(R.drawable.shape_51px_f2f2f2));
                mTvFaceStatusFailed.setVisibility(View.GONE);
                break;
            case 1:
                //已录入
                mTvFaceStatus.setText(mContext.getString(R.string.at_apply_success));
                mTvFaceStatus.setTextColor(mContext.getResources().getColor(R.color._333333));
                mTvFaceStatus.setBackground(null);
                mTvFaceStatusFailed.setVisibility(View.GONE);
                break;
            case 2:
                //授权中
                mTvFaceStatus.setText(mContext.getString(R.string.at_authorization));
                mTvFaceStatus.setTextColor(mContext.getResources().getColor(R.color._333333));
                mTvFaceStatus.setBackground(null);
                mTvFaceStatusFailed.setVisibility(View.GONE);
                break;
            case 3:
                //设备离线
                mTvFaceStatus.setText(mContext.getString(R.string.at_device_offline));
                mTvFaceStatus.setTextColor(mContext.getResources().getColor(R.color._F15E5E));
                mTvFaceStatus.setBackground(null);
                mTvFaceStatusFailed.setVisibility(View.GONE);
                break;
        }
    }
}