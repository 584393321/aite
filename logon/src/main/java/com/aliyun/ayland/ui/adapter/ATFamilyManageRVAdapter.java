package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATFamilyMenberBean;
import com.aliyun.ayland.ui.activity.ATFamilyManageMemberActivity;
import com.aliyun.ayland.utils.ATResourceUtils;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

public class ATFamilyManageRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATFamilyMenberBean> list = new ArrayList<>();

    public ATFamilyManageRVAdapter(Activity context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_family_manage, parent, false);
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

    public void setLists(List<ATFamilyMenberBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mRlContent;
        private TextView mTvName, mTvStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mRlContent = itemView.findViewById(R.id.rl_content);
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvStatus = itemView.findViewById(R.id.tv_status);
        }

        public void setData(int position) {
            mTvName.setText(list.get(position).getNickname());
            mTvStatus.setText(ATResourceUtils.getResIdByName(String.format(mContext.getString(R.string.at_householdtype_), list.get(position).getHouseholdtype()), ATResourceUtils.ResourceType.STRING));
            mRlContent.setOnClickListener(view -> {
//                if (TextUtils.isEmpty(list.get(position).getPhone())) {
                    mContext.startActivity(new Intent(mContext, ATFamilyManageMemberActivity.class)
                            .putExtra("nickname", list.get(position).getNickname())
                            .putExtra("firstname", list.get(position).getFirstname())
                            .putExtra("lastname", list.get(position).getLastname())
                            .putExtra("householdtype", list.get(position).getHouseholdtype())
                            .putExtra("memberPersonCode", list.get(position).getPersonCode())
                            .putExtra("birthDate", list.get(position).getBirthDate())
                            .putExtra("idNumber", list.get(position).getIdNumber())
                            .putExtra("sex", list.get(position).getSex())
                            .putExtra("openid", list.get(position).getOpenid()));
//                }else {
//                    mContext.startActivity(new Intent(mContext, FamilyManageEntryActivity.class)
//                            .putExtra("nickname", list.get(position).getNickname())
//                            .putExtra("firstname", list.get(position).getFirstname())
//                            .putExtra("lastname", list.get(position).getLastname())
//                            .putExtra("householdtype", list.get(position).getHouseholdtype())
//                            .putExtra("memberPersonCode", list.get(position).getPersonCode())
//                            .putExtra("birthDate", list.get(position).getBirthDate())
//                            .putExtra("idNumber", list.get(position).getIdNumber())
//                            .putExtra("sex", list.get(position).getSex())
//                            .putExtra("openid", list.get(position).getOpenid()));
//                }
            });
        }
    }
}