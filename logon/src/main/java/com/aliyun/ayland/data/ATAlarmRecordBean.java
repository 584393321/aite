package com.aliyun.ayland.data;

public class ATAlarmRecordBean {
    /**
     * deviceType : Camera
     * eventTime : 2019-09-16 14:33:30
     * description : 摄像头报警开启
     * id : 348
     * deviceName : HI3518EV200_T1_200W_PTZ
     * status : 0
     */

    private String deviceType;
    private String eventTime;
    private String description;
    private int id;
    private String deviceName;
    private int status;

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}