package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATConvenientLifeCommunityBean;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ATCommunityPublishCanseeRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private HashSet<Integer> checkSet = new HashSet<>();
    private List<ATConvenientLifeCommunityBean> list = new ArrayList<>();

    public ATCommunityPublishCanseeRVAdapter(Activity context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_community_publish_cansee, parent, false);
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

    public void setList(List<ATConvenientLifeCommunityBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public HashSet<Integer> getCheckSet() {
        return checkSet;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private CheckBox checkBox;
        private RelativeLayout rlContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            rlContent = itemView.findViewById(R.id.rl_content);
            checkBox = itemView.findViewById(R.id.checkBox);
        }

        public void setData(int position) {
            tvName.setText(list.get(position).getCommunityName());
            checkBox.setOnClickListener(view -> {
                if (checkBox.isChecked()) {
                    checkSet.add(position);
                } else {
                    checkSet.remove(position);
                }
                notifyItemChanged(position);
            });
            rlContent.setOnClickListener(view -> {
                if (checkBox.isChecked()) {
                    checkSet.remove(position);
                } else {
                    checkSet.add(position);
                }
                notifyItemChanged(position);
            });
            checkBox.setChecked(checkSet.contains(position));
        }
    }
}
