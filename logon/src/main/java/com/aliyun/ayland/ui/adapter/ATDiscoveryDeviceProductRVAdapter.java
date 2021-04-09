package com.aliyun.ayland.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aliyun.ayland.base.autolayout.util.ATAutoUtils;
import com.aliyun.ayland.data.ATDiscoveryDeviceSecondLevelBean;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.utils.ATAddDeviceScanHelper;
import com.aliyun.ayland.utils.ATSortUtils;
import com.aliyun.iot.aep.component.router.Router;
import com.anthouse.xuhui.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.espressif.android.v1.EspTouchActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ATDiscoveryDeviceProductRVAdapter extends RecyclerView.Adapter implements Filterable, Comparator<ATDiscoveryDeviceSecondLevelBean> {
    private Activity mContext;
    private List<ATDiscoveryDeviceSecondLevelBean> list = new ArrayList<>();
    private List<ATDiscoveryDeviceSecondLevelBean> mFilterList = new ArrayList<>();
    //拼音排列指针
    private List<ATDiscoveryDeviceSecondLevelBean> mSortList = new ArrayList<>();
    private RequestOptions options = new RequestOptions()
            .placeholder(R.drawable.at_home_ico_shebeigongyong)
            .error(R.drawable.at_home_ico_shebeigongyong);

    public ATDiscoveryDeviceProductRVAdapter(Activity context) {
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.at_item_rv_discovery_device_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setData(position);
    }

    @Override
    public int getItemCount() {
//        return list.size();
        return mFilterList.size();
    }

    public void setLists(List<ATDiscoveryDeviceSecondLevelBean> list, List<ATDiscoveryDeviceSecondLevelBean> list1, int pageNo) {
        if (pageNo == 1) {
            this.mSortList.clear();
            this.list.clear();
            this.mFilterList.clear();
        }
//        this.list.clear();
        this.mSortList.addAll(list1);
        this.mFilterList.addAll(list);
        this.list.addAll(list);
        initSort();
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            //执行过滤操作
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    //没有过滤的内容，则使用源数据
                    mFilterList = list;
                } else {
                    List<ATDiscoveryDeviceSecondLevelBean> filteredList = new ArrayList<>();
                    for (ATDiscoveryDeviceSecondLevelBean str : list) {
                        //这里根据需求，添加匹配规则
                        if (str.getProductName().contains(charString)) {
                            filteredList.add(str);
                        }
                    }

                    mFilterList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilterList;
                return filterResults;
            }

            //把过滤后的值返回出来
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilterList = (ArrayList<ATDiscoveryDeviceSecondLevelBean>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int compare(ATDiscoveryDeviceSecondLevelBean t, ATDiscoveryDeviceSecondLevelBean t1) {
        int c1 = (t.getProductName().charAt(0) + "").toUpperCase().hashCode();
        int c2 = (t1.getProductName().charAt(0) + "").toUpperCase().hashCode();

        boolean c1Flag = (c1 < "A".hashCode() || c1 > "Z".hashCode()); // 不是字母
        boolean c2Flag = (c2 < "A".hashCode() || c2 > "Z".hashCode()); // 不是字母
        if (c1Flag && !c2Flag) {
            return 1;
        } else if (!c1Flag && c2Flag) {
            return -1;
        }

        return c1 - c2;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout mRlContent;
        private TextView mTvDevice;
        private ImageView mImgDevice;
        private ImageView mImgJump;

        private ViewHolder(View itemView) {
            super(itemView);
            ATAutoUtils.autoSize(itemView);
            mRlContent = itemView.findViewById(R.id.rl_content);
            mTvDevice = itemView.findViewById(R.id.tv_device);
            mImgDevice = itemView.findViewById(R.id.img_device);
            mImgJump = itemView.findViewById(R.id.img_jump);
        }

        public void setData(int position) {
            mTvDevice.setText(mFilterList.get(position).getProductName());
            Glide.with(mContext).load(mFilterList.get(position).getCategoryUrl()).apply(options).into(mImgDevice);
            mRlContent.setOnClickListener(view -> {
                if (ATConstants.ProductKey.ESP_TOUCH.equals(list.get(position).getProductKey())) {
                    mContext.startActivityForResult(new Intent(mContext, EspTouchActivity.class), 10001);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("productKey", mFilterList.get(position).getProductKey());
//                bundle.putString("deviceName", mFilterList.get(position).getProductName());
                    Router.getInstance().toUrlForResult(mContext, ATConstants.RouterUrl.PLUGIN_ID_DEVICE_CONFIG,
                            ATAddDeviceScanHelper.REQUEST_CODE_CONFIG_WIFI, bundle);
                }
            });
        }
    }

    private void initSort() {
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < mSortList.size(); i++) {
            String pinyin = ATSortUtils.getPingYin(mSortList.get(i).getProductName());
            map.put(pinyin, mSortList.get(i).getProductName());
            mSortList.get(i).setProductName(pinyin);
        }
        Collections.sort(mSortList, this::compare);
        for (int i = 0; i < mSortList.size(); i++) {
            for (int j = 0; j < mSortList.size(); j++) {
                if (mSortList.get(i).getProductKey().equals(mFilterList.get(j).getProductKey())) {
                    Collections.swap(mFilterList, i, j);
                }
            }
        }
    }

}