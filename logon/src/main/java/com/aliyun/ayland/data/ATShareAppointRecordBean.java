package com.aliyun.ayland.data;

import java.util.List;

public class ATShareAppointRecordBean {
    /**
     * appointmentTime : ["16:00~16:30","16:30~17:00"]
     * SpaceName : 测试新增订单的盒子
     * price : 16.5
     * appointmentStatus : 0
     * projectName : ["web新增椭圆机","web测试新增订单","龙门架"]
     * appointmentOrderCode : 79
     * appointmentDay : 2021-02-20
     */

    private String spaceName;
    private double price;
    private int appointmentStatus;
    private int appointmentOrderCode;
    private String appointmentDay;
    private List<String> projectName;
    private List<String> appointmentTime;

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(int appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public int getAppointmentOrderCode() {
        return appointmentOrderCode;
    }

    public void setAppointmentOrderCode(int appointmentOrderCode) {
        this.appointmentOrderCode = appointmentOrderCode;
    }

    public String getAppointmentDay() {
        return appointmentDay;
    }

    public void setAppointmentDay(String appointmentDay) {
        this.appointmentDay = appointmentDay;
    }

    public List<String> getProjectName() {
        return projectName;
    }

    public void setProjectName(List<String> projectName) {
        this.projectName = projectName;
    }

    public List<String> getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(List<String> appointmentTime) {
        this.appointmentTime = appointmentTime;
    }
}