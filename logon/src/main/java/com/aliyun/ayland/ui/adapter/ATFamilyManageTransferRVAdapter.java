package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATFamilyMenberBean;
import com.aliyun.ayland.utils.ATResourceUtils;
import com.anthouse.xuhui.R;

import java.util.List;

public class ATFamilyManageTransferRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private int check_position = -1;
    private List<ATFamilyMenberBean> list;

    public ATFamilyManageTransferRVAdapter(Activity context, List<ATFamilyMenberBean> otherMenberList) {
        mContext = context;
        list = otherMenberList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_family_manage_transfer, parent, false);
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

    public int getCheck_position() {
        return check_position;
    }

    public void setLists(List<ATFamilyMenberBean> roomList) {
        this.list.clear();
        this.list.addAll(roomList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvName, mTvStatus;
        private RelativeLayout rlContent;
        private CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvStatus = itemView.findViewById(R.id.tv_status);
            checkBox = itemView.findViewById(R.id.checkBox);
            rlContent = itemView.findViewById(R.id.rl_content);
        }

        public void setData(int position) {
            mTvName.setText(list.get(position).getNickname());
            rlContent.setOnClickListener(view -> {
                check_position = position;
                notifyDataSetChanged();
            });
            checkBox.setChecked(position == check_position);

            if (TextUtils.isEmpty(list.get(position).getHouseholdtype()))
                mTvStatus.setText(R.string.at_other);
            else
                mTvStatus.setText(ATResourceUtils.getResIdByName(String.format(mContext.getString(R.string.at_householdtype_), list.get(position).getHouseholdtype()), ATResourceUtils.ResourceType.STRING));
        }
    }
}