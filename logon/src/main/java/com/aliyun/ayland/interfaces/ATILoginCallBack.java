package com.aliyun.ayland.interfaces;

public interface ATILoginCallBack {
	void onLoginSuccess();
	void onLoginFailed(int code, String error);
}