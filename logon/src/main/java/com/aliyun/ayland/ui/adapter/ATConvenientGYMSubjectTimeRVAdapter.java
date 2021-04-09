package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATGymProjectBean;
import com.aliyun.ayland.data.ATGymSubjectTimeBean;
import com.aliyun.ayland.ui.viewholder.ATSceneDeleteViewHolder;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

public class ATConvenientGYMSubjectTimeRVAdapter extends RecyclerView.Adapter {
    private static final int TYPE_TITLE = 0;
    private static final int TYPE_TITLE2 = 1;
    private static final int TYPE_CONTENT = 2;
    private Activity mContext;
    private String price = "";
    private List<ATGymSubjectTimeBean> list = new ArrayList<>();
    private List<String> selectedList = new ArrayList<>();
    private boolean yujia = false;

    public ATConvenientGYMSubjectTimeRVAdapter(Activity context) {
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (yujia && position == 0) {
            return TYPE_TITLE;
        } else if ((yujia && position == 1) || (!yujia && position == 0)) {
            return TYPE_TITLE2;
        } else {
            return TYPE_CONTENT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (TYPE_TITLE == viewType) {
            view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_gym_subject_time_title, parent, false);
            return new ATSceneDeleteViewHolder(view);
        } else if (TYPE_TITLE2 == viewType) {
            view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_gym_subject_time_title2, parent, false);
            return new ATSceneDeleteViewHolder(view);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_gym_subject_time, parent, false);
            return new ConvenientGYMSubjectPicViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ConvenientGYMSubjectPicViewHolder) {
            ConvenientGYMSubjectPicViewHolder convenientGYMSubjectPicViewHolder = (ConvenientGYMSubjectPicViewHolder) holder;
            convenientGYMSubjectPicViewHolder.setData(position);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setLists(ATGymProjectBean mATGymProjectBean, List<String> selectedList) {
        this.list.clear();
        if ("瑜伽".equals(mATGymProjectBean.getProjectName())) {
            yujia = true;
            list.add(new ATGymSubjectTimeBean());
        } else {
            yujia = false;
        }
        list.add(new ATGymSubjectTimeBean());
        if (mATGymProjectBean.getTime()!=null)
            this.list.addAll(JSONArray.parseArray(mATGymProjectBean.getTime().toJSONString(), ATGymSubjectTimeBean.class));
        this.price = mATGymProjectBean.getPrice();
        this.selectedList.clear();
        if (selectedList != null) {
            this.selectedList.addAll(selectedList);
        }
        notifyDataSetChanged();
    }

    public class ConvenientGYMSubjectPicViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mRlContent;
        private TextView mTvTime, mTvPrice;

        private ConvenientGYMSubjectPicViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mRlContent = itemView.findViewById(R.id.rl_content);
            mTvTime = itemView.findViewById(R.id.tv_time);
            mTvPrice = itemView.findViewById(R.id.tv_price);
        }

        public void setData(int position) {
            mTvTime.setText(list.get(position).getTime());
            mTvPrice.setText(String.format(mContext.getResources().getString(R.string.at_price_), price));
            if (-1 == list.get(position).getUseStatus()) {
                mTvTime.setTextColor(mContext.getResources().getColor(R.color._CCCCCC));
                mTvPrice.setTextColor(mContext.getResources().getColor(R.color._CCCCCC));
            } else {
                if (selectedList.contains(list.get(position).getTime())) {
                    mTvTime.setTextColor(mContext.getResources().getColor(R.color._EAA520));
                    mTvPrice.setTextColor(mContext.getResources().getColor(R.color._EAA520));
                } else {
                    mTvTime.setTextColor(mContext.getResources().getColor(R.color._666666));
                    mTvPrice.setTextColor(mContext.getResources().getColor(R.color._666666));
                }
            }
            mRlContent.setOnClickListener(view -> {
                if (list.get(position).getUseStatus() != -1) {
                    if (selectedList.contains(list.get(position).getTime())) {
                        selectedList.remove(list.get(position).getTime());
                        mTvTime.setTextColor(mContext.getResources().getColor(R.color._666666));
                        mTvPrice.setTextColor(mContext.getResources().getColor(R.color._666666));
                    } else {
                        selectedList.add(list.get(position).getTime());
                        mTvTime.setTextColor(mContext.getResources().getColor(R.color._EAA520));
                        mTvPrice.setTextColor(mContext.getResources().getColor(R.color._EAA520));
                    }
                    mOnItemClickListener.onItemClick(view, selectedList);
                }
            });
        }
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, List<String> selectedList);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
