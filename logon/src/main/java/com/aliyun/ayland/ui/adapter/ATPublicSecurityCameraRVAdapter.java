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
import com.aliyun.ayland.data.ATPublicCameraBean;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

public class ATPublicSecurityCameraRVAdapter extends RecyclerView.Adapter {

    private Activity mContext;
    private List<ATPublicCameraBean> list = new ArrayList<>();

    public ATPublicSecurityCameraRVAdapter(Activity context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_public_security_camera, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setData(position);
    }

    public void setLists(List<ATPublicCameraBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mRlContent;
        private TextView mTvName, mTvAddress;
        private ImageView mImgDevice;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mRlContent = itemView.findViewById(R.id.rl_content);
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvAddress = itemView.findViewById(R.id.tv_address);
        }

        public void setData(int position) {
            mTvName.setText(list.get(position).getName());
//            mLlContent.setOnClickListener(view -> {
//                mContext.startActivity(new Intent(mContext, ATIntelligentMonitorActivity.class)
//                        .putExtra("productKey", list.get(position).getProductKey())
//                        .putExtra("sign","rtmp")
//                        .putExtra("iotId", list.get(position).getDeviceId()));
//            });
            mRlContent.setOnClickListener(view -> {
                mOnItemClickListener.onItemClick(position);
            });
        }
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
