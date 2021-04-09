package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATMediaBean;
import com.aliyun.ayland.ui.activity.ATLocalVideoActivity;
import com.anthouse.xuhui.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.entity.LocalMedia;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ATSaveRecordRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATMediaBean> list = new ArrayList<>();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.getDefault());
    private RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.color.bar_grey)
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    public ATSaveRecordRVAdapter(Activity context) {
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_save_record, parent, false);
        SaveRecordHolder saveRecordHolder = new SaveRecordHolder(view);
        return saveRecordHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SaveRecordHolder saveRecordHolder = (SaveRecordHolder) holder;
        saveRecordHolder.setData(position);
        saveRecordHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int adapterPosition = saveRecordHolder.getAdapterPosition();
                onItemLongCilckListener.onLongClick(adapterPosition);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<ATMediaBean> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class SaveRecordHolder extends RecyclerView.ViewHolder {
        private ImageView mImgThumbnail;
        private ImageView mImgPlay;
        private TextView mTvName;
        private TextView mTvTime;
        private ImageButton mIbCellphone;

        public SaveRecordHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mImgThumbnail = itemView.findViewById(R.id.img_thumbnail);
            mImgPlay = itemView.findViewById(R.id.img_play);
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvTime = itemView.findViewById(R.id.tv_time);
            mIbCellphone = (ImageButton) itemView.findViewById(R.id.img_cellphone);
        }

        public void setData(int position) {
            Glide.with(mContext)
                    .load(list.get(position).getPath())
                    .apply(options)
                    .into(mImgThumbnail);
            String[] split = list.get(position).getMediaName().split("_");
            if (split.length > 2) {
                mTvName.setText(split[0]);
                mTvTime.setText(sdf.format(new Date(Long.parseLong(split[1]))));
                if(".mp4".equals(split[2])){
                    mImgPlay.setVisibility(View.VISIBLE);
                    mIbCellphone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent1 = new Intent(mContext, ATLocalVideoActivity.class);
                            intent1.putExtra("url", list.get(position).getPath());
                            intent1.putExtra("image", "network");
                            intent1.putExtra("fromLocal", true);
                            mContext.startActivity(intent1);
                        }
                    });
                }else {
                    mImgPlay.setVisibility(View.GONE);
                    mIbCellphone.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            LocalMedia localMedia = new LocalMedia();
                            localMedia.setPath(list.get(position).getPath());
                            localMedia.setPictureType("image");
                            List<LocalMedia> mediaList = new ArrayList<>();
                            mediaList.add(localMedia);
                            PictureSelector.create(mContext).externalPicturePreview(0,mediaList);
                        }
                    });
                }
            }
        }
    }
    //长按的点击事件
    public interface OnItemLongCilckListener {
        void onLongClick(int position);
    }

    private OnItemLongCilckListener onItemLongCilckListener;

    public void addItemLongClickListener (OnItemLongCilckListener onItemLongCilckListener){
        this.onItemLongCilckListener = onItemLongCilckListener;
    }
}