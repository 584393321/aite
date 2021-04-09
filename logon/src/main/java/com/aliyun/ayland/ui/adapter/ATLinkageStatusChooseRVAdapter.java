package com.aliyun.ayland.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.anthouse.xuhui.R;

import java.util.List;

public class ATLinkageStatusChooseRVAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<String> list;
    private int selectPosition;

    public ATLinkageStatusChooseRVAdapter(Context context, List<String> list, int selectPosition) {
        mContext = context;
        this.list = list;
        this.selectPosition = selectPosition;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_linkage_status_choose, parent, false);
        return new LinkageStatusChooseRVHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        LinkageStatusChooseRVHolder linkageStatusChooseRVHolder = (LinkageStatusChooseRVHolder) holder;
        linkageStatusChooseRVHolder.setData(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class LinkageStatusChooseRVHolder extends RecyclerView.ViewHolder {
        private TextView mTvName;
        private CheckBox mCbSelect;
        private RelativeLayout mRlContent;

        private LinkageStatusChooseRVHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mTvName = itemView.findViewById(R.id.tv_name);
            mCbSelect = itemView.findViewById(R.id.cb_select);
            mRlContent = itemView.findViewById(R.id.rl_content);
        }

        public void setData(int position) {
            mTvName.setText(list.get(position));
            mRlContent.setOnClickListener(view -> {
                mOnItemClickListener.onItemClick(view, position);
                selectPosition = position;
                notifyDataSetChanged();
            });
            if (selectPosition == position) {
                mCbSelect.setChecked(true);
            } else {
                mCbSelect.setChecked(false);
            }
        }
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
