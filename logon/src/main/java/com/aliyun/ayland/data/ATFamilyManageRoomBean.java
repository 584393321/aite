package com.aliyun.ayland.data;

import android.os.Parcel;
import android.os.Parcelable;

public class ATFamilyManageRoomBean {
    /**
     * iotSpaceId : 4341de8490424947935ebd4924200e3b
     * name : 阳台
     * orderCode : 4
     * roomCode : 100167
     * type : yangtai
     */
    private String iotSpaceId;
    private String name;
    private String orderCode;
    private String roomCode;
    private String type;
    private int canSee;

    public int getCanSee() {
        return canSee;
    }

    public void setCanSee(int canSee) {
        this.canSee = canSee;
    }

    public String getIotSpaceId() {
        return iotSpaceId;
    }

    public void setIotSpaceId(String iotSpaceId) {
        this.iotSpaceId = iotSpaceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(String roomCode) {
        this.roomCode = roomCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}