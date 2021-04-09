package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATShareSpaceProjectBean;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ATChooseGardenSubjectRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private HashSet<Integer> set = new HashSet<>();
    private List<ATShareSpaceProjectBean> allList = new ArrayList<>();
    private Handler handler = new Handler();
    private double discount;

    public ATChooseGardenSubjectRVAdapter(Activity context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_choose_garden_subject, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setData(position);
    }

    @Override
    public int getItemCount() {
        return allList.size() + 1;
    }

    public void setLists(List<ATShareSpaceProjectBean> allList, HashSet<Integer> set, double discount) {
        this.allList.clear();
        this.allList.addAll(allList);
        this.set.clear();
        this.set.addAll(set);
        this.discount = discount;
        notifyDataSetChanged();
    }

    public HashSet<Integer> getSet() {
        return set;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvDiscount;
        private CheckBox mCbSelect;

        private ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDiscount = itemView.findViewById(R.id.tv_discount);
            mCbSelect = itemView.findViewById(R.id.cb_select);
        }

        public void setData(int position) {
            if (position == 0) {
                tvName.setText(mContext.getString(R.string.at_choose_all_subject));
                tvName.setTextColor(mContext.getResources().getColor(R.color._86523C));
                if (discount == 10.0) {
                    tvDiscount.setVisibility(View.GONE);
                } else {
                    tvDiscount.setVisibility(View.VISIBLE);
                    tvDiscount.setText(String.format(mContext.getString(R.string.at_choose_all_subject1), String.valueOf(discount)));//不可去
                }
                mCbSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
                });//不可去
                mCbSelect.setChecked(set.size() == allList.size());
                mCbSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        for (int i = 1; i < allList.size() + 1; i++) {
                            set.add(i);
                        }
                    } else {
                        for (int i = 1; i < allList.size() + 1; i++) {
                            set.remove(i);
                        }
                    }
                    handler.post(ATChooseGardenSubjectRVAdapter.this::notifyDataSetChanged);
                });
            } else {
                tvName.setTextColor(mContext.getResources().getColor(R.color._333333));
                tvName.setText(allList.get(position - 1).getProjectName());
                tvDiscount.setVisibility(View.GONE);
                mCbSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
                });//不可去
                mCbSelect.setChecked(set.contains(position));
                mCbSelect.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        set.add(position);
                    } else {
                        set.remove(position);
                    }
                    handler.post(ATChooseGardenSubjectRVAdapter.this::notifyDataSetChanged);
                });
            }
        }
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    private interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String key, String appointment);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}