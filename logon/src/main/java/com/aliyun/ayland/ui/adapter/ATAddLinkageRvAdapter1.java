package com.aliyun.ayland.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.data.ATSceneAdd;
import com.aliyun.ayland.data.ATSceneAutoTitle;
import com.aliyun.ayland.data.ATSceneDelete;
import com.aliyun.ayland.data.ATSceneDoTitle;
import com.aliyun.ayland.data.ATSceneManualTitle;
import com.aliyun.ayland.data.ATSceneName;
import com.aliyun.ayland.interfaces.ATOnRecyclerViewItemClickListener;
import com.aliyun.ayland.ui.activity.ATLinkageAddActivity;
import com.aliyun.ayland.ui.activity.ATLinkageAddActivity1;
import com.aliyun.ayland.ui.viewholder.ATSceneDoViewHolder;
import com.aliyun.ayland.ui.viewholder.ATSceneTitleAutoViewHolder;
import com.aliyun.ayland.ui.viewholder.ATSceneViewHolder;
import com.aliyun.ayland.ui.viewholder.ATSettableViewHolder;
import com.aliyun.ayland.utils.ATToastUtils;
import com.aliyun.ayland.widget.ATNoUnderLineSpan;
import com.anthouse.xuhui.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.LinkedList;
import java.util.List;

public class ATAddLinkageRvAdapter1 extends RecyclerView.Adapter<ATSettableViewHolder> {
    private static final int TYPE_TITLE_IF = 0;
    private static final int TYPE_TITLE_AND = 1;
    private static final int TYPE_TITLE_DO = 2;
    private static final int TYPE_SCENE = 3;
    private static final int TYPE_ADD = 4;
    private static final int TYPE_DELETE = 5;
    private List<Object> data;
    private int indexOfTriggerCondition = 1;
    private int indexOfConditionCondition = 3;
    private int indexOfActionCondition = 5;
    private boolean edit;
    private TextView mTvName;
    private ImageView imgScene;
    private ATLinkageAddActivity1 mContext;
    private String sceneIcon;
    private RequestOptions options = new RequestOptions()
            .placeholder(R.drawable.atico_chj_liandong)
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    public ATAddLinkageRvAdapter1(ATLinkageAddActivity1 linkageAddActivity, String sceneId) {
        mContext = linkageAddActivity;
        edit = !TextUtils.isEmpty(sceneId);
        data = new LinkedList<>();
        data.add(new ATSceneManualTitle());
        data.add(new ATSceneAdd());
        data.add(new ATSceneAutoTitle());
        data.add(new ATSceneAdd());
        data.add(new ATSceneDoTitle());
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

    public void addTriggerCondition(ATSceneName sceneName) {
        if (sceneName.getUri().contains("device"))
            for (int i = 1; i < indexOfTriggerCondition; i++) {
                if (data.get(i) instanceof ATSceneName) {
                    if (((ATSceneName) data.get(i)).getName().equals(sceneName.getName()) && ((ATSceneName) data.get(i)).getUri().equals(sceneName.getUri())
                            && ((ATSceneName) data.get(i)).getContent().replaceAll(" ", "").equals(sceneName.getContent().replaceAll(" ", ""))
                            && (!sceneName.getParams().contains("iotId") || JSONObject.parseObject(sceneName.getParams()).getString("iotId").equals(JSONObject.parseObject(((ATSceneName) data.get(i)).getParams()).getString("iotId"))
                    )) {
                        ATToastUtils.shortShow("已有相同条件");
                        return;
                    }
                }
            }
        data.add(indexOfTriggerCondition, sceneName);
        notifyItemInserted(indexOfTriggerCondition);
        indexOfTriggerCondition++;
        indexOfConditionCondition++;
        indexOfActionCondition++;
    }

    public void replaceCondition(int currentPositon, ATSceneName sceneName) {
        data.remove(currentPositon);
        data.add(currentPositon, sceneName);
        notifyItemChanged(currentPositon);
    }

    public void addConditions(List<ATSceneName> triggerScene, List<ATSceneName> conditionScene, List<ATSceneName> actionScene) {
        data.addAll(indexOfTriggerCondition, triggerScene);
        indexOfTriggerCondition += triggerScene.size();
        indexOfConditionCondition += triggerScene.size();
        indexOfActionCondition += triggerScene.size();
        data.addAll(indexOfConditionCondition, conditionScene);
        indexOfConditionCondition += conditionScene.size();
        indexOfActionCondition += conditionScene.size();
        data.addAll(indexOfActionCondition, actionScene);
        indexOfActionCondition += actionScene.size();
        ((ATSceneManualTitle) data.get(0)).setAuto(triggerScene.size() > 0);
        notifyDataSetChanged();
    }

    public void addConditionCondition(ATSceneName sceneName) {
        if (sceneName.getUri().contains("device"))
            for (int i = indexOfTriggerCondition + 1; i < indexOfConditionCondition; i++) {
                if (data.get(i) instanceof ATSceneName) {
                    if (((ATSceneName) data.get(i)).getName().equals(sceneName.getName()) && ((ATSceneName) data.get(i)).getUri().equals(sceneName.getUri())
                            && ((ATSceneName) data.get(i)).getContent().replaceAll(" ", "").equals(sceneName.getContent().replaceAll(" ", ""))
                            && (!sceneName.getParams().contains("iotId") || JSONObject.parseObject(sceneName.getParams()).getString("iotId").equals(JSONObject.parseObject(((ATSceneName) data.get(i)).getParams()).getString("iotId"))
                    )) {
                        ATToastUtils.shortShow("已有相同条件");
                        return;
                    }
                }
            }
        data.add(indexOfConditionCondition, sceneName);
        notifyItemInserted(indexOfConditionCondition);
        indexOfConditionCondition++;
        indexOfActionCondition++;
    }

    public void addActionCondition(ATSceneName sceneName) {
        if (sceneName.getUri().contains("device"))
            for (int i = indexOfConditionCondition + 1; i < indexOfActionCondition; i++) {
                if (data.get(i) instanceof ATSceneName) {
                    if (((ATSceneName) data.get(i)).getName().equals(sceneName.getName()) && ((ATSceneName) data.get(i)).getUri().equals(sceneName.getUri())
                            && ((ATSceneName) data.get(i)).getContent().replaceAll(" ", "").equals(sceneName.getContent().replaceAll(" ", ""))
                            && (!sceneName.getParams().contains("iotId") || JSONObject.parseObject(sceneName.getParams()).getString("iotId").equals(JSONObject.parseObject(((ATSceneName) data.get(i)).getParams()).getString("iotId"))
                    )) {
                        ATToastUtils.shortShow("已有相同条件");
                        return;
                    }
                }
            }
        data.add(indexOfActionCondition, sceneName);
        notifyItemInserted(indexOfActionCondition);
        indexOfActionCondition++;
    }

    public void removeCondition(int position) {
        if (position < indexOfTriggerCondition) {
            indexOfTriggerCondition--;
            indexOfConditionCondition--;
            indexOfActionCondition--;
        } else if (position < indexOfConditionCondition) {
            indexOfConditionCondition--;
            indexOfActionCondition--;
        } else if (position < indexOfActionCondition) {
            indexOfActionCondition--;
        }
        data.remove(position);
        notifyItemRemoved(position);
    }

    public String getName() {
        return mTvName.getText().toString();
    }

    public void setName(String name) {
        mTvName.setText(name);
    }

    public void setSceneIcon(String sceneIcon) {
        this.sceneIcon = sceneIcon;
        if (imgScene != null)
            Glide.with(mContext).load(sceneIcon).apply(options).into(imgScene);
    }

    private SpannableString getSpannableString(String string) {
        SpannableString s = new SpannableString(string);
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#FF999999")), 0, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFDA448")), 7, 11, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return s;
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
            return TYPE_ADD;
        } else if (item instanceof ATSceneDelete) {
            return TYPE_DELETE;
        } else {
            return TYPE_SCENE;
        }
    }

    @NonNull
    @Override
    public ATSettableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        if (TYPE_TITLE_IF == viewType) {
            view = inflater.inflate(R.layout.at_item_rv_add_linkage_title_if1, parent, false);
            mTvName = view.findViewById(R.id.tv_name);
            imgScene = view.findViewById(R.id.img);
            Glide.with(mContext).load(sceneIcon).apply(options).into(imgScene);
            view.findViewById(R.id.img).setOnClickListener(v ->
                    mOnItemClickListener.onItemClick(v, null, 1));
            mTvName.setOnClickListener(v ->
                    mOnItemClickListener.onItemClick(v, null, 2));
            return new ATSceneTitleAutoViewHolder(view);
        } else if (TYPE_TITLE_AND == viewType) {
            view = inflater.inflate(R.layout.at_item_rv_add_linkage_title_and_do, parent, false);
            ((TextView) view.findViewById(R.id.tv_title)).setText(context.getResources().getString(R.string.at_and));
            ((TextView) view.findViewById(R.id.tv_condition)).setText(context.getResources().getString(R.string.at_all_of_the_following_restrictions_are_met));
            return new ATSceneTitleAutoViewHolder(view);
        } else if (TYPE_TITLE_DO == viewType) {
            view = inflater.inflate(R.layout.at_item_rv_add_linkage_title_and_do, parent, false);
            ((TextView) view.findViewById(R.id.tv_title)).setText(context.getResources().getString(R.string.at_jiu));
            ((TextView) view.findViewById(R.id.tv_condition)).setText(getSpannableString(context.getResources().getString(R.string.at_perform_the_following_conditions)));
            return new ATSceneTitleAutoViewHolder(view);
        } else if (TYPE_DELETE == viewType) {
            view = inflater.inflate(R.layout.at_item_rv_add_delete, parent, false);
            if (edit) {
                view.findViewById(R.id.tv_delete).setVisibility(View.VISIBLE);
            } else {
                view.findViewById(R.id.tv_delete).setVisibility(View.GONE);
            }
            return new ATSceneTitleAutoViewHolder(view);
        } else if (TYPE_ADD == viewType) {
            view = inflater.inflate(R.layout.at_item_rv_add_condition, parent, false);
            return new ATSceneDoViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.at_item_rv_condition, parent, false);
            return new ATSceneViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ATSettableViewHolder holder, int position) {
        Object item = data.get(position);
        holder.setData(item, position, data.size());
    }


    private ATOnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(ATOnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}