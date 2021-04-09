package com.aliyun.ayland.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.OpenAccountService;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.aliyun.ayland.contract.MainContract;
import com.aliyun.ayland.data.LoginBean;
import com.aliyun.ayland.global.ATApplication;
import com.aliyun.ayland.global.Constants;
import com.aliyun.ayland.global.WebSockIO;
import com.aliyun.ayland.presenter.LoginPresenter;
import com.aliyun.ayland.ui.activity.LoginActivity;
import com.aliyun.ayland.utils.CallbackUtil;
import com.aliyun.ayland.utils.LoginCallBack;
import com.aliyun.iot.aep.sdk.login.ILoginCallback;
import com.aliyun.iot.aep.sdk.login.ILogoutCallback;
import com.aliyun.iot.aep.sdk.login.LoginBusiness;
import com.aliyun.iot.aep.sdk.login.oa.OALoginAdapter;
import com.google.gson.Gson;

import org.json.JSONException;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MyOALoginAdapter extends OALoginAdapter implements MainContract.View {
    private Context context;
    private String TAG = "MyOALoginAdapter";
    private LoginPresenter mPresenter;
    private static String mAccount, mPassword;
    private static LoginCallBack mLoginCallback;
    private Gson gson;

    public MyOALoginAdapter(Context context) {
        super(context);
        this.context = context;
        gson = new Gson();
        mPresenter = new LoginPresenter(this);
        mPresenter.install(context);
    }

    @Override
    public void login(ILoginCallback callback) {
        if (TextUtils.isEmpty(mAccount)) {
            //打开三方登录页面
            Intent intent = new Intent();
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
            intent.setClass(context, LoginActivity.class);
            context.startActivity(intent);
        } else
            appLogin();
        CallbackUtil.setCallBack(authCode -> authLogin(authCode, new OALoginCallback(callback)));
    }

    private void authLogin(String authCode, LoginCallback mLoginCallback) {
        Log.e(TAG, "authLogin: " + authCode + mLoginCallback);
        //authCode 是三方登录页面返回的code
        OpenAccountService service = OpenAccountSDK.getService(OpenAccountService.class);
        service.authCodeLogin(context, authCode, mLoginCallback);
    }

    /**
     * 登录
     *
     * @param account password loginCallback
     */
    public static void login(String account, String password, LoginCallBack loginCallback) {
        mLoginCallback = loginCallback;
        if (LoginBusiness.isLogin() && account.equals(ATApplication.getAccount()) && !TextUtils.isEmpty(ATApplication.getLoginBeanStr())) {
            loginCallback.onSuccess();
        } else {
            mAccount = account;
            mPassword = password;
            LoginBusiness.login(new ILoginCallback() {
                @Override
                public void onLoginSuccess() {
                    loginCallback.onSuccess();
                }

                @Override
                public void onLoginFailed(int code, String error) {
                    loginCallback.onFailure(code, error);
                }
            });
        }
    }

    /**
     * 退出登录
     */
    public static void logout() {
        LoginBusiness.logout(new ILogoutCallback() {
            @Override
            public void onLogoutSuccess() {
                ATApplication.setAllDeviceData("");
                ATApplication.setAccount("");
                ATApplication.setLoginBeanStr("");
            }

            @Override
            public void onLogoutFailed(int code, String error) {
            }
        });
    }

    private void appLogin() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", mAccount);
        jsonObject.put("password", mPassword);
        mPresenter.request(Constants.Config.SERVER_URL_APPLOGIN, jsonObject);
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            if (TextUtils.isEmpty(result))
                return;
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if (Constants.Config.SERVER_URL_APPLOGIN.equals(url)) {
                if ("200".equals(jsonResult.getString("code"))) {
                    LoginBean loginBean = gson.fromJson(result, LoginBean.class);
                    ATApplication.setAccount(mAccount);
                    ATApplication.setLoginBeanStr(result);
                    CallbackUtil.doCallBackMethod(loginBean.getAuthCode());

                    WebSockIO.getInstance().closeSock();
                    WebSockIO.getInstance().setUpConnect();
                } else {
                    mLoginCallback.onFailure(jsonResult.getInt("code"), jsonResult.getString("message"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}