package com.aliyun.ayland.data;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author sinyuk
 * @date 2018/6/25
 */
public class ATTcaDeviceBean {
    private String image;
    private String iotId;
    private String productKey;
    private String deviceName;
    private String status;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIotId() {
        return iotId;
    }

    public void setIotId(String iotId) {
        this.iotId = iotId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ATTcaDeviceBean{" +
                "image='" + image + '\'' +
                ", iotId='" + iotId + '\'' +
                ", productKey='" + productKey + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}