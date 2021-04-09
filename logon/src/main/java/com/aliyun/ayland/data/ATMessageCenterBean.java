package com.aliyun.ayland.data;

public class ATMessageCenterBean {
    private String icon;
    private String device;
    private String status;
    private String time;

    public ATMessageCenterBean(String device, String status, String time, String icon) {
        this.device = device;
        this.status = status;
        this.time = time;
        this.icon = icon;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
