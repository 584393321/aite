package com.aliyun.ayland.ui.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.utils.ATSystemStatusBarUtils;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.aliyun.iot.aep.sdk.threadpool.ThreadPool;
import com.anthouse.xuhui.R;

import java.util.Map;

import static com.aliyun.ayland.ui.activity.ATTmallVoiceWizardActivity.REQUEST_CODE_AUTHORIZE;

public class ATTmallWizardActivity extends ATBaseActivity {
    private TextView tvBack;
    private Button button;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_tmall_wizard;
    }

    @Override
    protected void findView() {
        tvBack = findViewById(R.id.tv_back);
        button = findViewById(R.id.button);
        init();
    }

    @Override
    protected void initPresenter() {
    }

    private void init() {
        ATSystemStatusBarUtils.init(this, false);
        tvBack.setOnClickListener(v -> finish());
        button.setOnClickListener(v -> {
            if (getString(R.string.at_tmall_wizard_authorization).equals(button.getText().toString()))
                startActivityForResult(new Intent(ATTmallWizardActivity.this, ATTianMaoWebViewActivity.class)
                        , REQUEST_CODE_AUTHORIZE);
            else
                unbind();
        });
        getBind();
    }


    public void getBind() {
        IoTRequest ioTRequest = new IoTRequestBuilder()
                .setAuthType("iotAuth")
                .setApiVersion("1.0.5")
                .setPath("/account/thirdparty/get")
                .addParam("accountType", "TAOBAO")
                .setScheme(Scheme.HTTPS)
                .build();
        new IoTAPIClientFactory().getClient().send(ioTRequest, new IoTCallback() {
            @Override
            public void onFailure(IoTRequest ioTRequest, Exception e) {
                Log.e("onResponse: ", e.getMessage());
            }

            @Override
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                ThreadPool.MainThreadHandler.getInstance().post(() -> {
                    if (TextUtils.isEmpty(ioTResponse.getData().toString()))
                        button.setText(getString(R.string.at_tmall_wizard_authorization));
                    else
                        button.setText(getString(R.string.at_cancel_authorization));
                });
                Log.e("onResponse: ", ioTResponse.getCode() + "---" + ioTResponse.getMessage() + "---" + ioTResponse.getLocalizedMsg());
            }
        });
    }

    public void unbind() {
        IoTRequest ioTRequest = new IoTRequestBuilder()
                .setAuthType("iotAuth")
                .setApiVersion("1.0.5")
                .setPath("/account/thirdparty/unbind")
                .addParam("accountType", "TAOBAO")
                .setScheme(Scheme.HTTPS)
                .build();
        new IoTAPIClientFactory().getClient().send(ioTRequest, new IoTCallback() {
            @Override
            public void onFailure(IoTRequest ioTRequest, Exception e) {
                ThreadPool.MainThreadHandler.getInstance().post(() -> showToast(e.getMessage()));
            }

            @Override
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                ThreadPool.MainThreadHandler.getInstance().post(() -> {
                    button.setText(getString(R.string.at_tmall_wizard_authorization));
                    if (ioTResponse.getCode() == 200)
                        showToast(getString(R.string.at_unbind_success));
                    else
                        showToast(ioTResponse.getMessage());
                    setResult(RESULT_OK);
                });
            }
        });
    }

    public void bindAccount(String authCode) {
        JSONObject params = new JSONObject();
        if (null != authCode)
            params.put("authCode", authCode);
        Map<String, Object> requestMap = params.getInnerMap();
        IoTRequest ioTRequest = new IoTRequestBuilder()
                .setAuthType("iotAuth")
                .setApiVersion("1.0.5")
                .setPath("/account/taobao/bind")
                .setParams(requestMap)
                .setScheme(Scheme.HTTPS)
                .build();
        new IoTAPIClientFactory().getClient().send(ioTRequest, new IoTCallback() {
            @Override
            public void onFailure(IoTRequest ioTRequest, Exception e) {
                Log.e("weionResponse: ", e.getMessage());
            }

            @Override
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                if (200 != ioTResponse.getCode()) {
                    Log.e("weionResponse: ", ioTResponse.getCode() + "---" + ioTResponse.getMessage() + "---" + ioTResponse.getLocalizedMsg());
                    return;
                }
                ThreadPool.MainThreadHandler.getInstance().post(() -> {
                    showToast(getString(R.string.at_authorization_success));
                    button.setText(getString(R.string.at_cancel_authorization));
                });
                setResult(RESULT_OK);
                Log.e("onResponse: ", ioTResponse.getData().toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_AUTHORIZE)
            bindAccount(data.getStringExtra("AuthCode"));
    }
}