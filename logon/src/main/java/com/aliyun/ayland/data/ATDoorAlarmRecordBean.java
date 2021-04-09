package com.aliyun.ayland.data;

public class ATDoorAlarmRecordBean {

    /**
     * image : https://smarthome.cifi.com.cn/pic/deviceLinkage/home_ld_ico_SmartDoor.png
     * iotId : NabvJ0ijgEKBLn3D1ocv000100
     * eventTime : 2020-07-09 16:19:02
     * description : 5次密码错误
     * id : 459
     * deviceName : ATTE智能门锁
     */

    private String image;
    private String iotId;
    private String eventTime;
    private String description;
    private int id;
    private String deviceName;

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
}
