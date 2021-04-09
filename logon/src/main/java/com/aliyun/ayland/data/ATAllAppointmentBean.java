package com.aliyun.ayland.data;

import android.os.Parcel;
import android.os.Parcelable;

public class ATAllAppointmentBean implements Parcelable{
    /**
     * appointmentTime : 16:30~17:00
     * day : 2020-03-31
     * projectName : 亲子盒子
     * typeName : 亲子
     */

    private String appointmentTime;
    private String day;
    private String projectName;
    private String typeName;

    public String getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(appointmentTime);
        out.writeString(day);
        out.writeString(projectName);
        out.writeString(typeName);
    }

    public static final Creator<ATAllAppointmentBean> CREATOR = new Creator<ATAllAppointmentBean>() {
        @Override
        public ATAllAppointmentBean[] newArray(int size) {
            return new ATAllAppointmentBean[size];
        }

        @Override
        public ATAllAppointmentBean createFromParcel(Parcel in) {
            return new ATAllAppointmentBean(in);
        }
    };

    private ATAllAppointmentBean(Parcel in) {
        appointmentTime = in.readString();
        day = in.readString();
        projectName = in.readString();
        typeName = in.readString();
    }
}
