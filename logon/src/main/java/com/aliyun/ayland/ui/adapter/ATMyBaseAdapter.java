package com.aliyun.ayland.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aliyun.ayland.interfaces.ATOnRecyclerViewItemClickListener;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


public abstract class ATMyBaseAdapter<T, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {
    protected List<T> list = new ArrayList<T>();
    protected Context context;
    protected ATOnRecyclerViewItemClickListener<T> lis;

    public ATMyBaseAdapter(List<T> list, Context context) {
        this.context = context;
        if (list != null) {
            this.list.addAll(list);
        }
    }


    public void setOnRecyclerViewItemClickListener(ATOnRecyclerViewItemClickListener<T> lis) {
        this.lis = lis;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void myNotifyItemChanged(int position, T t) {
        if (position < 0 || position > list.size()) {
            return;
        }
        list.set(position, t);
        super.notifyItemChanged(position);
    }

    public void refreshAllData(List<T> list) {
        this.list.clear();
        if (list != null) {
            this.list.addAll(list);
        }
        this.notifyDataSetChanged();
    }

    @Override
    public abstract V onCreateViewHolder(ViewGroup container, int type);

    @Override
    public abstract void onBindViewHolder(V holder, int position);

    protected void displayImageView(ImageView img, String url) {
        Glide.with(context).load(url).into(img);
    }
}
