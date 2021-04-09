package com.aliyun.ayland.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class ATDeviceManageBean {
    /**
     * attributes : [{"attribute":"CountDown","value":"{}"},{"attribute":"CountDownList","value":"{}"},{"attribute":"CurrentHumidity","value":"55"},{"attribute":"CurrentTemperature","value":"27"},{"attribute":"LocalTimer","value":"[]"},{"attribute":"PM25","value":"8"},{"attribute":"TVOC","value":"1"}]
     * deviceName : 832400000064
     * iotId : vkQsA6vo9yCs09GVyFql000100
     * productImage : http://iotx-paas-admin.oss-cn-shanghai.aliyuncs.com/publish/image/1559631270079.png
     * productKey : a1Ti5A4zDdn
     * productName : 艾特空气盒子
     * sharedUsers : [{"shareTime":"2019-09-16 16:18:24","username":"13823688480"}]
     * status : 1
     */

    private String deviceName;
    private String iotId;
    private String productImage;
    private String productKey;
    private String productName;
    private int status;
    private List<AttributesBean> attributes;
    private List<SharedUsersBean> sharedUsers;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getIotId() {
        return iotId;
    }

    public void setIotId(String iotId) {
        this.iotId = iotId;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<AttributesBean> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<AttributesBean> attributes) {
        this.attributes = attributes;
    }

    public List<SharedUsersBean> getSharedUsers() {
        return sharedUsers;
    }

    public void setSharedUsers(List<SharedUsersBean> sharedUsers) {
        this.sharedUsers = sharedUsers;
    }

    public static class AttributesBean {
        /**
         * attribute : CountDown
         * value : {}
         */

        private String attribute;
        private String value;

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
    }

    public static class SharedUsersBean implements Parcelable {
        /**
         * shareTime : 2019-09-16 16:18:24
         * username : 13823688480
         */

        private String shareTime;
        private String username;

        public String getShareTime() {
            return shareTime;
        }

        public void setShareTime(String shareTime) {
            this.shareTime = shareTime;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel out, int i) {
            out.writeString(shareTime);
            out.writeString(username);
        }

        public static final Creator<SharedUsersBean> CREATOR = new Creator<SharedUsersBean>() {
            @Override
            public SharedUsersBean[] newArray(int size) {
                return new SharedUsersBean[size];
            }

            @Override
            public SharedUsersBean createFromParcel(Parcel in) {
                return new SharedUsersBean(in);
            }
        };

        public SharedUsersBean() {

        }

        private SharedUsersBean(Parcel in) {
            shareTime = in.readString();
            username = in.readString();
        }
    }
}