package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATBrightnessLightBean;
import com.aliyun.ayland.widget.ATSwitchButton;
import com.anthouse.xuhui.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ATLinkageMorningRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATBrightnessLightBean> list = new ArrayList<>();
    private HashSet<Integer> set = new HashSet<>();
    private RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.at_home_ico_shebeigongyong)
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    public ATLinkageMorningRVAdapter(Activity context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_linkage_morning, parent, false);
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

    public void setLists(List<ATBrightnessLightBean> list) {
        this.list.clear();
        this.list.addAll(list);
        for (int i = 0; i < list.size(); i++) {
            set.add(i);
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvName;
        private ImageView img;
        private ATSwitchButton mSwitchview;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mTvName = itemView.findViewById(R.id.tv_name);
            img = itemView.findViewById(R.id.img);
            mSwitchview = itemView.findViewById(R.id.switchview);
        }

        public void setData(int position) {
            Glide.with(mContext)
                    .load(list.get(position).getProductImage())
                    .apply(options)
                    .into(img);
            mSwitchview.setChecked(set.contains(position));
            mSwitchview.setOnCheckedChangeListener((compoundButton, b) -> {
                if(b){
                    set.add(position);
                }else {
                    set.remove(position);
                }
            });
            mTvName.setText(TextUtils.isEmpty(list.get(position).getNickName()) ? list.get(position).getProductName() : list.get(position).getNickName());
        }
    }

    public HashSet<Integer> getSet() {
        return set;
    }
}