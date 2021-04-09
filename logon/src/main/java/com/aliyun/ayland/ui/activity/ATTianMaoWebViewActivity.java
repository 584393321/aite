package com.aliyun.ayland.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.aliyun.ayland.base.ATBaseActivity;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.presenter.ATMainPresenter;
import com.aliyun.ayland.widget.titlebar.ATMyTitleBar;
import com.anthouse.xuhui.R;

import org.json.JSONException;

public class ATTianMaoWebViewActivity extends ATBaseActivity implements ATMainContract.View {
    private ATMainPresenter mPresenter;
    private String mAuthCode;
    private ATMyTitleBar titlebar;
    private WebView webview;

    @Override
    protected int getLayoutId() {
        return R.layout.at_activity_webview_tianmao;
    }

    @Override
    protected void findView() {
        titlebar = findViewById(R.id.titlebar);
        webview = findViewById(R.id.webview);
        init();
    }

    @Override
    protected void initPresenter() {
        mPresenter = new ATMainPresenter(this);
        mPresenter.install(this);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
//        String url = "https://oauth.taobao.com/authorize?response_type=code&client_id=28106845&redirect_uri=http://alisaas.atsmartlife.com:9001/villagecenter/v1/tmall&view=wap";
        String url = "https://oauth.taobao.com/authorize?response_type=code&client_id=" + ATConstants.Config.APPKEY + "&redirect_uri="
                + ATConstants.Config.SERVER_URL_TMALL + "&view=wap";
        webview.setScrollbarFadingEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAppCacheEnabled(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webview.requestFocus();
        webview.setWebViewClient(new WebViewClient() {
            //设置结束加载函数
            @Override
            public void onPageFinished(WebView view, String url) {
                titlebar.setTitle(view.getTitle());
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (isTokenUrl(url)) {
                    Intent intent = new Intent();
                    intent.putExtra("AuthCode", mAuthCode);
                    setResult(RESULT_OK, intent);
                    finish();
                    return true;
                }
                view.loadUrl(url);
                return false;
            }
        });
        webview.loadUrl(url);
    }

    private boolean isTokenUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            if (url.contains("code=")) {
                String[] urlArray = url.split("code=");
                if (urlArray.length > 1) {
                    String[] paramArray = urlArray[1].split("&");
                    if (paramArray.length > 1) {
                        mAuthCode = paramArray[0];
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if ("200".equals(jsonResult.getString("code"))) {
                switch (url) {
                    case ATConstants.Config.SERVER_URL_HOUSEDEVICE:

                        break;
                }
            } else {
                closeBaseProgressDlg();
                showToast(jsonResult.getString("message"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
