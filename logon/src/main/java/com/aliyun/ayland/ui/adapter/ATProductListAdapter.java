package com.aliyun.ayland.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aliyun.ayland.data.ATProduct;
import com.aliyun.ayland.ui.viewholder.ATHeaderViewHolder;
import com.aliyun.ayland.ui.viewholder.ATProductViewHolder;
import com.anthouse.xuhui.R;

import java.util.LinkedList;
import java.util.List;

/**
 * @author guikong on 18/4/10.
 */

public class ATProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int VIEW_TYPE_HEAD = 0;
    private final static int VIEW_TYPE_PRODUCT = 1;
    private List<ATProduct> data;

    public ATProductListAdapter() {
        data = new LinkedList<>();
    }

    public void addCategory(List<ATProduct> ATProducts) {
        int index = getItemCount();
        data.addAll(ATProducts);
        notifyItemInserted(index);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_TYPE_HEAD;
        }

        return VIEW_TYPE_PRODUCT;
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (null == parent) {
            return null;
        }
        Context context = parent.getContext();

        if (VIEW_TYPE_HEAD == viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.at_deviceadd_product_list_header_recycle_item, parent, false);
            return new ATHeaderViewHolder(view);
        }

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.at_deviceadd_product_list_product_recycle_item, parent, false);
        return new ATProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (VIEW_TYPE_HEAD == getItemViewType(position)) {
            return;
        }
        ATProduct ATProduct = data.get(position - 1);
        int count = getItemCount();
        if (holder instanceof ATProductViewHolder) {
            ((ATProductViewHolder) holder).setProduct(ATProduct, position, count);
        }
    }
}
