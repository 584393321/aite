package com.aliyun.ayland.global;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import com.xhc.sbh.tool.lists.networks.IpUtils;

public class GlobalBroadCastReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(final Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		if (Intent.ACTION_MY_PACKAGE_REPLACED.equals(action)) {

		} else if(ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
			ConnectivityManager mConnMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (mConnMgr == null)
				return ;
			NetworkInfo aActiveInfo = mConnMgr.getActiveNetworkInfo();
			if(aActiveInfo != null) {
				String ieIp = IpUtils.getLocalnet(ATApplication.getContext());
				if(!TextUtils.isEmpty(ieIp)) {

				}else{
				}
			}
		}
	}
}