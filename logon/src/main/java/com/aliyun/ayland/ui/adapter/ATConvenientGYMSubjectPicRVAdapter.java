package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATGymProjectBean;
import com.anthouse.xuhui.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class ATConvenientGYMSubjectPicRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATGymProjectBean> list = new ArrayList<>();
    private int selected_position = 0;
    private int temp_position = 0;

    public ATConvenientGYMSubjectPicRVAdapter(Activity context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_gym_subject_pic, parent, false);
        return new ConvenientGYMSubjectPicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ConvenientGYMSubjectPicViewHolder convenientGYMSubjectPicViewHolder = (ConvenientGYMSubjectPicViewHolder) holder;
        convenientGYMSubjectPicViewHolder.setData(position);
    }

    @Override
    public int getItemCount() {
        return list.size() > 0 ? 1 : 0;
    }

    public void setLists(List<ATGymProjectBean> list, int position) {
        selected_position = position;
        temp_position = position;
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ConvenientGYMSubjectPicViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mRlContent;
        private TextView mTvName;
        private ImageView mImg;
        private View view;

        private ConvenientGYMSubjectPicViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mRlContent = itemView.findViewById(R.id.rl_content);
            mTvName = itemView.findViewById(R.id.tv_name);
            mImg = itemView.findViewById(R.id.img);
            view = itemView.findViewById(R.id.view);
        }

        public void setData(int position) {
            mTvName.setText(list.get(position).getProjectName());
            if (position == selected_position) {
                Glide.with(mContext).load(list.get(position).getCheckedIcon()).into(mImg);
            }else {
                Glide.with(mContext).load(list.get(position).getUnCheckedIcon()).into(mImg);
            }

            mRlContent.setOnClickListener(view-> {
                temp_position = selected_position;
                selected_position = position;
                notifyItemChanged(temp_position);
                notifyItemChanged(selected_position);
                mOnItemClickListener.onItemClick(view, position);
            });
        }
    }
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
