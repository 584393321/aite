package com.aliyun.ayland.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aliyun.ayland.data.ATRankingListBean.RankListBean;
import com.aliyun.ayland.data.ATRankingListBean.UserRankInfoBean;
import com.aliyun.ayland.interfaces.ATOnRecyclerViewItemClickListener;
import com.aliyun.ayland.ui.viewholder.ATRankingListContentRvViewHolder;
import com.aliyun.ayland.ui.viewholder.ATRankingListTitleRvViewHolder;
import com.aliyun.ayland.ui.viewholder.ATSettableViewHolder;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

public class ATRankingListRvAdapter extends RecyclerView.Adapter<ATSettableViewHolder> {
    private static final int TYPE_TITLE = 0;
    private static final int TYPE_CONTENT = 1;
    private List<Object> data = new ArrayList<>();

    public void notifyPosition(int position) {
        if (data.get(position) instanceof RankListBean) {
            if (((RankListBean) data.get(position)).getAgreeStatus() == 0) {
                ((RankListBean) data.get(position)).setAgreeStatus(1);
                ((RankListBean) data.get(position)).setAgree(((RankListBean) data.get(position)).getAgree() + 1);
            } else {
                ((RankListBean) data.get(position)).setAgreeStatus(0);
                ((RankListBean) data.get(position)).setAgree(((RankListBean) data.get(position)).getAgree() - 1);
            }
        } else {
            if (((UserRankInfoBean) data.get(position)).getAgreeStatus() == 0) {
                ((UserRankInfoBean) data.get(position)).setAgreeStatus(1);
                ((UserRankInfoBean) data.get(position)).setAgree(((UserRankInfoBean) data.get(position)).getAgree() + 1);
            } else {
                ((UserRankInfoBean) data.get(position)).setAgreeStatus(0);
                ((UserRankInfoBean) data.get(position)).setAgree(((UserRankInfoBean) data.get(position)).getAgree() - 1);
            }
        }
        notifyItemChanged(position);
    }

    public void setList(List<RankListBean> rankList, UserRankInfoBean userRankInfo) {
        data.clear();
        data.add(userRankInfo);
        data.addAll(rankList);
        notifyDataSetChanged();
    }

    public List<Object> getData() {
        return data;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = data.get(position);
        if (item instanceof UserRankInfoBean) {
            return TYPE_TITLE;
        } else {
            return TYPE_CONTENT;
        }
    }

    @NonNull
    @Override
    public ATSettableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        if (TYPE_TITLE == viewType) {
            view = inflater.inflate(R.layout.at_item_rv_ranking_list_title, parent, false);
            return new ATRankingListTitleRvViewHolder(view, mOnItemClickListener);
        } else {
            view = inflater.inflate(R.layout.at_item_rv_ranking_list_content, parent, false);
            return new ATRankingListContentRvViewHolder(view, mOnItemClickListener);
        }
    }

    @Override
    public void onBindViewHolder(ATSettableViewHolder holder, int position) {
        Object item = data.get(position);
        holder.setData(item, position, data.size());
    }

    private ATOnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(ATOnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}