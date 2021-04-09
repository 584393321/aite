package com.aliyun.ayland.utils;


import com.aliyun.ayland.global.ATApplication;

public class ResourceUtils {
	public static class ResourceType{
		public static String DRAWABLE = "drawable";
		public static String STRING  = "string";
		public static String DIMENS = "dimen";
	}

	public static int getResIdByName(String name, String defType) {
		String packageName = ATApplication.getContext().getApplicationInfo().packageName;
		return ATApplication.getContext().getResources().getIdentifier(name, defType, packageName);
	}

	public static float getDimen(int resId)
	{
		return ATApplication.getContext().getResources().getDimension(resId);
	}
	
	public static String getString(int resId)
	{
		return ATApplication.getContext().getResources().getString(resId);
	}
	
	public static String[] getStringArray(int resId)
	{
		return ATApplication.getContext().getResources().getStringArray(resId);
	}
	
	public static int getColor(int resId)
	{
		return ATApplication.getContext().getResources().getColor(resId);
	}
	
}
