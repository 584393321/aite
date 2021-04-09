package com.aliyun.ayland.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATDeviceTcaList;
import com.anthouse.xuhui.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class ATLinkageDragGVAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Context mContext;
    private List<ATDeviceTcaList> list = new ArrayList<>();
    private int select = 0;
    private String allDevice = "";
    private RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.at_home_ld_ico_defaultsetting)
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    public ATLinkageDragGVAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    public void setList(List<ATDeviceTcaList> itemList) {
        list.clear();
        list.addAll(itemList);
        notifyDataSetChanged();
    }

    @Override
    public ATDeviceTcaList getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = mInflater.inflate(R.layout.at_item_gv_linkage_control, parent, false);
            vh.mText = convertView.findViewById(R.id.text);
            vh.mImg = convertView.findViewById(R.id.img);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.mText.setText(list.get(position).getCategoryName());
        if (position == 0) {
            vh.mImg.setImageResource(R.drawable.at_home_ld_ico_binfenheizi);
        } else if (position == 1) {
            vh.mImg.setImageResource(R.drawable.at_icon_s_ld_dingshi);
        }  else if (position == 2) {
            vh.mImg.setImageResource(R.drawable.at_icon_s_ld_che);
        } else if (position == 3) {
            vh.mImg.setImageResource(R.drawable.at_icon_s_ld_xiaoren);
        } else {
            Glide.with(mContext).load(list.get(position).getImageUrl()).apply(options).into(vh.mImg);
        }
        ATAutoUtils.autoSize(convertView);
        return convertView;
    }

    class ViewHolder {
        private TextView mText;
        private ImageView mImg;
    }
}