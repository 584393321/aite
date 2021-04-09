package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

public class ATVisitorHistoryRVAdapter extends RecyclerView.Adapter implements Filterable {
    private Activity mContext;
    private List<String> list = new ArrayList<>();
    private List<String> mSourceList = new ArrayList<>();

    public ATVisitorHistoryRVAdapter(Activity context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_visitor_history, parent, false);
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

    public void setLists(List<String> list) {
        this.list.clear();
        this.list.addAll(list);
        mSourceList.clear();
        mSourceList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    list = mSourceList;
                } else {
                    List<String> filteredList = new ArrayList<>();
                    for (String str : mSourceList) {
                        //这里根据需求，添加匹配规则
                        if (str.contains(charString)) {
                            filteredList.add(str);
                        }
                    }

                    list = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                list = (ArrayList<String>) filterResults.values;
                //刷新数据
                notifyDataSetChanged();
                notifyItemRangeChanged(0, list.size());
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvVisitorHistory;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mTvVisitorHistory = itemView.findViewById(R.id.tv_visitor_history);
        }

        public void setData(int position) {
            mTvVisitorHistory.setText(list.get(position));
            mTvVisitorHistory.setOnClickListener(view -> mOnItemClickListener.onItemClick(list, position));
        }
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(List<String> list, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
