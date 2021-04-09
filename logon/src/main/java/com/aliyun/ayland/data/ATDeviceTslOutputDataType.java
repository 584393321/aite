package com.aliyun.ayland.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.JSONObject;

/**
 * @author guikong on 18/4/12.
 */
public class ATDeviceTslOutputDataType implements Parcelable {
    private JSONObject dataType;
    private String identifier;
    private String name;

    public JSONObject getDataType() {
        return dataType;
    }

    public void setDataType(JSONObject dataType) {
        this.dataType = dataType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "ATDeviceTslOutputDataType{" +
                "dataType=" + dataType +
                ", identifier='" + identifier + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeSerializable(dataType);
        out.writeString(name);
        out.writeString(identifier);
    }

    public static final Creator<ATDeviceTslOutputDataType> CREATOR = new Creator<ATDeviceTslOutputDataType>() {
        @Override
        public ATDeviceTslOutputDataType[] newArray(int size) {
            return new ATDeviceTslOutputDataType[size];
        }

        @Override
        public ATDeviceTslOutputDataType createFromParcel(Parcel in) {
            return new ATDeviceTslOutputDataType(in);
        }
    };

    public ATDeviceTslOutputDataType() {

    }

    public ATDeviceTslOutputDataType(Parcel in) {
        dataType = (JSONObject) in.readSerializable();
        name = in.readString();
        identifier = in.readString();
    }
}
