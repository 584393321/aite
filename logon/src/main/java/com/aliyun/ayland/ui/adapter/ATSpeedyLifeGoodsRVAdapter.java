package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATGoodsBean;
import com.aliyun.ayland.interfaces.ATOnRecyclerViewItemClickListener;
import com.anthouse.xuhui.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class ATSpeedyLifeGoodsRVAdapter extends RecyclerView.Adapter {
    private Activity mContext;
    private List<ATGoodsBean> list = new ArrayList<>();
    private RequestOptions options = new RequestOptions()
            .centerCrop()
            .placeholder(R.drawable.at_bjsh_pic_dianshi_d)
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    public ATSpeedyLifeGoodsRVAdapter(Activity context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_speedy_life_goods, parent, false);
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

    public void setLists(List<ATGoodsBean> list, int pageNum) {
        if (pageNum == 0)
            this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout mLlContent;
        private TextView mTextView, mTvDetail, tvPrice;
        private ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mLlContent = itemView.findViewById(R.id.ll_content);
            mImageView = itemView.findViewById(R.id.img);
            mTextView = itemView.findViewById(R.id.tv_name);
            mTvDetail = itemView.findViewById(R.id.tv_detail);
            tvPrice = itemView.findViewById(R.id.tv_price);
        }

        public void setData(int position) {
            mTextView.setText(list.get(position).getCommodityName());
            mTvDetail.setText(list.get(position).getDescription());
            tvPrice.setText(getSpannableString(list.get(position).getPrice()));
            Glide.with(mContext).load(list.get(position).getImage()).apply(options).into(mImageView);
            mLlContent.setOnClickListener(view -> mOnItemClickListener.onItemClick(view, list.get(position), position));
        }
    }

    private SpannableString getSpannableString(String price) {
        SpannableString s = new SpannableString(String.format(mContext.getString(R.string.at_price_1), price));
        s.setSpan(new AbsoluteSizeSpan(ATAutoUtils.getPercentWidthSize(42)), 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        s.setSpan(new AbsoluteSizeSpan(ATAutoUtils.getPercentWidthSize(54)), 1, price.length() + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        s.setSpan(new AbsoluteSizeSpan(ATAutoUtils.getPercentWidthSize(28)), price.length() + 1, s.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        s.setSpan(new ForegroundColorSpan(Color.parseColor("#FFEAA520")), 0, price.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#FF999999")), price.length() + 1, s.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return s;
    }

    private ATOnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(ATOnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
