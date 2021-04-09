package com.aliyun.ayland.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aliyun.ayland.data.ATLocalDevice;
import com.aliyun.ayland.ui.viewholder.ATLocalDeviceSearcherViewHolder;
import com.aliyun.ayland.ui.viewholder.ATLocalDeviceViewHolder;
import com.aliyun.ayland.ui.viewholder.ATSceneDeleteViewHolder;
import com.aliyun.ayland.ui.viewholder.ATSettableViewHolder;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.List;

public class ATDiscoveryDeviceLocalRvAdapter extends RecyclerView.Adapter<ATSettableViewHolder> {
    private static final int TYPE_LOCAL_DEVICE_SEARCHER = 0;
    private static final int TYPE_LOCAL_DEVICE = 1;
    private static final int TYPE_LOCAL_OPEN_WIFI = 2;
    private List<ATLocalDevice> list;
    private int indexOfLocalDeviceSearcher;

    public ATDiscoveryDeviceLocalRvAdapter() {
        list = new ArrayList<>();
        list.add(new ATLocalDevice());
        list.add(new ATLocalDevice());
        indexOfLocalDeviceSearcher = 1;
    }

    public void addLocalDevice(ATLocalDevice ATLocalDevice) {
        list.add(indexOfLocalDeviceSearcher, ATLocalDevice);
        notifyItemInserted(indexOfLocalDeviceSearcher);
        indexOfLocalDeviceSearcher++;
    }

    public void clearLocalDevices() {
        if (indexOfLocalDeviceSearcher == 1) {
            return;
        }
        int oldIndex = indexOfLocalDeviceSearcher;
        while (indexOfLocalDeviceSearcher > 1) {
            list.remove(1);
            indexOfLocalDeviceSearcher--;
        }
        notifyItemRangeRemoved(indexOfLocalDeviceSearcher, oldIndex - indexOfLocalDeviceSearcher);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_LOCAL_DEVICE_SEARCHER;
        } else if (position == list.size() - 1) {
            return TYPE_LOCAL_OPEN_WIFI;
        } else {
            return TYPE_LOCAL_DEVICE;
        }
    }

    @NonNull
    @Override
    public ATSettableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        if (TYPE_LOCAL_DEVICE_SEARCHER == viewType) {
            view = inflater.inflate(R.layout.at_item_local_device_scan, parent, false);
            return new ATLocalDeviceSearcherViewHolder(view);
        } else if (TYPE_LOCAL_OPEN_WIFI == viewType) {
            view = inflater.inflate(R.layout.at_item_local_device_wifi, parent, false);
            return new ATSceneDeleteViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.at_item_local_device, parent, false);
            return new ATLocalDeviceViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ATSettableViewHolder holder, int position) {
        Object item = list.get(position);
        holder.setData(item, position, list.size());
    }
}