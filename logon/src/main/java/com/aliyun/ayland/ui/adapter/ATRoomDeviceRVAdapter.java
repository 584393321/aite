package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATDeviceBean;
import com.aliyun.ayland.interfaces.ATOnBindRVItemClickListener;
import com.anthouse.xuhui.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class ATRoomDeviceRVAdapter extends RecyclerView.Adapter{
    private Activity mContext;
    private boolean bind;
    private List<ATDeviceBean> list = new ArrayList<>();
    private RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.at_home_ico_shebeigongyong)
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    public ATRoomDeviceRVAdapter(Activity context, boolean bind) {
        mContext = context;
        this.bind = bind;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_room_device, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setData(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setLists(List<ATDeviceBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTvName;
        private ImageView imgDevice,mImgRight;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mTvName = itemView.findViewById(R.id.tv_name);
            imgDevice = itemView.findViewById(R.id.img_device);
            mImgRight = itemView.findViewById(R.id.img_right);
            mImgRight.setImageDrawable(bind ? mContext.getResources().getDrawable(R.drawable.at_home_btn_tjbdsb) : mContext.getResources().getDrawable(R.drawable.atico_glfj_shanchu));
        }

        public void setData(int position) {
            mTvName.setText(TextUtils.isEmpty(list.get(position).getNickName()) ?
                    list.get(position).getProductName() : list.get(position).getNickName());
            Glide.with(mContext).load(list.get(position).getProductImage()).apply(options).into(imgDevice);
            mImgRight.setOnClickListener(view -> {
                if (lis != null) {
                    lis.onItemClick(view, list.get(position).getIotId(), position);
                }
            });
        }
    }

    protected ATOnBindRVItemClickListener lis;

    public void setOnRVClickListener(ATOnBindRVItemClickListener lis) {
        this.lis = lis;
    }
}