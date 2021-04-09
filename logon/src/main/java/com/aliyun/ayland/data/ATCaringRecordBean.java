package com.aliyun.ayland.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ATCaringRecordBean implements Parcelable {
//    {
//        "personName":"ceshi01lin",
//            "address":"web端创建小区1",
//            "eventTime":"2019-11-28 10:53:26",
//            "id":18297,
//            "personType":1
//    }
    private String personName;
    private String address;
    private String eventTime;
    private String id;
    private String iotId;
    private String findOutAloneRecord;
    private String villageName;
    private String deviceName;
    private String picUrl;
    private int personType;

    public String getFindOutAloneRecord() {
        return findOutAloneRecord;
    }

    public void setFindOutAloneRecord(String findOutAloneRecord) {
        this.findOutAloneRecord = findOutAloneRecord;
    }

    public String getIotId() {
        return iotId;
    }

    public void setIotId(String iotId) {
        this.iotId = iotId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPersonType() {
        return personType;
    }

    public void setPersonType(int personType) {
        this.personType = personType;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(personName);
        out.writeString(address);
        out.writeString(eventTime);
        out.writeString(id);
        out.writeString(picUrl);
        out.writeString(iotId);
        out.writeString(findOutAloneRecord);
        out.writeString(villageName);
        out.writeString(deviceName);
        out.writeInt(personType);
    }

    public static final Creator<ATCaringRecordBean> CREATOR = new Creator<ATCaringRecordBean>() {
        @Override
        public ATCaringRecordBean[] newArray(int size) {
            return new ATCaringRecordBean[size];
        }

        @Override
        public ATCaringRecordBean createFromParcel(Parcel in) {
            return new ATCaringRecordBean(in);
        }
    };

    public ATCaringRecordBean() {

    }

    public ATCaringRecordBean(Parcel in) {
        personName = in.readString();
        address = in.readString();
        eventTime = in.readString();
        id = in.readString();
        picUrl = in.readString();
        iotId = in.readString();
        findOutAloneRecord = in.readString();
        villageName = in.readString();
        deviceName = in.readString();
        personType = in.readInt();
    }
}