package com.aliyun.ayland.data;

import java.util.List;

public class ATSceneActionBean {

    /**
     * actionIcon : 我是图片
     * createTime : 1570861027000
     * recommendSceneId : 497
     * deviceList : [{"iotId":"yCfMC0DhR96VcJn2LQyG000100","createTime":1571985143000,"deviceIcon":"http://alisaas.atsmartlife.comnull","id":500,"deviceName":"ATTE空调控制器","deviceCategoryKey":"AirConditioning","recommendSceneUserId":536,"deviceStatus":1}]
     * createPerson : 1140807701130522624
     * updateTime : 1570861030000
     * id : 498
     * updatePerson : 1140807701130522624
     * actionName : 开空调
     */

    private String actionIcon;
    private long createTime;
    private int recommendSceneId;
    private String createPerson;
    private long updateTime;
    private int id;
    private String updatePerson;
    private String actionName;
    private List<DeviceListBean> deviceList;

    public String getActionIcon() {
        return actionIcon;
    }

    public void setActionIcon(String actionIcon) {
        this.actionIcon = actionIcon;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getRecommendSceneId() {
        return recommendSceneId;
    }

    public void setRecommendSceneId(int recommendSceneId) {
        this.recommendSceneId = recommendSceneId;
    }

    public String getCreatePerson() {
        return createPerson;
    }

    public void setCreatePerson(String createPerson) {
        this.createPerson = createPerson;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUpdatePerson() {
        return updatePerson;
    }

    public void setUpdatePerson(String updatePerson) {
        this.updatePerson = updatePerson;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public List<DeviceListBean> getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(List<DeviceListBean> deviceList) {
        this.deviceList = deviceList;
    }

    public static class DeviceListBean {
         /**
         * iotId : yCfMC0DhR96VcJn2LQyG000100
         * createTime : 1571985143000
         * deviceIcon : http://alisaas.atsmartlife.comnull
         * id : 500
         * deviceName : ATTE空调控制器
         * deviceCategoryKey : AirConditioning
         * recommendSceneUserId : 536
         * deviceStatus : 1
         */

        private String iotId;
        private long createTime;
        private String deviceIcon;
        private int id;
        private String deviceName;
        private String deviceCategoryKey;
        private int recommendSceneUserId;
        private int deviceStatus;

        public String getIotId() {
            return iotId;
        }

        public void setIotId(String iotId) {
            this.iotId = iotId;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public String getDeviceIcon() {
            return deviceIcon;
        }

        public void setDeviceIcon(String deviceIcon) {
            this.deviceIcon = deviceIcon;
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

        public String getDeviceCategoryKey() {
            return deviceCategoryKey;
        }

        public void setDeviceCategoryKey(String deviceCategoryKey) {
            this.deviceCategoryKey = deviceCategoryKey;
        }

        public int getRecommendSceneUserId() {
            return recommendSceneUserId;
        }

        public void setRecommendSceneUserId(int recommendSceneUserId) {
            this.recommendSceneUserId = recommendSceneUserId;
        }

        public int getDeviceStatus() {
            return deviceStatus;
        }

        public void setDeviceStatus(int deviceStatus) {
            this.deviceStatus = deviceStatus;
        }
    }
}
