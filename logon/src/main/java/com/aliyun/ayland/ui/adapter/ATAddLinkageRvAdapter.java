package com.aliyun.ayland.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.data.ATSceneAdd;
import com.aliyun.ayland.data.ATSceneAutoTitle;
import com.aliyun.ayland.data.ATSceneDelete;
import com.aliyun.ayland.data.ATSceneDoTitle;
import com.aliyun.ayland.data.ATSceneManualTitle;
import com.aliyun.ayland.data.ATSceneName;
import com.aliyun.ayland.ui.viewholder.ATSceneTitleAutoViewHolder;
import com.aliyun.ayland.ui.viewholder.ATSceneTitleIfViewHolder;
import com.aliyun.ayland.ui.viewholder.ATSceneViewHolder;
import com.aliyun.ayland.ui.viewholder.ATSettableViewHolder;
import com.aliyun.ayland.utils.ATToastUtils;
import com.anthouse.xuhui.R;

import java.util.LinkedList;
import java.util.List;

public class ATAddLinkageRvAdapter extends RecyclerView.Adapter<ATSettableViewHolder> {
    private static final int TYPE_TITLE_IF = 0;
    private static final int TYPE_TITLE_AND = 1;
    private static final int TYPE_TITLE_DO = 2;
    private static final int TYPE_SCENE = 3;
    private static final int TYPE_END = 4;
    private static final int TYPE_DELETE = 5;
    private List<Object> data;
    private int indexOfTriggerCondition = 1;
    private int indexOfActionCondition = 2;
    private int indexOfConditionCondition = 3;
    private boolean edit;
    private ATSceneManualTitle mATSceneManualTitle;

    public ATAddLinkageRvAdapter(String sceneId) {
        edit = !TextUtils.isEmpty(sceneId);
        data = new LinkedList<>();
        mATSceneManualTitle = new ATSceneManualTitle();
        mATSceneManualTitle.setAuto(false);
        data.add(mATSceneManualTitle);
        data.add(new ATSceneDoTitle());
        data.add(new ATSceneAutoTitle());
        data.add(new ATSceneAdd());
        data.add(new ATSceneDelete());
    }

    public List<Object> getData() {
        return data;
    }

    public int getIndexOfTrigger() {
        return indexOfTriggerCondition;
    }

    public int getIndexOfCondition() {
        return indexOfConditionCondition;
    }

    public int getIndexOfAction() {
        return indexOfActionCondition;
    }

    public void addTriggerCondition(ATSceneName ATSceneName) {
        if (ATSceneName.getUri().contains("device"))
            for (int i = 1; i < indexOfTriggerCondition; i++) {
                if (data.get(i) instanceof ATSceneName) {
                    if (((ATSceneName) data.get(i)).getName().equals(ATSceneName.getName()) && ((ATSceneName) data.get(i)).getUri().equals(ATSceneName.getUri())
                            && ((ATSceneName) data.get(i)).getContent().replaceAll(" ", "").equals(ATSceneName.getContent().replaceAll(" ", ""))
                            && (!ATSceneName.getParams().contains("iotId") || JSONObject.parseObject(ATSceneName.getParams()).getString("iotId").equals(JSONObject.parseObject(((ATSceneName) data.get(i)).getParams()).getString("iotId"))
                    )) {
                        ATToastUtils.shortShow("已有相同条件");
                        return;
                    }
                }
            }
        data.add(indexOfTriggerCondition, ATSceneName);
        notifyItemInserted(indexOfTriggerCondition);
        setAuto(true);
        indexOfTriggerCondition++;
        indexOfActionCondition++;
        indexOfConditionCondition++;
    }

    public void setAuto(boolean auto) {
        ((ATSceneManualTitle) data.get(0)).setAuto(auto);
        notifyItemChanged(0);
    }

    public void replaceCondition(int currentPositon, ATSceneName ATSceneName) {
        data.remove(currentPositon);
        data.add(currentPositon, ATSceneName);
        notifyItemChanged(currentPositon);
    }

    public void addConditions(List<ATSceneName> triggerScene, List<ATSceneName> conditionScene, List<ATSceneName> actionScene) {
        data.addAll(indexOfTriggerCondition, triggerScene);
        indexOfTriggerCondition += triggerScene.size();
        indexOfActionCondition += triggerScene.size();
        indexOfConditionCondition += triggerScene.size();
        data.addAll(indexOfActionCondition, actionScene);
        indexOfActionCondition += actionScene.size();
        indexOfConditionCondition += actionScene.size();
        data.addAll(indexOfConditionCondition, conditionScene);
        indexOfConditionCondition += conditionScene.size();
        ((ATSceneManualTitle) data.get(0)).setAuto(triggerScene.size() > 0);
        notifyDataSetChanged();
    }

    public void addConditionCondition(ATSceneName ATSceneName) {
        if (ATSceneName.getUri().contains("device"))
            for (int i = indexOfActionCondition + 1; i < indexOfConditionCondition; i++) {
                if (data.get(i) instanceof ATSceneName) {
                    if (((ATSceneName) data.get(i)).getName().equals(ATSceneName.getName()) && ((ATSceneName) data.get(i)).getUri().equals(ATSceneName.getUri())
                            && ((ATSceneName) data.get(i)).getContent().replaceAll(" ", "").equals(ATSceneName.getContent().replaceAll(" ", ""))
                            && (!ATSceneName.getParams().contains("iotId") || JSONObject.parseObject(ATSceneName.getParams()).getString("iotId").equals(JSONObject.parseObject(((ATSceneName) data.get(i)).getParams()).getString("iotId"))
                    )) {
                        ATToastUtils.shortShow("已有相同条件");
                        return;
                    }
                }
            }
        data.add(indexOfConditionCondition, ATSceneName);
        notifyItemInserted(indexOfConditionCondition);
        indexOfConditionCondition++;
    }

    public void addActionCondition(ATSceneName ATSceneName) {
        if (ATSceneName.getUri().contains("device"))
            for (int i = indexOfTriggerCondition + 1; i < indexOfActionCondition; i++) {
                if (data.get(i) instanceof ATSceneName) {
                    if (((ATSceneName) data.get(i)).getName().equals(ATSceneName.getName()) && ((ATSceneName) data.get(i)).getUri().equals(ATSceneName.getUri())
                            && ((ATSceneName) data.get(i)).getContent().replaceAll(" ", "").equals(ATSceneName.getContent().replaceAll(" ", ""))
                            && (!ATSceneName.getParams().contains("iotId") || JSONObject.parseObject(ATSceneName.getParams()).getString("iotId").equals(JSONObject.parseObject(((ATSceneName) data.get(i)).getParams()).getString("iotId"))
                    )) {
                        ATToastUtils.shortShow("已有相同条件");
                        return;
                    }
                }
            }
        data.add(indexOfActionCondition, ATSceneName);
        notifyItemInserted(indexOfActionCondition);
        indexOfConditionCondition++;
        indexOfActionCondition++;
    }

    public void removeCondition(int position) {
        if (position < indexOfTriggerCondition) {
            indexOfTriggerCondition--;
            indexOfActionCondition--;
            indexOfConditionCondition--;
        } else if (position < indexOfActionCondition) {
            indexOfActionCondition--;
            indexOfConditionCondition--;
        } else if (position < indexOfConditionCondition) {
            indexOfConditionCondition--;
        }
        data.remove(position);
        if (indexOfTriggerCondition == 1) {
            setAuto(false);
        }
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = data.get(position);
        if (item instanceof ATSceneManualTitle) {
            return TYPE_TITLE_IF;
        } else if (item instanceof ATSceneAutoTitle) {
            return TYPE_TITLE_AND;
        } else if (item instanceof ATSceneDoTitle) {
            return TYPE_TITLE_DO;
        } else if (item instanceof ATSceneAdd) {
            return TYPE_END;
        } else if (item instanceof ATSceneDelete) {
            return TYPE_DELETE;
        } else {
            return TYPE_SCENE;
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
        if (TYPE_TITLE_IF == viewType) {
            view = inflater.inflate(R.layout.at_item_rv_add_linkage_title_if, parent, false);
            return new ATSceneTitleIfViewHolder(view);
        } else if (TYPE_TITLE_AND == viewType) {
            view = inflater.inflate(R.layout.at_item_rv_add_linkage_title, parent, false);
            ((TextView) view.findViewById(R.id.tv_title)).setText(context.getResources().getString(R.string.at_and_condition));
            ((TextView) view.findViewById(R.id.tv_drag)).setText(context.getResources().getString(R.string.at_long_click_to_drag));
            ((TextView) view.findViewById(R.id.tv_hinit)).setText(context.getResources().getString(R.string.at_when_you));
            return new ATSceneTitleAutoViewHolder(view);
        } else if (TYPE_TITLE_DO == viewType) {
            view = inflater.inflate(R.layout.at_item_rv_add_linkage_title, parent, false);
            ((TextView) view.findViewById(R.id.tv_title)).setText(context.getResources().getString(R.string.at_action_condition));
            ((TextView) view.findViewById(R.id.tv_drag)).setText(context.getResources().getString(R.string.at_long_click_to_drag_));
            ((TextView) view.findViewById(R.id.tv_hinit)).setText(null);
            return new ATSceneTitleAutoViewHolder(view);
        } else if (TYPE_DELETE == viewType) {
            view = inflater.inflate(R.layout.at_item_rv_add_delete, parent, false);
            if (edit) {
                view.findViewById(R.id.tv_delete).setVisibility(View.VISIBLE);
            } else {
                view.findViewById(R.id.tv_delete).setVisibility(View.GONE);
            }
            return new ATSceneTitleAutoViewHolder(view);
        } else if (TYPE_END == viewType) {
            view = inflater.inflate(R.layout.at_item_rv_add_linkage_end, parent, false);
            return new ATSceneTitleAutoViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.at_item_rv_condition, parent, false);
            return new ATSceneViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(ATSettableViewHolder holder, int position) {
        Object item = data.get(position);
        holder.setData(item, position, data.size());
    }
}