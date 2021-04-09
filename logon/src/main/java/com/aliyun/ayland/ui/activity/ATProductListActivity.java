package com.aliyun.ayland.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.data.ATProduct;
import com.aliyun.ayland.listener.ATOnGetProductCompletedListener;
import com.aliyun.ayland.ui.adapter.ATProductListAdapter;
import com.aliyun.ayland.utils.ATSelectProductBusiness;
import com.aliyun.iot.aep.component.router.Router;
import com.aliyun.iot.aep.sdk.log.ALog;
import com.aliyun.iot.link.ui.component.RefreshRecycleViewLayout;
import com.aliyun.iot.link.ui.component.simpleLoadview.LinkSimpleLoadView;
import com.anthouse.xuhui.R;

import java.util.List;

/**
 * @author guikong on 18/4/8.
 */
public class ATProductListActivity extends ATBaseActivity {
    public final static String CODE = "page/productList";
    public static final int REQUEST_CODE = 0x333;
    public final static int REQUEST_CODE_PRODUCT_ADD = 0x444;
    public final static String ARGS_KEY_GROUP_ID = "groupId";
    public final static String ARGS_KEY_ROOM_ID = "roomId";
    public final static String ARGS_KEY_CATEGORY_NAME = "args_key_category_name";
    public final static String ARGS_KEY_CATEGORY_KEY = "args_key_category_key";

    static final String TAG = "ATProductListActivity";

    LinkSimpleLoadView loadView;
    RefreshRecycleViewLayout recyclerView;
    ATProductListAdapter adapter;

    int currentPage = 1;
    int pageSize = 20;
    int loadedNumber = 0;

    boolean isLoadingMore = false;

    String categoryName;
    String categoryKey;
    private RefreshRecycleViewLayout deviceaddProductListSwipeRefreshLayout;
    private LinkSimpleLoadView deviceaddProductListLinkSimpleLoadView;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_product_list;
    }

    @Override
    protected void findView() {
        deviceaddProductListSwipeRefreshLayout = findViewById(R.id.deviceadd_product_list_swipe_refresh_layout);
        deviceaddProductListLinkSimpleLoadView = findViewById(R.id.deviceadd_product_list_link_simple_load_view);
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // read args
        Intent intent = getIntent();
        if (null != intent) {
            categoryKey = intent.getStringExtra(ARGS_KEY_CATEGORY_KEY);
            categoryName = getIntent().getStringExtra(ARGS_KEY_CATEGORY_NAME);
        }

//        checkNotNull(groupId, "groupId can't be null");
//        checkNotNull(categoryKey, "invalid category key");

        // top_bar
//        TextView title = findViewById(R.id.deviceadd_top_bar_title_tv);
//        View backView = findViewById(R.id.deviceadd_top_bar_back_fl);
//        backView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

        // load view
        deviceaddProductListLinkSimpleLoadView.setLoadViewLoacation(1);
        deviceaddProductListLinkSimpleLoadView.setTipViewLoacation(1);
        String loading = getString(R.string.at_deviceadd_loading);
        deviceaddProductListLinkSimpleLoadView.showLoading(loading);

        // swipe refresh layout
        deviceaddProductListSwipeRefreshLayout.setLayoutManager(new LinearLayoutManager(this));
        deviceaddProductListSwipeRefreshLayout.setEnabled(false);
        deviceaddProductListSwipeRefreshLayout.setOnLoadMoreListener(() -> {
            if (!isLoadingMore) {
                loadNextPage();
            }
        });

        // init adapter
        adapter = new ATProductListAdapter();
        loadNextPage();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK != resultCode) {
            return;
        }
        if (REQUEST_CODE_PRODUCT_ADD == requestCode) {
            String productKey = data.getStringExtra("productKey");
            String deviceName = data.getStringExtra("deviceName");
            String token = data.getStringExtra("token");
            String iotId = data.getStringExtra("iotId");
            if (productKey != null) {
                Bundle bundle = new Bundle();
                bundle.putString("token", token);
                bundle.putString("iotId", iotId);
                bundle.putString("productKey", productKey);
                bundle.putString("deviceName", deviceName);
                Router.getInstance().toUrlForResult(this,
                        "page/bindDevice",
                        ATDeviceBindActivity.REQUEST_CODE, bundle);
                finish();
                overridePendingTransition(0, 0);
            } else {
                // 未发现本地设备
            }
        }
    }

    private void loadNextPage() {
        isLoadingMore = true;
        new ATSelectProductBusiness().getProductsByCategory(categoryName, categoryKey, currentPage, pageSize, new ATOnGetProductCompletedListener() {
            @Override
            public void onSuccess(int count, List<ATProduct> ATProducts) {
                isLoadingMore = false;

                // show categories
                adapter.addCategory(ATProducts);

                // if first page, adapter
                // so empty view will dismiss
                if (currentPage == 1) {
                    deviceaddProductListSwipeRefreshLayout.setAdapter(adapter);
                    loadView.hide();
                }

                // increase counters
                loadedNumber += ATProducts.size();
                currentPage++;

                // load finish
                if (loadedNumber >= count) {
                    deviceaddProductListSwipeRefreshLayout.fullLoad();
                }
            }

            @Override
            public void onFailed(Exception e) {
                isLoadingMore = false;

                ALog.e(TAG, "load products failed", e);
                e.printStackTrace();
            }

            @Override
            public void onFailed(int code, String message, String localizedMsg) {
                isLoadingMore = false;
                ALog.e(TAG, "load products failed:" + message);
            }
        });
    }
}
