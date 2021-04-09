package com.aliyun.ayland.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class ATPreferencesUtils {

	public static String PREFERENCE_NAME = "door_preference";
	
	public static String getString(Context context, String key, String defaultStr)
	{
		return getString(context,PREFERENCE_NAME,key,defaultStr);
	}
		
	public static String getString(Context context, String preferencesName, String key, String defaultStr) {
		SharedPreferences settings = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
		return settings.getString(key, defaultStr);
	}
	
	public static boolean putString(Context context, String key, String value)
	{
		return putString(context,PREFERENCE_NAME,key,value);
	}
	
	public static boolean putString(Context context, String preferencesName, String key, String value)
	{
		SharedPreferences settings = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value);
		return editor.commit();
	}
	
	public static boolean removeKey(Context context, String key)
	{
		SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.remove(key);
		return editor.commit();
	}
	
	/**
	 * put int preferences
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to modify
	 * @param value
	 *            The new value for the preference
	 * @return True if the new values were successfully written to persistent
	 *         storage.
	 */
	public static boolean putInt(Context context, String key, int value) {
		return putInt(context, PREFERENCE_NAME, key, value);
	}

	public static boolean putInt(Context context, String preferencesName, String key, int value) {
		SharedPreferences settings = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(key, value);
		return editor.commit();
	}
	
	/**
	 * get int preferences
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to retrieve
	 * @param defaultValue
	 *            Value to return if this preference does not exist
	 * @return The preference value if it exists, or defValue. Throws
	 *         ClassCastException if there is a preference with this name that
	 *         is not a int
	 */
	public static int getInt(Context context, String key, int defaultValue) {
		return getInt(context, PREFERENCE_NAME, key, defaultValue);
	}
	
	public static int getInt(Context context, String preferencesName, String key, int defaultValue) {
		SharedPreferences settings = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
		return settings.getInt(key, defaultValue);
	}
	
	/**
	 * put long preferences
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to modify
	 * @param value
	 *            The new value for the preference
	 * @return True if the new values were successfully written to persistent
	 *         storage.
	 */
	public static boolean putLong(Context context, String key, long value) {
		return putLong(context, PREFERENCE_NAME, key, value);
	}
	
	public static boolean putLong(Context context, String preferencesName, String key, long value) {
		SharedPreferences settings = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong(key, value);
		return editor.commit();
	}
	
	/**
	 * get long preferences
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to retrieve
	 * @param defaultValue
	 *            Value to return if this preference does not exist
	 * @return The preference value if it exists, or defValue. Throws
	 *         ClassCastException if there is a preference with this name that
	 *         is not a long
	 */
	public static long getLong(Context context, String key, long defaultValue) {
		return getLong(context, PREFERENCE_NAME, key, defaultValue);
	}

	public static long getLong(Context context, String preferencesName, String key, long defaultValue) {
		SharedPreferences settings = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
		return settings.getLong(key, defaultValue);
	}
	
	/**
	 * put float preferences
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to modify
	 * @param value
	 *            The new value for the preference
	 * @return True if the new values were successfully written to persistent
	 *         storage.
	 */
	public static boolean putFloat(Context context, String key, float value) {
		return putFloat(context, PREFERENCE_NAME, key, value);
	}

	public static boolean putFloat(Context context, String preferencesName, String key, float value) {
		SharedPreferences settings = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putFloat(key, value);
		return editor.commit();
	}
	
	/**
	 * get float preferences
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to retrieve
	 * @param defaultValue
	 *            Value to return if this preference does not exist
	 * @return The preference value if it exists, or defValue. Throws
	 *         ClassCastException if there is a preference with this name that
	 *         is not a float
	 */
	public static float getFloat(Context context, String key, float defaultValue) {
		return getFloat(context, PREFERENCE_NAME, key, defaultValue);
	}
	
	public static float getFloat(Context context, String preferencesName, String key, float defaultValue) {
		SharedPreferences settings = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
		return settings.getFloat(key, defaultValue);
	}
	
	/**
	 * put boolean preferences
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to modify
	 * @param value
	 *            The new value for the preference
	 * @return True if the new values were successfully written to persistent
	 *         storage.
	 */
	public static boolean putBoolean(Context context, String key, boolean value) {
		return putBoolean(context, PREFERENCE_NAME, key, value);
	}

	public static boolean putBoolean(Context context, String preferencesName, String key, boolean value) {
		SharedPreferences settings = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(key, value);
		return editor.commit();
	}
	
	/**
	 * get boolean preferences
	 * 
	 * @param context
	 * @param key
	 *            The name of the preference to retrieve
	 * @param defaultValue
	 *            Value to return if this preference does not exist
	 * @return The preference value if it exists, or defValue. Throws
	 *         ClassCastException if there is a preference with this name that
	 *         is not a boolean
	 */
	public static boolean getBoolean(Context context, String key, boolean defaultValue) {
		return getBoolean(context, PREFERENCE_NAME, key, defaultValue);
	}
	
	public static boolean getBoolean(Context context, String preferencesName, String key, boolean defaultValue) {
		SharedPreferences settings = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
		return settings.getBoolean(key, defaultValue);
	}
	
	
	
	/**
	 * clear the preferences
	 * 
	 * @param context
	 */
	public static void clear(Context context) {
		clear(context, PREFERENCE_NAME);
	}
	
	/**
	 * clear the preferences
	 * 
	 * @param context
	 * @param preferenceName
	 */
	public static void clear(Context context, String preferenceName) {
		
		SharedPreferences settings = context.getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.clear(); 
		editor.commit();
	}
}