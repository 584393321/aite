package com.aliyun.ayland.ui.viewholder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATEventBoolean;
import com.aliyun.ayland.data.ATEventInteger2;
import com.aliyun.ayland.data.ATFamilyDeviceBean;
import com.aliyun.ayland.data.ATPublicDeviceBean;
import com.aliyun.ayland.data.ATSceneBean1;
import com.aliyun.ayland.data.ATShortcutBean;
import com.anthouse.xuhui.R;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;


/**
 * @author guikong on 18/4/8.
 */

public class ATShortcutViewHolder extends ATSettableViewHolder {
    private TextView mTvName;
    private Context mContext;
    private ImageView mImg, mImgAddDelete;
    private RelativeLayout mRlContent;
    private int status;
    private OnRecyclerViewItemClickListener mOnItemClickListener;

    public ATShortcutViewHolder(View view, int status, OnRecyclerViewItemClickListener mOnItemClickListener) {
        super(view);
        ATAutoUtils.autoSize(view);
        mContext = view.getContext();
        mTvName = view.findViewById(R.id.tv_name);
        mImg = view.findViewById(R.id.img);
        mImgAddDelete = view.findViewById(R.id.img_add_delete);
        mRlContent = view.findViewById(R.id.rl_content);
        this.mOnItemClickListener = mOnItemClickListener;
        this.status = status;
    }

    @Override
    public void setData(Object object, int position, int count) {
        if (status == 0) {
            mImgAddDelete.setVisibility(View.GONE);
            mRlContent.setClickable(true);
            mRlContent.setEnabled(true);
        }else {
            mImgAddDelete.setVisibility(View.VISIBLE);
            mRlContent.setClickable(false);
            mRlContent.setEnabled(false);
        }
        if (object instanceof ATShortcutBean) {
            mTvName.setText(((ATShortcutBean)object).getItemName());
            Glide.with(mContext).load(((ATShortcutBean)object).getItemIcon()).into(mImg);
            mImgAddDelete.setImageResource(R.drawable.atico_glfj_sckj);
            mImgAddDelete.setOnClickListener(view -> {
                  EventBus.getDefault().post(new ATEventInteger2("ATHomeShortcutActivity", getAdapterPosition(), 0));
//                list.remove(getLayoutPosition());
//                notifyItemRemoved(getLayoutPosition());
//                mOnItemClickListener.onItemClick(view, getLayoutPosition());
            });
            mRlContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(view, position - 1);
                }
            });
        }else if (object instanceof ATFamilyDeviceBean) {
            mTvName.setText(((ATFamilyDeviceBean)object).getProductName());
            Glide.with(mContext).load(((ATFamilyDeviceBean)object).getProductImage()).into(mImg);
            mImgAddDelete.setImageResource(((ATFamilyDeviceBean)object).isAdd() ? R.drawable.atico_glfj_sckj : R.drawable.atico_glfj_jiaru);
            mImgAddDelete.setOnClickListener(view -> {
                EventBus.getDefault().post(new ATEventBoolean("ATHomeShortcutActivity", position, !((ATFamilyDeviceBean)object).isAdd()));
//                ((ATFamilyDeviceBean)object).setAdd(!((ATFamilyDeviceBean)object).isAdd());
//                notifyItemChanged(position);
//                mOnItemClickListener.onItemClick(((ATFamilyDeviceBean)object).isAdd(), position);
            });
        }else if (object instanceof ATPublicDeviceBean) {
            mTvName.setText(((ATPublicDeviceBean)object).getName());
            Glide.with(mContext).load(((ATPublicDeviceBean)object).getImageUrl()).into(mImg);
            mImgAddDelete.setImageResource(((ATPublicDeviceBean)object).isAdd() ? R.drawable.atico_glfj_sckj : R.drawable.atico_glfj_jiaru);
            mImgAddDelete.setOnClickListener(view -> {
                EventBus.getDefault().post(new ATEventBoolean("ATHomeShortcutActivity", position, !((ATPublicDeviceBean)object).isAdd()));
//                ((ATPublicDeviceBean)object).setAdd(!((ATPublicDeviceBean)object).isAdd());
//                notifyItemChanged(position);
//                mOnItemClickListener.onItemClick(((ATPublicDeviceBean)object).isAdd(), position);
            });
        }else if (object instanceof ATSceneBean1) {
            mTvName.setText(((ATSceneBean1)object).getSceneName());
            Glide.with(mContext).load(((ATSceneBean1)object).getSceneIcon()).into(mImg);
            mImgAddDelete.setImageResource(((ATSceneBean1)object).isAdd() ? R.drawable.atico_glfj_sckj : R.drawable.atico_glfj_jiaru);
            mImgAddDelete.setOnClickListener(view -> {
                EventBus.getDefault().post(new ATEventBoolean("ATHomeShortcutActivity", position, !((ATSceneBean1)object).isAdd()));
//                ((ATSceneBean1)object).setAdd(!((ATSceneBean1)object).isAdd());
//                notifyItemChanged(position);
//                mOnItemClickListener.onItemClick(((ATSceneBean1)object).isAdd(), position);
            });
        }

    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }
}
