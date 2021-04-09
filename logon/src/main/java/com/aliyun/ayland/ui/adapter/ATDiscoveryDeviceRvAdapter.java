package com.aliyun.ayland.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aliyun.ayland.data.Category;
import com.aliyun.ayland.data.ATCategoryTitle;
import com.aliyun.ayland.data.ATLocalDevice;
import com.aliyun.ayland.data.ATLocalDeviceSearcher;
import com.aliyun.ayland.data.ATLocalDeviceTitle;
import com.aliyun.ayland.ui.viewholder.ATCategoryTitleViewHolder;
import com.aliyun.ayland.ui.viewholder.ATCategoryViewHolder;
import com.aliyun.ayland.ui.viewholder.ATLocalDeviceSearcherViewHolder;
import com.aliyun.ayland.ui.viewholder.ATLocalDeviceTitleViewHolder;
import com.aliyun.ayland.ui.viewholder.ATLocalDeviceViewHolder;
import com.aliyun.ayland.ui.viewholder.ATSettableViewHolder;
import com.anthouse.xuhui.R;

import java.util.LinkedList;
import java.util.List;

public class ATDiscoveryDeviceRvAdapter extends RecyclerView.Adapter<ATSettableViewHolder> {
    private static final int TYPE_TITLE_LOCAL_DEVICE = 0;
    private static final int TYPE_LOCAL_DEVICE = 1;
    private static final int TYPE_LOCAL_DEVICE_SEARCHER = 2;
    private static final int TYPE_TITLE_CATEGORY = 3;
    private static final int TYPE_CATEGORY = 4;

    private List<Object> data;
    private int indexOfLocalDeviceSearcher = 1;

    public ATDiscoveryDeviceRvAdapter() {
        data = new LinkedList<>();
        data.add(new ATLocalDeviceTitle());
        data.add(new ATLocalDeviceSearcher());
        data.add(new ATCategoryTitle());
        indexOfLocalDeviceSearcher = 1;
    }

    public void addLocalDevice(ATLocalDevice ATLocalDevice) {
        data.add(indexOfLocalDeviceSearcher, ATLocalDevice);
        notifyItemInserted(indexOfLocalDeviceSearcher);
        indexOfLocalDeviceSearcher++;
    }

    public void clearLocalDevices() {
        // maybe no local device
        if (indexOfLocalDeviceSearcher == 1) {
            return;
        }

        int oldIndex = indexOfLocalDeviceSearcher;

        while (indexOfLocalDeviceSearcher > 1) {
            data.remove(1);
            indexOfLocalDeviceSearcher--;
        }

        notifyItemRangeRemoved(indexOfLocalDeviceSearcher, oldIndex - indexOfLocalDeviceSearcher);
    }

    public void addCategory(List<Category> categories) {
        int index = getItemCount();
        data.addAll(categories);
        notifyItemInserted(index);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = data.get(position);
        if (item instanceof ATLocalDeviceTitle) {
            return TYPE_TITLE_LOCAL_DEVICE;
        } else if (item instanceof ATLocalDevice) {
            return TYPE_LOCAL_DEVICE;
        } else if (item instanceof ATLocalDeviceSearcher) {
            return TYPE_LOCAL_DEVICE_SEARCHER;
        } else if (item instanceof ATCategoryTitle) {
            return TYPE_TITLE_CATEGORY;
        } else {
            return TYPE_CATEGORY;
        }
    }

    @Override
    public ATSettableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (null == parent) {
            return null;
        }
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;

        if (TYPE_TITLE_LOCAL_DEVICE == viewType) {
            view = inflater.inflate(R.layout.at_item_local_device_title, parent, false);
            return new ATLocalDeviceTitleViewHolder(view);
        } else if (TYPE_LOCAL_DEVICE == viewType) {
            view = inflater.inflate(R.layout.at_item_local_device, parent, false);
            return new ATLocalDeviceViewHolder(view);
        } else if (TYPE_LOCAL_DEVICE_SEARCHER == viewType) {
            view = inflater.inflate(R.layout.at_item_local_device_scan, parent, false);
            return new ATLocalDeviceSearcherViewHolder(view);
        } else if (TYPE_TITLE_CATEGORY == viewType) {
            view = inflater.inflate(R.layout.at_item_local_device_category_title, parent, false);
            return new ATCategoryTitleViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.at_item_local_device_category, parent, false);
            return new ATCategoryViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(ATSettableViewHolder holder, int position) {
        Object item = data.get(position);
        holder.setData(item, position, data.size());
    }
}