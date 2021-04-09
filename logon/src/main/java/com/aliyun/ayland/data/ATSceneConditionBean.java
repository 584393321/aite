package com.aliyun.ayland.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.JSONObject;

public class ATSceneConditionBean implements Parcelable {
    private JSONObject params;
    private String uri;

    public JSONObject getParams() {
        return params;
    }

    public void setParams(JSONObject params) {
        this.params = params;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeSerializable(params);
        out.writeString(uri);
    }

    public static final Creator<ATSceneConditionBean> CREATOR = new Creator<ATSceneConditionBean>() {
        @Override
        public ATSceneConditionBean[] newArray(int size) {
            return new ATSceneConditionBean[size];
        }

        @Override
        public ATSceneConditionBean createFromParcel(Parcel in) {
            return new ATSceneConditionBean(in);
        }
    };

    public ATSceneConditionBean() {

    }

    private ATSceneConditionBean(Parcel in) {
        params = (JSONObject)in.readSerializable();
        uri = in.readString();
    }
}
