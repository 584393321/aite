package com.aliyun.ayland.ui.viewholder;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATEventString;
import com.aliyun.ayland.data.ATSceneBean;
import com.aliyun.ayland.ui.activity.ATLinkageAddActivity;
import com.anthouse.xuhui.R;

import org.greenrobot.eventbus.EventBus;

import static com.aliyun.ayland.ui.fragment.ATLinkageFragment.REQUEST_CODE_EDIT_LINKAGE;


/**
 * @author guikong on 18/4/8.
 */
public class ATLinkageViewHolder extends ATSettableViewHolder {
    private ImageView mImgIcon;
    private TextView mTvName, tvKeyword;
    private RelativeLayout mRlContent;
    private Activity mContext;

    public ATLinkageViewHolder(View view) {
        super(view);
        ATAutoUtils.autoSize(view);
        mContext = (Activity) view.getContext();
        mImgIcon = view.findViewById(R.id.img_icon);
        mTvName = view.findViewById(R.id.tv_name);
        tvKeyword = view.findViewById(R.id.tv_keyword);
        mRlContent = view.findViewById(R.id.rl_content);
    }

    @Override
    public void setData(Object object, int position, int count) {
        if (!(object instanceof ATSceneBean)) {
            return;
        }
        ATSceneBean ATSceneBean = (ATSceneBean) object;
        mTvName.setText(ATSceneBean.getName());
        mRlContent.setOnClickListener((view) -> {
            mContext.startActivityForResult(new Intent(mContext, ATLinkageAddActivity.class).putExtra("sceneId", ATSceneBean.getSceneId()), REQUEST_CODE_EDIT_LINKAGE);
        });
        mImgIcon.setOnClickListener((view) -> {
            EventBus.getDefault().post(new ATEventString("ATLinkageFamilyFragment", ATSceneBean.getSceneId()));
        });
    }
}