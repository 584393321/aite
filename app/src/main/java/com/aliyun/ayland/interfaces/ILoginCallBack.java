package com.aliyun.ayland.interfaces;

public interface ILoginCallBack {
	void onLoginSuccess();
	void onLoginFailed(int code, String error);
}
