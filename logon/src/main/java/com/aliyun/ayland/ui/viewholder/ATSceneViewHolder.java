package com.aliyun.ayland.ui.viewholder;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATSceneName;
import com.anthouse.xuhui.R;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;


/**
 * @author guikong on 18/4/8.
 */
public class ATSceneViewHolder extends ATSettableViewHolder {
    private LinearLayout llContent;
    private TextView tvContent, tvTitle;
    private Activity mContext;
    private SwipeMenuRecyclerView mMenuRecyclerView;

    public ATSceneViewHolder(View view) {
        super(view);
        ATAutoUtils.autoSize(view);
        mContext = (Activity) view.getContext();
        llContent = view.findViewById(R.id.ll_content);
        tvTitle = view.findViewById(R.id.tv_title);
        tvContent = view.findViewById(R.id.tv_content);
    }

    @Override
    public void setData(Object object, int position, int count) {
        if (!(object instanceof ATSceneName)) {
            return;
        }
        ATSceneName ATSceneName = (ATSceneName) object;
        tvTitle.setText(ATSceneName.getName());
        tvContent.setText(ATSceneName.getContent());
//        rlContent.setOnClickListener(view -> {
//            Intent intent = new Intent();
//            intent.putExtra("uri", ATSceneName.getUri());
//            switch (ATSceneName.getUri()) {
//                case "trigger/timer":
//                    intent.putExtra("flowType", 1);
//                    intent.putExtra("params", ATSceneName.getParams());
//                    intent.setClass(mContext, ATLinkageTimingActivity.class);
//                    break;
//                case "condition/timeRange":
//                    intent.putExtra("flowType", 2);
//                    intent.setClass(mContext, ATLinkageTimingActivity.class);
//                    break;
//            }
//            mContext.startActivityForResult(intent, REQUEST_CODE_REPLACE_CONDITION);
//        });
    }
}