package com.aliyun.ayland.data;

import android.os.Parcel;
import android.os.Parcelable;

public class ATApplicationBean implements Parcelable {
    private int appSort;
    private String applicationIcon;
    private int applicationId;
    private String applicationIdentification;
    private String applicationName;
    private String personCode;

    public int getAppSort() {
        return appSort;
    }

    public void setAppSort(int appSort) {
        this.appSort = appSort;
    }

    public String getApplicationIcon() {
        return applicationIcon;
    }

    public void setApplicationIcon(String applicationIcon) {
        this.applicationIcon = applicationIcon;
    }

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationIdentification() {
        return applicationIdentification;
    }

    public void setApplicationIdentification(String applicationIdentification) {
        this.applicationIdentification = applicationIdentification;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getPersonCode() {
        return personCode;
    }

    public void setPersonCode(String personCode) {
        this.personCode = personCode;
    }

    @Override
    public String toString() {
        return "ATApplicationBean{" +
                "appSort='" + appSort + '\'' +
                ", applicationIcon='" + applicationIcon + '\'' +
                ", applicationId='" + applicationId + '\'' +
                ", applicationIdentification='" + applicationIdentification + '\'' +
                ", applicationName='" + applicationName + '\'' +
                ", personCode='" + personCode + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeInt(appSort);
        out.writeString(applicationIcon);
        out.writeInt(applicationId);
        out.writeString(applicationIdentification);
        out.writeString(applicationName);
        out.writeString(personCode);
    }

    public static final Creator<ATApplicationBean> CREATOR = new Creator<ATApplicationBean>() {
        @Override
        public ATApplicationBean[] newArray(int size) {
            return new ATApplicationBean[size];
        }

        @Override
        public ATApplicationBean createFromParcel(Parcel in) {
            return new ATApplicationBean(in);
        }
    };

    public ATApplicationBean() {

    }

    public ATApplicationBean(Parcel in) {
        appSort = in.readInt();
        applicationIcon = in.readString();
        applicationId = in.readInt();
        applicationIdentification = in.readString();
        applicationName = in.readString();
        personCode = in.readString();
    }
}