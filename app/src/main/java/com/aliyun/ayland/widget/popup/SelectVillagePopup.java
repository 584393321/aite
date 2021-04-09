package com.aliyun.ayland.widget.popup;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.AutoUtils;
import com.aliyun.ayland.data.AllVillageDetailBean1;
import com.aliyun.ayland.data.AllVillageDetailBean1.AreaBean;
import com.aliyun.ayland.data.AllVillageDetailBean1.AreaBean.ImmeubleBean;
import com.aliyun.ayland.data.AllVillageDetailBean1.AreaBean.ImmeubleBean.UnitBean;
import com.aliyun.ayland.data.AllVillageDetailBean1.AreaBean.ImmeubleBean.UnitBean.FloorBean;
import com.aliyun.ayland.data.AllVillageDetailBean1.AreaBean.ImmeubleBean.UnitBean.FloorBean.*;

import com.aliyun.ayland.data.SelectBean;
import com.aliyun.ayland.widget.popup.base.BasePopupWindow;
import com.anthouse.lgcs.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SelectVillagePopup<T> extends BasePopupWindow {
    private Activity context;
    private View popupView;
    private InnerPopupAdapter mAdapter;
    private int current = 0;
    private String name = "", code = "";
    private HashMap<Integer, Integer> all_position = new HashMap<>();
    private List<T> list;
    private TextView textView;

    public SelectVillagePopup(Activity context, List<AllVillageDetailBean1> allList) {
        super(context);
        setAdjustInputMethod(true);
        setBackPressEnable(false);
        setPopupWindowFullScreen(true);
        setDismissWhenTouchOuside(true);
        this.list = (List<T>) allList;
        this.context = context;
        all_position.put(0, 0);
        all_position.put(1, 0);
        all_position.put(2, 0);
        all_position.put(3, 0);
        all_position.put(4, 0);
        all_position.put(5, 0);
        textView = (TextView) findViewById(R.id.textView);
        findViewById(R.id.imageView).setOnClickListener(v -> {
            if (current == 0) {
                dismiss();
            } else {
                all_position.put(current, 0);
                current--;
                switch (current) {
                    case 0:
                        textView.setText(context.getString(R.string.please_choose_village1));
                        this.list = (List<T>) allList;
                        break;
                    case 1:
                        textView.setText(context.getString(R.string.please_choose_village2));
                        this.list = (List<T>) allList.get(all_position.get(0)).getArea();
                        break;
                    case 2:
                        textView.setText(context.getString(R.string.please_choose_village3));
                        this.list = (List<T>) allList.get(all_position.get(0)).getArea().get(all_position.get(1))
                                .getImmeuble();
                        break;
                    case 3:
                        textView.setText(context.getString(R.string.please_choose_village4));
                        this.list = (List<T>) allList.get(all_position.get(0)).getArea().get(all_position.get(1))
                                .getImmeuble().get(all_position.get(2)).getUnit();
                        break;
                    case 4:
                        textView.setText(context.getString(R.string.please_choose_village5));
                        this.list = (List<T>) allList.get(all_position.get(0)).getArea().get(all_position.get(1))
                                .getImmeuble().get(all_position.get(2)).getUnit().get(all_position.get(3))
                                .getFloor();
                        break;
                    case 5:
                        textView.setText(context.getString(R.string.please_choose_village6));
                        this.list = (List<T>) allList.get(all_position.get(0)).getArea().get(all_position.get(1))
                                .getImmeuble().get(all_position.get(2)).getUnit().get(all_position.get(3))
                                .getFloor().get(all_position.get(4)).getRoom();
                        break;
                }
                mAdapter.setItemList(list, all_position.get(current));
            }
        });
        ListView listView = (ListView) findViewById(R.id.listView);
        mAdapter = new InnerPopupAdapter(context);
        mAdapter.setItemList(list);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            all_position.put(current, position);
            current++;
            switch (current) {
                case 0:
                    textView.setText(context.getString(R.string.please_choose_village1));
                    this.list = (List<T>) allList;
                    break;
                case 1:
                    textView.setText(context.getString(R.string.please_choose_village2));
                    this.list = (List<T>) allList.get(all_position.get(0)).getArea();
                    code = allList.get(all_position.get(0)).getCode();
                    name = allList.get(all_position.get(0)).getName();
                    break;
                case 2:
                    textView.setText(context.getString(R.string.please_choose_village3));
                    this.list = (List<T>) allList.get(all_position.get(0)).getArea().get(all_position.get(1))
                            .getImmeuble();
                    code = allList.get(all_position.get(0)).getArea().get(all_position.get(1)).getCode();
                    name = allList.get(all_position.get(0)).getName() + allList.get(all_position.get(0)).getArea().get(all_position.get(1)).getName();
                    break;
                case 3:
                    textView.setText(context.getString(R.string.please_choose_village4));
                    this.list = (List<T>) allList.get(all_position.get(0)).getArea().get(all_position.get(1))
                            .getImmeuble().get(all_position.get(2)).getUnit();
                    code = allList.get(all_position.get(0)).getArea().get(all_position.get(1)).getImmeuble().get(all_position.get(2)).getCode();
                    name = allList.get(all_position.get(0)).getName() + allList.get(all_position.get(0)).getArea().get(all_position.get(1)).getName()
                            + allList.get(all_position.get(0)).getArea().get(all_position.get(1)).getImmeuble().get(all_position.get(2)).getName();
                    break;
                case 4:
                    textView.setText(context.getString(R.string.please_choose_village5));
                    this.list = (List<T>) allList.get(all_position.get(0)).getArea().get(all_position.get(1))
                            .getImmeuble().get(all_position.get(2)).getUnit().get(all_position.get(3))
                            .getFloor();
                    code = allList.get(all_position.get(0)).getArea().get(all_position.get(1)).getImmeuble().get(all_position.get(2))
                            .getUnit().get(all_position.get(3)).getCode();
                    name = allList.get(all_position.get(0)).getName() + allList.get(all_position.get(0)).getArea().get(all_position.get(1)).getName()
                            + allList.get(all_position.get(0)).getArea().get(all_position.get(1)).getImmeuble().get(all_position.get(2)).getName()
                            + allList.get(all_position.get(0)).getArea().get(all_position.get(1)).getImmeuble().get(all_position.get(2)).getUnit().get(all_position.get(3)).getName();
                    break;
                case 5:
                    textView.setText(context.getString(R.string.please_choose_village6));
                    this.list = (List<T>) allList.get(all_position.get(0)).getArea().get(all_position.get(1))
                            .getImmeuble().get(all_position.get(2)).getUnit().get(all_position.get(3))
                            .getFloor().get(all_position.get(4)).getRoom();
                    code = allList.get(all_position.get(0)).getArea().get(all_position.get(1)).getImmeuble().get(all_position.get(2))
                            .getUnit().get(all_position.get(3)).getFloor().get(all_position.get(4)).getCode();
                    name = allList.get(all_position.get(0)).getName() + allList.get(all_position.get(0)).getArea().get(all_position.get(1)).getName()
                            + allList.get(all_position.get(0)).getArea().get(all_position.get(1)).getImmeuble().get(all_position.get(2)).getName()
                            + allList.get(all_position.get(0)).getArea().get(all_position.get(1)).getImmeuble().get(all_position.get(2)).getUnit().get(all_position.get(3)).getName()
                            + allList.get(all_position.get(0)).getArea().get(all_position.get(1)).getImmeuble().get(all_position.get(2)).getUnit().get(all_position.get(3)).getFloor().get(all_position.get(4)).getName();
                    break;
                case 6:
                    textView.setText(context.getString(R.string.please_choose_village6));
                    this.list = new ArrayList<>();
                    code = allList.get(all_position.get(0)).getArea().get(all_position.get(1))
                            .getImmeuble().get(all_position.get(2)).getUnit().get(all_position.get(3))
                            .getFloor().get(all_position.get(4)).getRoom().get(all_position.get(5)).getCode();
                    name = allList.get(all_position.get(0)).getName() + allList.get(all_position.get(0)).getArea().get(all_position.get(1)).getName()
                            + allList.get(all_position.get(0)).getArea().get(all_position.get(1)).getImmeuble().get(all_position.get(2)).getName()
                            + allList.get(all_position.get(0)).getArea().get(all_position.get(1)).getImmeuble().get(all_position.get(2)).getUnit().get(all_position.get(3)).getName()
                            + allList.get(all_position.get(0)).getArea().get(all_position.get(1)).getImmeuble().get(all_position.get(2)).getUnit().get(all_position.get(3)).getFloor().get(all_position.get(4)).getName()
                            + allList.get(all_position.get(0)).getArea().get(all_position.get(1)).getImmeuble().get(all_position.get(2)).getUnit().get(all_position.get(3)).getFloor().get(all_position.get(4)).getRoom().get(all_position.get(5)).getName();
                    break;
            }
            if (list.size() == 0) {
                dismiss();
                mOnClickListener.onItemClick(name, code);
            }
            mAdapter.setItemList(list);
        });
    }

    @Override
    protected Animation initShowAnimation() {
        return getTranslateAnimation(250 * 2, 0, 300);
    }

    @Override
    protected Animation initExitAnimation() {
        return getTranslateAnimation(0, 250 * 2, 300);
    }

    @Override
    public View getClickToDismissView() {
        return popupView.findViewById(R.id.click_to_dismiss);
    }

    @Override
    public View onCreatePopupView() {
        popupView = LayoutInflater.from(getContext()).inflate(R.layout.popup_select_village, null);
        return popupView;
    }

    @Override
    public View initAnimaView() {
        return popupView.findViewById(R.id.popup_container);
    }

    private static class InnerPopupAdapter<T> extends BaseAdapter {
        private LayoutInflater mInflater;
        private Context mContext;
        private List<T> list = new ArrayList<>();
        private int mPosition = -1;

        private InnerPopupAdapter(Context context) {
            mContext = context;
            mInflater = LayoutInflater.from(context);
        }

        private void setItemList(List<T> itemList, int position) {
            list = itemList;
            mPosition = position;
            notifyDataSetChanged();
        }

        private void setItemList(List<T> itemList) {
            list = itemList;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public T getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            InnerPopupAdapter.ViewHolder vh;
            if (convertView == null) {
                vh = new InnerPopupAdapter.ViewHolder();
                convertView = mInflater.inflate(R.layout.item_lv_select_building, parent, false);
                vh.mTvName = convertView.findViewById(R.id.tv_name);
                vh.mImgCheck = convertView.findViewById(R.id.img_check);
                convertView.setTag(vh);
            } else {
                vh = (InnerPopupAdapter.ViewHolder) convertView.getTag();
            }
            if (list.get(position) instanceof AllVillageDetailBean1) {
                vh.mTvName.setText(((AllVillageDetailBean1) list.get(position)).getName());
            } else if (list.get(position) instanceof AreaBean) {
                vh.mTvName.setText(((AreaBean) list.get(position)).getName());
            } else if (list.get(position) instanceof ImmeubleBean) {
                vh.mTvName.setText(((ImmeubleBean) list.get(position)).getName());
            } else if (list.get(position) instanceof UnitBean) {
                vh.mTvName.setText(((UnitBean) list.get(position)).getName());
            } else if (list.get(position) instanceof FloorBean) {
                vh.mTvName.setText(((FloorBean) list.get(position)).getName());
            } else if (list.get(position) instanceof RoomBean) {
                vh.mTvName.setText(((RoomBean) list.get(position)).getName());
            }
            if (mPosition == position) {
                vh.mTvName.setTextColor(mContext.getResources().getColor(R.color._5F7EE1));
                vh.mImgCheck.setVisibility(View.VISIBLE);
            } else {
                vh.mTvName.setTextColor(mContext.getResources().getColor(R.color._515669));
                vh.mImgCheck.setVisibility(View.GONE);
            }
            AutoUtils.autoSize(convertView);
            return convertView;
        }

        public void setPosition(int position) {
            mPosition = position;
            notifyDataSetChanged();
        }

        class ViewHolder {
            private TextView mTvName;
            private ImageView mImgCheck;
        }
    }

    private OnClickListener mOnClickListener = null;

    public interface OnClickListener {
        void onItemClick(String name, String position);
    }

    public void setOnItemClickListener(OnClickListener listener) {
        this.mOnClickListener = listener;
    }
}