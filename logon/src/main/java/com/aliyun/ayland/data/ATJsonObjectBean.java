package com.aliyun.ayland.data;

import com.alibaba.fastjson.JSONObject;

public class ATJsonObjectBean {
    private String url;
    private JSONObject jsonObject;
    private Object o;

    public ATJsonObjectBean(String url, JSONObject jsonObject) {
        this.url = url;
        this.jsonObject = jsonObject;
    }

    public ATJsonObjectBean(String url, JSONObject jsonObject, Object o) {
        this.url = url;
        this.jsonObject = jsonObject;
        this.o = o;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public Object getO() {
        return o;
    }

    public void setO(Object o) {
        this.o = o;
    }
}
