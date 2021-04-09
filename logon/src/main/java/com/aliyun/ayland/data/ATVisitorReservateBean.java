package com.aliyun.ayland.data;

public class ATVisitorReservateBean {
    /**
     * reservationStartTime : 2019-08-22 09:40
     * visitorName : 陈
     * actualStartTime :
     * carNumber :
     * createTime : 2019-08-22 09:40
     * actualEndTime :
     * reservationEndTime : 2019-08-23 09:40
     * visitorTel : 13333333333
     * id : 83
     * visitorStatus : -1
     * hasCar : -1
     * intermediary : -1
     */

    private String reservationStartTime;
    private String visitorName;
    private String actualStartTime;
    private String createTime;
    private String actualEndTime;
    private String reservationEndTime;
    private String visitorTel;
    private String id;
    private String carNumber;
    private int visitorStatus;
    private int hasCar;
    private int intermediary;

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getReservationStartTime() {
        return reservationStartTime;
    }

    public void setReservationStartTime(String reservationStartTime) {
        this.reservationStartTime = reservationStartTime;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public String getActualStartTime() {
        return actualStartTime;
    }

    public void setActualStartTime(String actualStartTime) {
        this.actualStartTime = actualStartTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getActualEndTime() {
        return actualEndTime;
    }

    public void setActualEndTime(String actualEndTime) {
        this.actualEndTime = actualEndTime;
    }

    public String getReservationEndTime() {
        return reservationEndTime;
    }

    public void setReservationEndTime(String reservationEndTime) {
        this.reservationEndTime = reservationEndTime;
    }

    public String getVisitorTel() {
        return visitorTel;
    }

    public void setVisitorTel(String visitorTel) {
        this.visitorTel = visitorTel;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getVisitorStatus() {
        return visitorStatus;
    }

    public void setVisitorStatus(int visitorStatus) {
        this.visitorStatus = visitorStatus;
    }

    public int getHasCar() {
        return hasCar;
    }

    public void setHasCar(int hasCar) {
        this.hasCar = hasCar;
    }

    public int getIntermediary() {
        return intermediary;
    }

    public void setIntermediary(int intermediary) {
        this.intermediary = intermediary;
    }
}
