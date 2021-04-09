package com.aliyun.ayland.widget.voip;

import android.content.Context;
import android.content.SharedPreferences;

import com.anthouse.xuhui.R;


/**
 * Created by lhr on 2018/7/19.
 */
public class CacheManager {

    private static CacheManager cacheManager;
    private SharedPreferences sharedPreferences;

    public static CacheManager getInstance() {
        if (cacheManager == null) {
            cacheManager = new CacheManager();
        }
        return cacheManager;
    }

    public void init(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE);
    }

    private void save(String key, Object value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (value instanceof Integer)
            editor.putInt(key, (Integer) value);
        else if (value instanceof Long)
            editor.putLong(key, (Long) value);
        else if (value instanceof Boolean)
            editor.putBoolean(key, (Boolean) value);
        else if (value instanceof String)
            editor.putString(key, (String) value);
        else if (value instanceof Float)
            editor.putFloat(key, (Float) value);
        editor.apply();
    }

    private String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    private int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }
    private static final String KEY_CACHE_SIP = "key.cache.sip";
    private static final String KEY_CACHE_PASSWORD = "key.cache.password";
    private static final String KEY_CACHE_DOMAIN = "key.access.domain";
    private static final String KEY_CACHE_PORT = "key.refresh.port";

    public void saveSip(String sip) {
        save(KEY_CACHE_SIP, sip);
    }

    public void savePw(String password) {
        save(KEY_CACHE_PASSWORD, password);
    }

    public void saveDomain(String domain) {
        save(KEY_CACHE_DOMAIN, domain);
    }

    public void savePort(int port) {
        save(KEY_CACHE_PORT, port);
    }

    public String getSip() {
        return getString(KEY_CACHE_SIP);
    }

    public String getPw() {
        return getString(KEY_CACHE_PASSWORD);
    }

    public String getDomain() {
        return getString(KEY_CACHE_DOMAIN);
    }

    public int getPort() {
        return getInt(KEY_CACHE_PORT);
    }
}
