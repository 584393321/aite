package com.aliyun.ayland.data;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author sinyuk
 * @date 2018/6/25
 */
public class ATRoomBean1 implements Parcelable {
    private String iotSpaceId;
    public String name;
    private String orderCode;
    private int roomCode;
    public String type;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRoomCode() {
        return roomCode;
    }

    public void setRoomCode(int roomCode) {
        this.roomCode = roomCode;
    }

    @Override
    public String toString() {
        return "ATRoomBean1{" +
                "iotSpaceId='" + iotSpaceId + '\'' +
                ", name='" + name + '\'' +
                ", orderCode='" + orderCode + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(iotSpaceId);
        out.writeString(name);
        out.writeString(orderCode);
        out.writeString(type);
        out.writeInt(roomCode);
    }

    public static final Creator<ATRoomBean1> CREATOR = new Creator<ATRoomBean1>() {
        @Override
        public ATRoomBean1[] newArray(int size) {
            return new ATRoomBean1[size];
        }

        @Override
        public ATRoomBean1 createFromParcel(Parcel in) {
            return new ATRoomBean1(in);
        }
    };

    public ATRoomBean1() {

    }

    public ATRoomBean1(Parcel in) {
        iotSpaceId = in.readString();
        name = in.readString();
        orderCode = in.readString();
        type = in.readString();
        roomCode = in.readInt();
    }
}