package com.aliyun.ayland.utils;

import com.alibaba.fastjson.JSON;
import com.aliyun.ayland.data.Category;
import com.aliyun.ayland.data.ATProduct;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.listener.ATOnGetCategoryCompletedListener;
import com.aliyun.ayland.listener.ATOnGetProductCompletedListener;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.aliyun.iot.aep.sdk.log.ALog;
import com.aliyun.iot.aep.sdk.threadpool.ThreadPool;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 品类列表/产品列表的获取业务封装
 *
 * @author guikong on 18/4/8.
 */
public class ATSelectProductBusiness {

    private static final String TAG = "ATSelectProductBusiness";

    public void getCategories(String productName, int page, int pageSize, final ATOnGetCategoryCompletedListener listener) {
        com.alibaba.fastjson.JSONObject userObject = new com.alibaba.fastjson.JSONObject();
        userObject.put("hid", ATGlobalApplication.getOpenId());
        userObject.put("hidType", "OPEN");
        com.alibaba.fastjson.JSONObject productQuery = new com.alibaba.fastjson.JSONObject();
        productQuery.put("pageNo", page);
        productQuery.put("pageSize", pageSize);
        productQuery.put("categoryKey", "");
        productQuery.put("productName", productName);
        IoTRequest request = new IoTRequestBuilder()
                .setPath("/home/paas/product/list")
                .setApiVersion("1.0.1")
                .addParam("operator", userObject)
                .addParam("productQuery", productQuery)
                .build();
        new IoTAPIClientFactory().getClient().send(request, new IoTCallback() {
            @Override
            public void onFailure(IoTRequest ioTRequest, final Exception e) {
                ThreadPool.MainThreadHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            listener.onFailed(e);
                        } catch (Exception ex) {
                            ALog.e(TAG, "exception happen when call listener.onFailed", ex);
                            ex.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onResponse(final IoTRequest ioTRequest, final IoTResponse ioTResponse) {
                if (200 != ioTResponse.getCode()) {
                    ThreadPool.MainThreadHandler.getInstance().post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                listener.onFailed(ioTResponse.getCode(), ioTResponse.getMessage(), ioTResponse.getLocalizedMsg());
                            } catch (Exception ex) {
                                ALog.e(TAG, "exception happen when call listener.onFailed", ex);
                                ex.printStackTrace();
                            }
                        }
                    });
                    return;
                }

                if (!(ioTResponse.getData() instanceof JSONObject)) {
                    return;
                }

                int totalNum;
                List<Category> categories = new ArrayList<>();

                JSONObject data = (JSONObject) ioTResponse.getData();
                totalNum = data.optInt("total");

                JSONArray items = data.optJSONArray("data");

                if (null != items
                        && items.length() > 0) {
                    String jsonStr = items.toString();
                    categories = JSON.parseArray(jsonStr, Category.class);
                }

                final int finalTotalNum = totalNum;
                final List<Category> finalCategories = categories;
                ThreadPool.MainThreadHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            listener.onSuccess(finalTotalNum, finalCategories);
                        } catch (Exception ex) {
                            ALog.e(TAG, "exception happen when call listener.onSuccess", ex);
                            ex.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public void getCategories(int page, int pageSize, final ATOnGetCategoryCompletedListener listener) {
        com.alibaba.fastjson.JSONObject userObject = new com.alibaba.fastjson.JSONObject();
        userObject.put("hid", ATGlobalApplication.getOpenId());
        userObject.put("hidType", "OPEN");
        com.alibaba.fastjson.JSONObject productQuery = new com.alibaba.fastjson.JSONObject();
        productQuery.put("pageNo", page);
        productQuery.put("pageSize", pageSize);
        productQuery.put("categoryKey", "");
        productQuery.put("productName", "");
        IoTRequest request = new IoTRequestBuilder()
                .setPath("/home/paas/product/list")
                .setApiVersion("1.0.1")
                .addParam("operator", userObject)
                .addParam("productQuery", productQuery)
                .build();
        new IoTAPIClientFactory().getClient().send(request, new IoTCallback() {
            @Override
            public void onFailure(IoTRequest ioTRequest, final Exception e) {
                ThreadPool.MainThreadHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            listener.onFailed(e);
                        } catch (Exception ex) {
                            ALog.e(TAG, "exception happen when call listener.onFailed", ex);
                            ex.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onResponse(final IoTRequest ioTRequest, final IoTResponse ioTResponse) {
                if (200 != ioTResponse.getCode()) {
                    ThreadPool.MainThreadHandler.getInstance().post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                listener.onFailed(ioTResponse.getCode(), ioTResponse.getMessage(), ioTResponse.getLocalizedMsg());
                            } catch (Exception ex) {
                                ALog.e(TAG, "exception happen when call listener.onFailed", ex);
                                ex.printStackTrace();
                            }
                        }
                    });
                    return;
                }

                if (!(ioTResponse.getData() instanceof JSONObject)) {
                    return;
                }

                int totalNum;
                List<Category> categories = new ArrayList<>();

                JSONObject data = (JSONObject) ioTResponse.getData();
                totalNum = data.optInt("total");

                JSONArray items = data.optJSONArray("data");

                if (null != items
                        && items.length() > 0) {
                    String jsonStr = items.toString();
                    categories = JSON.parseArray(jsonStr, Category.class);
                }

                final int finalTotalNum = totalNum;
                final List<Category> finalCategories = categories;
                ThreadPool.MainThreadHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            listener.onSuccess(finalTotalNum, finalCategories);
                        } catch (Exception ex) {
                            ALog.e(TAG, "exception happen when call listener.onSuccess", ex);
                            ex.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @SuppressWarnings("unused")
    public void getProductsByCategory(String categoryName, String categoryKey, int page, int pageSize, final ATOnGetProductCompletedListener listener) {
        com.alibaba.fastjson.JSONObject userObject = new com.alibaba.fastjson.JSONObject();
        userObject.put("hid", ATGlobalApplication.getOpenId());
        userObject.put("hidType", "OPEN");
        com.alibaba.fastjson.JSONObject productQuery = new com.alibaba.fastjson.JSONObject();
        productQuery.put("pageNo", page);
        productQuery.put("pageSize", pageSize);
        productQuery.put("categoryKey", categoryKey);
        productQuery.put("productName", categoryName);
        IoTRequest request = new IoTRequestBuilder()
                .setPath("/home/paas/product/list")
                .setApiVersion("1.0.1")
                .addParam("operator", userObject)
                .addParam("productQuery", productQuery)
                .build();
        new IoTAPIClientFactory().getClient().send(request, new IoTCallback() {
            @Override
            public void onFailure(IoTRequest ioTRequest, final Exception e) {
                ThreadPool.MainThreadHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            listener.onFailed(e);
                        } catch (Exception ex) {
                            ALog.e(TAG, "exception happen when call listener.onFailed", ex);
                            ex.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onResponse(final IoTRequest ioTRequest, final IoTResponse ioTResponse) {
                if (200 != ioTResponse.getCode()) {
                    ThreadPool.MainThreadHandler.getInstance().post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                listener.onFailed(ioTResponse.getCode(), ioTResponse.getMessage(), ioTResponse.getLocalizedMsg());
                            } catch (Exception ex) {
                                ALog.e(TAG, "exception happen when call listener.onFailed", ex);
                                ex.printStackTrace();
                            }
                        }
                    });
                    return;
                }

                if (!(ioTResponse.getData() instanceof JSONObject)) {
                    return;
                }

                int totalNum;
                List<ATProduct> ATProducts = new ArrayList<>();

                JSONObject data = (JSONObject) ioTResponse.getData();
                totalNum = data.optInt("total");

                JSONArray items = data.optJSONArray("data");

                if (null != items
                        && items.length() > 0) {
                    String jsonStr = items.toString();
                    ATProducts = JSON.parseArray(jsonStr, ATProduct.class);
                }

                final int finalTotalNum = totalNum;
                final List<ATProduct> finalATProducts = ATProducts;
                ThreadPool.MainThreadHandler.getInstance().post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            listener.onSuccess(finalTotalNum, finalATProducts);
                        } catch (Exception ex) {
                            ALog.e(TAG, "exception happen when call listener.onSuccess", ex);
                            ex.printStackTrace();
                        }
                    }
                });
            }
        });
    }
}
