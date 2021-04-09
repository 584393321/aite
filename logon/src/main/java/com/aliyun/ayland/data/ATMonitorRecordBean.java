package com.aliyun.ayland.data;

public class ATMonitorRecordBean {

    /**
     * duration : 5秒
     * date : 2019-10-21
     * createTime : 18:10:18
     * endTime : 18:10:23
     * id : 20
     * deviceName : 小眯眼C8X
     */

    private String duration;
    private String date;
    private String createTime;
    private String endTime;
    private int id;
    private String deviceName;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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
