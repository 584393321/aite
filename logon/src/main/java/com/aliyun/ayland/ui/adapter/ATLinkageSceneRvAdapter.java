package com.aliyun.ayland.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aliyun.ayland.data.ATSceneAutoTitle;
import com.aliyun.ayland.data.ATSceneBean;
import com.aliyun.ayland.data.ATSceneDoTitle;
import com.aliyun.ayland.data.ATSceneManualTitle;
import com.aliyun.ayland.ui.viewholder.ATLinkageViewHolder;
import com.aliyun.ayland.ui.viewholder.ATSceneTitleViewHolder;
import com.aliyun.ayland.ui.viewholder.ATSettableViewHolder;
import com.anthouse.xuhui.R;

import java.util.LinkedList;
import java.util.List;

public class ATLinkageSceneRvAdapter extends RecyclerView.Adapter<ATSettableViewHolder> {
    private static final int TYPE_TITLE_MANUAL_TITLE = 0;
    private static final int TYPE_TITLE_MANUAL = 1;
    private static final int TYPE_TITLE_AUTO_TITLE = 2;
    private static final int TYPE_TITLE_AUTO = 3;
    private static final int TYPE_TITLE_EMPTY = 4;
    private List<Object> data;
    private int indexOfTriggerCondition = 2;

    public ATLinkageSceneRvAdapter() {
        data = new LinkedList<>();
        data.add(new ATSceneManualTitle());
        data.add(new ATSceneDoTitle());
        data.add(new ATSceneAutoTitle());
        data.add(new ATSceneDoTitle());
    }

    public List<Object> getData() {
        return data;
    }

    public int getIndexOfTrigger() {
        return indexOfTriggerCondition;
    }

    public void setLists(List<ATSceneBean> triggerScene, int pager) {
        if(pager == 1){
            indexOfTriggerCondition = 2;
            data.clear();
            data.add(new ATSceneManualTitle());
            data.add(new ATSceneDoTitle());
            data.add(new ATSceneAutoTitle());
            data.add(new ATSceneDoTitle());
        }
        data.addAll(indexOfTriggerCondition, triggerScene);
        indexOfTriggerCondition += triggerScene.size();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = data.get(position);
        if (item instanceof ATSceneManualTitle) {
            return TYPE_TITLE_MANUAL_TITLE;
        } else if (item instanceof ATSceneAutoTitle) {
            return TYPE_TITLE_AUTO_TITLE;
        } else if (item instanceof ATSceneDoTitle) {
            return TYPE_TITLE_EMPTY;
        } else {
            return TYPE_TITLE_MANUAL;
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
        if (TYPE_TITLE_MANUAL_TITLE == viewType) {
            view = inflater.inflate(R.layout.at_item_rv_scene_title, parent, false);
            ((TextView) view.findViewById(R.id.tv_title)).setText("手动");
            return new ATSceneTitleViewHolder(view);
        } else if (TYPE_TITLE_AUTO_TITLE == viewType) {
            view = inflater.inflate(R.layout.at_item_rv_scene_title, parent, false);
            ((TextView) view.findViewById(R.id.tv_title)).setText("自动");
            return new ATSceneTitleViewHolder(view);
        }  else if (TYPE_TITLE_EMPTY == viewType) {
            view = inflater.inflate(R.layout.at_item_rv_scene_title, parent, false);
            return new ATSceneTitleViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.at_item_rv_scene, parent, false);
            return new ATLinkageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(ATSettableViewHolder holder, int position) {
        Object item = data.get(position);
        holder.setData(item, position, data.size());
    }
}