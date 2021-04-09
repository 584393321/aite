package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATTmallSceneBean;
import com.aliyun.ayland.interfaces.ATOnRecyclerViewItemClickListener;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

public class ATTmallVoiceWizardRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATTmallSceneBean> list = new ArrayList<>();
    private Drawable drawable;
    ;

    public ATTmallVoiceWizardRVAdapter(Activity context) {
        mContext = context;
        drawable = mContext.getResources().getDrawable(R.drawable.atico_tianmaojingling);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //根据返回类型选择不同item
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_voice_wizard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setData(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setLists(List<ATTmallSceneBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mRlContent;
        private TextView mTvName, tvAssociated;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mRlContent = itemView.findViewById(R.id.rl_content);
            mTvName = itemView.findViewById(R.id.tv_name);
            tvAssociated = itemView.findViewById(R.id.tv_associated);
        }

        public void setData(int position) {
            mTvName.setText(list.get(position).getSceneName());
            if (!TextUtils.isEmpty(list.get(position).getSceneId())) {
                if (list.get(position).getRelationStatus() == 1) {
                    mTvName.setTextColor(mContext.getResources().getColor(R.color._333333));
                    tvAssociated.setText(mContext.getResources().getString(R.string.at_associated));
                    tvAssociated.setTextColor(mContext.getResources().getColor(R.color._86523C));
                    tvAssociated.setCompoundDrawables(drawable, null, null, null);
                } else {
                    mTvName.setTextColor(mContext.getResources().getColor(R.color._999999));
                    tvAssociated.setText(mContext.getResources().getString(R.string.at_not_associated));
                    tvAssociated.setTextColor(mContext.getResources().getColor(R.color._999999));
                    tvAssociated.setCompoundDrawables(null, null, null, null);
                }
            } else {
                mTvName.setTextColor(mContext.getResources().getColor(R.color._999999));
                tvAssociated.setText(mContext.getResources().getString(R.string.at_not_create));
                tvAssociated.setTextColor(mContext.getResources().getColor(R.color._999999));
                tvAssociated.setCompoundDrawables(null, null, null, null);
            }
            mRlContent.setOnClickListener(v -> mOnItemClickListener.onItemClick(v, list.get(position), position));
        }
    }

    private ATOnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(ATOnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}