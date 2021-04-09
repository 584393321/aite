package com.aliyun.ayland.utils;


import com.aliyun.ayland.global.ATGlobalApplication;

public class ATResourceUtils {
	public static class ResourceType{
		public static String DRAWABLE = "drawable";
		public static String STRING  = "string";
		public static String DIMENS = "dimen";
	}

	public static int getResIdByName(String name, String defType) {
		String packageName = ATGlobalApplication.getContext().getApplicationInfo().packageName;
		return ATGlobalApplication.getContext().getResources().getIdentifier(name, defType, packageName);
	}

	public static float getDimen(int resId)
	{
		return ATGlobalApplication.getContext().getResources().getDimension(resId);
	}
	
	public static String getString(int resId)
	{
		return ATGlobalApplication.getContext().getResources().getString(resId);
	}
	
	public static String[] getStringArray(int resId)
	{
		return ATGlobalApplication.getContext().getResources().getStringArray(resId);
	}
	
	public static int getColor(int resId)
	{
		return ATGlobalApplication.getContext().getResources().getColor(resId);
	}
	
}
