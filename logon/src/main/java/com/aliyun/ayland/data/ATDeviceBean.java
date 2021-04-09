package com.aliyun.ayland.data;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author guikong on 18/4/12.
 */
public class ATDeviceBean implements Parcelable {
    public String iotId;
    private String rootSpaceId;
    private String productImage;
    private String nickName;
    public String productKey;
    private String deviceName;
    private String iotSpaceId;
    private String productName;
    private String categoryKey;
    private int status;
    private List<ATDeviceTslDataType> attributes;

    public String getIotId() {
        return iotId;
    }

    public void setIotId(String iotId) {
        this.iotId = iotId;
    }

    public String getRootSpaceId() {
        return rootSpaceId;
    }

    public void setRootSpaceId(String rootSpaceId) {
        this.rootSpaceId = rootSpaceId;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getIotSpaceId() {
        return iotSpaceId;
    }

    public void setIotSpaceId(String iotSpaceId) {
        this.iotSpaceId = iotSpaceId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategoryKey() {
        return categoryKey;
    }

    public void setCategoryKey(String categoryKey) {
        this.categoryKey = categoryKey;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ATDeviceTslDataType> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<ATDeviceTslDataType> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "ATDeviceBean{" +
                "iotId='" + iotId + '\'' +
                ", rootSpaceId='" + rootSpaceId + '\'' +
                ", productImage='" + productImage + '\'' +
                ", nickName='" + nickName + '\'' +
                ", productKey='" + productKey + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", iotSpaceId='" + iotSpaceId + '\'' +
                ", productName='" + productName + '\'' +
                ", categoryKey='" + categoryKey + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(iotId);
        out.writeString(rootSpaceId);
        out.writeString(productImage);
        out.writeString(nickName);
        out.writeString(productKey);
        out.writeString(deviceName);
        out.writeString(iotSpaceId);
        out.writeString(productName);
        out.writeString(categoryKey);
        out.writeInt(status);
        out.writeTypedList(attributes);
    }

    public static final Creator<ATDeviceBean> CREATOR = new Creator<ATDeviceBean>() {
        @Override
        public ATDeviceBean[] newArray(int size) {
            return new ATDeviceBean[size];
        }

        @Override
        public ATDeviceBean createFromParcel(Parcel in) {
            return new ATDeviceBean(in);
        }
    };

    public ATDeviceBean() {

    }

    public ATDeviceBean(Parcel in) {
        iotId = in.readString();
        rootSpaceId = in.readString();
        productImage = in.readString();
        nickName = in.readString();
        productKey = in.readString();
        deviceName = in.readString();
        iotSpaceId = in.readString();
        productName = in.readString();
        categoryKey = in.readString();
        status = in.readInt();
        if(attributes ==null){
            attributes = new ArrayList<>();
        }
        in.readTypedList(attributes, ATDeviceTslDataType.CREATOR);
    }
}
