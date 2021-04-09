package com.aliyun.ayland.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.JSONObject;

/**
 * @author guikong on 18/4/12.
 */
public class ATDeviceTslDataType implements Parcelable {
    private JSONObject specs;
    private String type;
    private String dataType;
    private String name;
    private String attribute;
    private String value;

    public JSONObject getSpecs() {
        return specs;
    }

    public void setSpecs(JSONObject specs) {
        this.specs = specs;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeSerializable(specs);
        out.writeString(type);
        out.writeString(dataType);
        out.writeString(name);
        out.writeString(attribute);
        out.writeString(value);
    }

    public static final Creator<ATDeviceTslDataType> CREATOR = new Creator<ATDeviceTslDataType>() {
        @Override
        public ATDeviceTslDataType[] newArray(int size) {
            return new ATDeviceTslDataType[size];
        }

        @Override
        public ATDeviceTslDataType createFromParcel(Parcel in) {
            return new ATDeviceTslDataType(in);
        }
    };

    public ATDeviceTslDataType() {

    }

    private ATDeviceTslDataType(Parcel in) {
        specs = (JSONObject)in.readSerializable();
        type = in.readString();
        dataType = in.readString();
        name = in.readString();
        attribute = in.readString();
        value = in.readString();
    }
}
