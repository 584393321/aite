package com.aliyun.ayland.contract;


import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBasePresenter;
import com.aliyun.ayland.base.ATBaseView;

/**
 * Created by fr on 2018/5/8.
 */

public interface ATMainContract {
    interface Presenter extends ATBasePresenter {
        void request(String url, JSONObject jsonObject);
        void requestResult(String result, String url);
    }

    interface View extends ATBaseView<Presenter> {
        void requestResult(String result, String url);
    }
}
