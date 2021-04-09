package com.aliyun.ayland.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.OpenAccountService;
import com.alibaba.sdk.android.openaccount.callback.LoginCallback;
import com.aliyun.ayland.contract.ATMainContract;
import com.aliyun.ayland.data.ATAuthCodeBean;
import com.aliyun.ayland.data.ATLoginBean;
import com.aliyun.ayland.global.ATConstants;
import com.aliyun.ayland.global.ATGlobalApplication;
import com.aliyun.ayland.global.ATWebSockIO;
import com.aliyun.ayland.presenter.ATLoginPresenter;
import com.aliyun.ayland.ui.activity.ATEmptyActivity;
import com.aliyun.ayland.ui.activity.ATLoginActivity;
import com.aliyun.ayland.utils.ATCallbackUtil;
import com.aliyun.ayland.utils.ATLoginCallBack;
import com.aliyun.iot.aep.sdk.login.ILoginCallback;
import com.aliyun.iot.aep.sdk.login.ILogoutCallback;
import com.aliyun.iot.aep.sdk.login.LoginBusiness;
import com.aliyun.iot.aep.sdk.login.oa.OALoginAdapter;
import com.google.gson.Gson;

import org.json.JSONException;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class ATMyOALoginAdapter extends OALoginAdapter implements ATMainContract.View {
    private Context context;
    private String TAG = "ATMyOALoginAdapter";
    private ATLoginPresenter mPresenter;
    private static String mAccount, mPassword;
    private static ATLoginCallBack mLoginCallback;
    private Gson gson;

    public ATMyOALoginAdapter(Context context) {
        super(context);
        this.context = context;
        gson = new Gson();
        mPresenter = new ATLoginPresenter(this);
        mPresenter.install(context);
    }

    @Override
    public void login(ILoginCallback callback) {
        if (TextUtils.isEmpty(mAccount)) {
            //打开三方登录页面
            Intent intent = new Intent();
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK);
            intent.setClass(context, ATLoginActivity.class);
            context.startActivity(intent);
        } else
            login();
        ATCallbackUtil.setCallBack(authCode -> authLogin(authCode, new OALoginCallback(callback)));
    }

    private void authLogin(String authCode, LoginCallback mLoginCallback) {
        //authCode 是三方登录页面返回的code
        OpenAccountService service = OpenAccountSDK.getService(OpenAccountService.class);
        service.authCodeLogin(context, authCode, mLoginCallback);
    }

    /**
     * 登录
     * @param account password loginCallback
     */
    public static void login(String account, String password, ATLoginCallBack loginCallback) {
        mLoginCallback = loginCallback;
        if (LoginBusiness.isLogin() && account.equals(ATGlobalApplication.getAccount()) && !TextUtils.isEmpty(ATGlobalApplication.getLoginBeanStr())) {
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
                ATGlobalApplication.setAllDeviceData("");
                ATGlobalApplication.setAccount("");
                ATGlobalApplication.setLoginBeanStr("");
            }

            @Override
            public void onLogoutFailed(int code, String error) {
            }
        });
    }

    private void getAuthCode() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", mAccount);
        jsonObject.put("password", mPassword);
        mPresenter.request(ATConstants.Config.SERVER_URL_GETAUTHCODE, jsonObject);
    }

    private void login() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", mAccount);
        jsonObject.put("password", mPassword);
        mPresenter.request(ATConstants.Config.SERVER_URL_LOGIN, jsonObject);
    }

    @Override
    public void requestResult(String result, String url) {
        try {
            org.json.JSONObject jsonResult = new org.json.JSONObject(result);
            if (ATConstants.Config.SERVER_URL_LOGIN.equals(url)) {
                if ("200".equals(jsonResult.getString("code"))) {
                    ATLoginBean mATLoginBean = gson.fromJson(jsonResult.getString("result"), ATLoginBean.class);
                    ATGlobalApplication.setAccount(mAccount);
                    if (mATLoginBean.getHouse() != null) {
                        ATGlobalApplication.setHouse(mATLoginBean.getHouse().toString());
                    }
                    ATGlobalApplication.setLoginBeanStr(jsonResult.getString("result"));

                    if (mATLoginBean.isHasHouse() && mATLoginBean.getHouse() != null) {
                        getAuthCode();
                    } else {
                        //游客
                        context.startActivity(new Intent(context, ATEmptyActivity.class));
                    }
                } else {
                    mLoginCallback.onFailure(jsonResult.getInt("code"), jsonResult.getString("message"));
                }
            } else if (ATConstants.Config.SERVER_URL_GETAUTHCODE.equals(url)) {
                ATAuthCodeBean aTLogin = gson.fromJson(jsonResult.toString(), ATAuthCodeBean.class);
                ATCallbackUtil.doCallBackMethod(aTLogin.getAuthCode());
                ATGlobalApplication.setAuthCode(aTLogin.getAuthCode());
                ATWebSockIO.getInstance().closeSock();
                ATWebSockIO.getInstance().setUpConnect();

                if (aTLogin.getHouse() != null) {
                    ATGlobalApplication.setHouse(aTLogin.getHouse().toString());
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}