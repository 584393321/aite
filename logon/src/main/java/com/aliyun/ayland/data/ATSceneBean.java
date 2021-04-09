package com.aliyun.ayland.data;

import android.os.Parcel;
import android.os.Parcelable;

public class ATSceneBean implements Parcelable {
    private String icon;
    private String name;
    private String status;
    private String description;
    private String enable;
    private String sceneId;
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String isEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(icon);
        out.writeString(name);
        out.writeString(status);
        out.writeString(description);
        out.writeString(enable);
        out.writeString(sceneId);
    }

    public static final Creator<ATSceneBean> CREATOR = new Creator<ATSceneBean>() {
        @Override
        public ATSceneBean[] newArray(int size) {
            return new ATSceneBean[size];
        }

        @Override
        public ATSceneBean createFromParcel(Parcel in) {
            return new ATSceneBean(in);
        }
    };

    public ATSceneBean() {

    }

    public ATSceneBean(Parcel in) {
        icon = in.readString();
        name = in.readString();
        status = in.readString();
        description = in.readString();
        enable = in.readString();
        sceneId = in.readString();
    }
}
