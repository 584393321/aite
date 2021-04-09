package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aliyun.ayland.data.ATFamilyDeviceBean;
import com.aliyun.ayland.data.ATPublicDeviceBean;
import com.aliyun.ayland.data.ATSceneBean1;
import com.aliyun.ayland.data.ATShortcutBean;
import com.aliyun.ayland.data.ATSceneAutoTitle;
import com.aliyun.ayland.data.ATSceneDoTitle;
import com.aliyun.ayland.ui.viewholder.ATSceneTitleAutoViewHolder;
import com.aliyun.ayland.ui.viewholder.ATSettableViewHolder;
import com.aliyun.ayland.ui.viewholder.ATShortcutViewHolder;
import com.aliyun.ayland.ui.viewholder.ATShortcutSeleteViewHolder;
import com.anthouse.xuhui.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ATShortcutSMRVAdapter extends RecyclerView.Adapter<ATSettableViewHolder> {
    private static final int TYPE_TITLE = 0;
    private static final int TYPE_TITLE_DELETE = 1;
    private static final int TYPE_TITLE_SELETE = 2;
    private static final int TYPE_TITLE_ADD = 3;
    private Activity mContext;
    private List<Object> list;
    private List<ATFamilyDeviceBean> mFamilyDeviceList = new ArrayList<>();
    private List<ATPublicDeviceBean> mPublicDeviceList = new ArrayList<>();
    private List<ATSceneBean1> mSceneList = new ArrayList<>();
    private List<ATShortcutBean> mShortcutList = new ArrayList<>();
    private int indexOfDelete;
    private int current_type = 0;
    private ATShortcutBean mATShortcutBean;
    private int status = 0;

    public ATShortcutSMRVAdapter(Activity context, List<ATShortcutBean> list, int status) {
        mContext = context;
        indexOfDelete = 1 + list.size();
        this.mShortcutList.clear();
        this.mShortcutList.addAll(list);
        this.list = new LinkedList<>();
        this.list.add(new ATSceneAutoTitle());
        this.list.addAll(mShortcutList);
        this.list.add(new ATSceneDoTitle());
        this.status = status;
    }

    public void setList (List<ATShortcutBean> list) {
        indexOfDelete = 1 + list.size();
        this.mShortcutList.clear();
        this.mShortcutList.addAll(list);
        this.list = new LinkedList<>();
        this.list.add(new ATSceneAutoTitle());
        this.list.addAll(mShortcutList);
        this.list.add(new ATSceneDoTitle());
        notifyDataSetChanged();
    }

    public List<Object> getList() {
        return list;
    }

    public List<ATShortcutBean> getShortcutList() {
        return mShortcutList;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = list.get(position);
        if (item instanceof ATSceneAutoTitle) {
            return TYPE_TITLE;
        } else if (item instanceof ATSceneDoTitle) {
            return TYPE_TITLE_SELETE;
        } else if (item instanceof ATShortcutBean) {
            return TYPE_TITLE_DELETE;
        } else {
            return TYPE_TITLE_ADD;
        }
    }

    public int getCurrent_type() {
        return current_type;
    }

    public void checkType(int position) {
        current_type = position;
        this.list.clear();
        this.list.add(new ATSceneAutoTitle());
        this.list.addAll(mShortcutList);
        this.list.add(new ATSceneDoTitle());
        if (position == 0) {
            this.list.addAll(mFamilyDeviceList);
        } else if (position == 1) {
            this.list.addAll(mPublicDeviceList);
        } else {
            this.list.addAll(mSceneList);
        }
        notifyDataSetChanged();
    }

    public void setFamilyDeviceLists(List<ATFamilyDeviceBean> list) {
        this.mFamilyDeviceList.clear();
        this.mFamilyDeviceList.addAll(list);
    }

//    public void setFamilyDeviceLists(List<ATFamilyDeviceBean> list,int i) {
//        if(i != 1) {
//            this.mFamilyDeviceList.clear();
//        }
//        this.mFamilyDeviceList.addAll(list);
//    }

    public void setPublicDeviceLists(List<ATPublicDeviceBean> list) {
        this.mPublicDeviceList.clear();
        this.mPublicDeviceList.addAll(list);
    }

    public void setSceneLists(List<ATSceneBean1> list) {
        this.mSceneList.clear();
        this.mSceneList.addAll(list);
    }

    public void addDelete(int position, boolean add) {
        if (current_type == 0) {
            mFamilyDeviceList.get(position - mShortcutList.size() - 2).setAdd(add);
            if (add) {
                ATShortcutBean ATShortcutBean = new ATShortcutBean();
                ATShortcutBean.setItemIcon(mFamilyDeviceList.get(position - mShortcutList.size() - 2).getProductImage());
                ATShortcutBean.setItemName(mFamilyDeviceList.get(position - mShortcutList.size() - 2).getProductName());
                ATShortcutBean.setItemId(mFamilyDeviceList.get(position - mShortcutList.size() - 2).getIotId());
                if("Camera".equals(mFamilyDeviceList.get(position - mShortcutList.size() - 2).getCategoryKey()) || "FaceRecognitionCapabilityModel".equals(mFamilyDeviceList.get(position - mShortcutList.size() - 2).getCategoryKey())){
                    ATShortcutBean.setOperateType(1);
                }else {
                    ATShortcutBean.setOperateType(2);
                }
                ATShortcutBean.setShortcutType(0);
                mShortcutList.add(ATShortcutBean);
                indexOfDelete++;
            } else {
                for (ATShortcutBean ATShortcutBean : mShortcutList) {
                    if (mFamilyDeviceList.get(position - mShortcutList.size() - 2).getIotId().equals(ATShortcutBean.getItemId())) {
                        mShortcutList.remove(ATShortcutBean);
                        break;
                    }
                }
                indexOfDelete--;
            }
        } else if (current_type == 1) {
            mPublicDeviceList.get(position - mShortcutList.size() - 2).setAdd(add);
            if (add) {
                ATShortcutBean ATShortcutBean = new ATShortcutBean();
                ATShortcutBean.setItemIcon(mPublicDeviceList.get(position - mShortcutList.size() - 2).getImageUrl());
                ATShortcutBean.setItemName(mPublicDeviceList.get(position - mShortcutList.size() - 2).getName());
                ATShortcutBean.setItemId(mPublicDeviceList.get(position - mShortcutList.size() - 2).getDeviceId());
                if("Camera".equals(mPublicDeviceList.get(position - mShortcutList.size() - 2).getCategoryKey()) || "FaceRecognitionCapabilityModel".equals(mPublicDeviceList.get(position - mShortcutList.size() - 2).getCategoryKey())){
                    ATShortcutBean.setOperateType(1);
                }else {
                    ATShortcutBean.setOperateType(2);
                }
                ATShortcutBean.setShortcutType(1);
                mShortcutList.add(ATShortcutBean);
                indexOfDelete++;
            } else {
                for (ATShortcutBean ATShortcutBean : mShortcutList) {
                    if (mPublicDeviceList.get(position - mShortcutList.size() - 2).getDeviceId().equals(ATShortcutBean.getItemId())) {
                        mShortcutList.remove(ATShortcutBean);
                        break;
                    }
                }
                indexOfDelete--;
            }
        } else if (current_type == 2) {
            mSceneList.get(position - mShortcutList.size() - 2).setAdd(add);
            if (add) {
                ATShortcutBean ATShortcutBean = new ATShortcutBean();
                ATShortcutBean.setItemIcon(mSceneList.get(position - mShortcutList.size() - 2).getSceneIcon());
                ATShortcutBean.setItemName(mSceneList.get(position - mShortcutList.size() - 2).getSceneName());
                ATShortcutBean.setItemId(mSceneList.get(position - mShortcutList.size() - 2).getSceneId());
                ATShortcutBean.setOperateType(2);
                ATShortcutBean.setShortcutType(2);
                mShortcutList.add(ATShortcutBean);
                indexOfDelete++;
            } else {
                for (ATShortcutBean ATShortcutBean : mShortcutList) {
                    if (mSceneList.get(position - mShortcutList.size() - 2).getSceneId().equals(ATShortcutBean.getItemId())) {
                        mShortcutList.remove(ATShortcutBean);
                        break;
                    }
                }
                indexOfDelete--;
            }
        }
        checkType(current_type);
    }

    public void removePosition(int position) {
        switch (((ATShortcutBean) list.get(position)).getShortcutType()) {
            case 0:
                for (int i = 0; i < mFamilyDeviceList.size(); i++) {
                    if (((ATShortcutBean) list.get(position)).getItemId().equals(mFamilyDeviceList.get(i).getIotId())) {
                        mFamilyDeviceList.get(i).setAdd(false);
                        if (current_type == 0) {
                            list.clear();
                            list.add(new ATSceneAutoTitle());
                            list.addAll(mShortcutList);
                            list.add(new ATSceneDoTitle());
                            list.addAll(mFamilyDeviceList);
                            notifyDataSetChanged();
                        }
                        break;
                    }
                }
                break;
            case 1:
                for (int i = 0; i < mPublicDeviceList.size(); i++) {
                    if (((ATShortcutBean) list.get(position)).getItemId().equals(mPublicDeviceList.get(i).getDeviceId())) {
                        mPublicDeviceList.get(i).setAdd(false);
                        if (current_type == 1) {
                            list.clear();
                            list.add(new ATSceneAutoTitle());
                            list.addAll(mShortcutList);
                            list.add(new ATSceneDoTitle());
                            list.addAll(mPublicDeviceList);
                            notifyDataSetChanged();
                        }
                        break;
                    }
                }
                for (int i = 0; i < mFamilyDeviceList.size(); i++) {
                    if (((ATShortcutBean) list.get(position)).getItemId().equals(mFamilyDeviceList.get(i).getIotId())) {
                        mFamilyDeviceList.get(i).setAdd(false);
                        if (current_type == 0) {
                            list.clear();
                            list.add(new ATSceneAutoTitle());
                            list.addAll(mShortcutList);
                            list.add(new ATSceneDoTitle());
                            list.addAll(mFamilyDeviceList);
                            notifyDataSetChanged();
                        }
                        break;
                    }
                }
                break;
            case 2:
                for (int i = 0; i < mSceneList.size(); i++) {
                    if (((ATShortcutBean) list.get(position)).getItemId().equals(mSceneList.get(i).getSceneId())) {
                        mSceneList.get(i).setAdd(false);
                        if (current_type == 2) {
                            list.clear();
                            list.add(new ATSceneAutoTitle());
                            list.addAll(mShortcutList);
                            list.add(new ATSceneDoTitle());
                            list.addAll(mSceneList);
                            notifyDataSetChanged();
                        }
                        break;
                    }
                }
                break;
        }
        if (position < indexOfDelete) {
            mShortcutList.remove(position - 1);
            indexOfDelete--;
        }
        list.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public ATSettableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view;
        if (TYPE_TITLE == viewType) {
            view = inflater.inflate(R.layout.at_item_rv_shortcut_title, parent, false);
            return new ATSceneTitleAutoViewHolder(view);
        } else if (TYPE_TITLE_SELETE == viewType) {
            view = inflater.inflate(R.layout.at_item_rv_shortcut_select, parent, false);
            return new ATShortcutSeleteViewHolder(view, mContext);
        } else {
            view = inflater.inflate(R.layout.at_item_rv_shortcut, parent, false);
            return new ATShortcutViewHolder(view, status, mOnItemClickListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ATSettableViewHolder holder, int position) {
        Object item = list.get(position);
        holder.setData(item, position, list.size());
    }

    public void notifyItemMoveded(int fromPosition, int toPosition) {
        mATShortcutBean = mShortcutList.get(fromPosition-1);
        mShortcutList.remove(fromPosition-1);
        mShortcutList.add(toPosition-1, mATShortcutBean);
        notifyItemMoved(fromPosition, toPosition);
    }

    private ATShortcutViewHolder.OnRecyclerViewItemClickListener mOnItemClickListener = null;
    public void setOnItemClickListener(ATShortcutViewHolder.OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
