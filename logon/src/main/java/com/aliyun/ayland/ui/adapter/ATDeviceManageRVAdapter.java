package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATDeviceManageBean;
import com.aliyun.ayland.ui.activity.ATDeviceManageSharedToActivity;
import com.anthouse.xuhui.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ATDeviceManageRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private boolean checkable;
    private int type;
    private HashSet<Integer> checkSet = new HashSet<>();
    private List<ATDeviceManageBean> list = new ArrayList<>();
    private RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.at_home_ico_shebeigongyong)
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    public ATDeviceManageRVAdapter(Activity context, int type) {
        mContext = context;
        this.type = type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_unbind_device, parent, false);
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

    public void setList(List<ATDeviceManageBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void setCheckable(boolean checkable) {
        this.checkable = checkable;
        notifyDataSetChanged();
    }

    public HashSet<Integer> getCheckSet() {
        return checkSet;
    }

    public void remove(int position) {
        list.remove(position);
        checkSet.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private ImageView img;
        private CheckBox checkBox;
        private ImageView imgJump, imgBind;
        private RelativeLayout rlContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            img = itemView.findViewById(R.id.img);
            tvName = itemView.findViewById(R.id.tv_name);
            imgBind = itemView.findViewById(R.id.img_bind);
            rlContent = itemView.findViewById(R.id.rl_content);
            checkBox = itemView.findViewById(R.id.checkBox);
            imgJump = itemView.findViewById(R.id.img_jump);
        }

        public void setData(int position) {
            tvName.setText(list.get(position).getProductName());
            Glide.with(mContext)
                    .load(list.get(position).getProductImage())
                    .apply(options)
                    .into(img);
            switch (type){
                case 1:
                    imgBind.setVisibility(View.VISIBLE);
                    imgJump.setVisibility(View.GONE);
                    imgBind.setImageDrawable(mContext.getResources().getDrawable(R.drawable.at_home_btn_tjbdsb));
                    imgBind.setOnClickListener(view -> mOnItemClickListener.onItemClick(type, position));
                    break;
                case 2:
                    imgBind.setVisibility(View.GONE);
                    imgJump.setVisibility(View.GONE);
                    rlContent.setOnClickListener(view -> {
                        if(checkBox.isChecked()){
                            checkSet.remove(position);
                        }else {
                            checkSet.add(position);
                        }
                        notifyItemChanged(position);
                    });
                    if(checkable){
                        rlContent.setClickable(true);
                        checkBox.setVisibility(View.VISIBLE);
                    }else {
                        rlContent.setClickable(false);
                        checkBox.setVisibility(View.GONE);
                    }
                    break;
                case 3:
                    imgBind.setVisibility(View.GONE);
                    imgJump.setVisibility(View.VISIBLE);
                    rlContent.setOnClickListener(view -> mContext.startActivity(new Intent(mContext, ATDeviceManageSharedToActivity.class)
                            .putParcelableArrayListExtra("sharedUsers", (ArrayList<? extends Parcelable>) list.get(position).getSharedUsers())
                            .putExtra("iotId", list.get(position).getIotId())));
                    break;
                case 4:
                    imgJump.setVisibility(View.GONE);
                    imgBind.setVisibility(View.VISIBLE);
                    imgBind.setImageDrawable(mContext.getResources().getDrawable(R.drawable.atico_glfj_shanchu));
                    imgBind.setOnClickListener(view -> mOnItemClickListener.onItemClick(type, position));
                    break;
            }
            checkBox.setChecked(checkSet.contains(position));
        }
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(int type, int position);
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
