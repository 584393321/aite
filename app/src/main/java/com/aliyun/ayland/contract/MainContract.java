package com.aliyun.ayland.contract;


import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.BasePresenter;
import com.aliyun.ayland.base.BaseView;

/**
 * Created by fr on 2018/5/8.
 */

public interface MainContract {
    interface Presenter extends BasePresenter {
        void request(String url, JSONObject jsonObject);
        void requestResult(String result, String url);
    }

    interface View extends BaseView<Presenter> {
        void requestResult(String result, String url);
    }
}
