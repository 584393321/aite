package com.aliyun.ayland.data;


/**
 * @author sinyuk
 * @date 2018/6/25
 */
public class ATRoomBean {
    private String room_class_type;
    private String room_name;

    public String getRoom_class_type() {
        return room_class_type;
    }

    public void setRoom_class_type(String room_class_type) {
        this.room_class_type = room_class_type;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    @Override
    public String toString() {
        return "RoomBean{" +
                "room_class_type='" + room_class_type + '\'' +
                ", room_name='" + room_name + '\'' +
                '}';
    }
}
