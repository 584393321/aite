package com.aliyun.ayland.data;

import java.util.List;

public class ATFamilySensorsBean {
    /**
     * hasTypeCount : 4
     * code : 200
     * sensors : [{"name":"入侵报警","status":1},{"name":"燃气报警","status":1},{"name":"水浸报警","status":1},{"name":"紧急报警","status":1}]
     * totalTypeCount : 5
     * message : success
     * totalDeviceCount : 4
     * totalCameraCount : 0
     * onlineCameraCount : 0
     */

    private String hasTypeCount;
    private String code;
    private String totalTypeCount;
    private String message;
    private String totalDeviceCount;
    private String totalCameraCount;
    private String onlineCameraCount;
    private List<SensorsBean> sensors;

    public String getHasTypeCount() {
        return hasTypeCount;
    }

    public void setHasTypeCount(String hasTypeCount) {
        this.hasTypeCount = hasTypeCount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTotalTypeCount() {
        return totalTypeCount;
    }

    public void setTotalTypeCount(String totalTypeCount) {
        this.totalTypeCount = totalTypeCount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTotalDeviceCount() {
        return totalDeviceCount;
    }

    public void setTotalDeviceCount(String totalDeviceCount) {
        this.totalDeviceCount = totalDeviceCount;
    }

    public String getTotalCameraCount() {
        return totalCameraCount;
    }

    public void setTotalCameraCount(String totalCameraCount) {
        this.totalCameraCount = totalCameraCount;
    }

    public String getOnlineCameraCount() {
        return onlineCameraCount;
    }

    public void setOnlineCameraCount(String onlineCameraCount) {
        this.onlineCameraCount = onlineCameraCount;
    }

    public List<SensorsBean> getSensors() {
        return sensors;
    }

    public void setSensors(List<SensorsBean> sensors) {
        this.sensors = sensors;
    }

    public static class SensorsBean {
        /**
         * name : 入侵报警
         * status : 1
         */

        private String name;
        private int status;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
