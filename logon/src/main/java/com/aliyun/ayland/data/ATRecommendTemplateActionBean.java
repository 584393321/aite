package com.aliyun.ayland.data;

import java.util.List;

public class ATRecommendTemplateActionBean {
    /**
     * actionKey : FAU
     * actionName : 新风开启
     * actionSort : 0
     * actionType : 1
     * deviceDtoList : [{"attributes":[{"attribute":"PowerSwitch","value":1},{"attributes":"WindSpeed","value":4}],"categoryKey":"FAU","deviceName":"1079a51e004b1200_29","iotId":"c98sjPEuZUo7KgUFt8If000100","iotSpaceId":"5e83ec9d96d046efaa71e16ddd204612","productImage":"http://iotx-paas-admin.oss-cn-shanghai.aliyuncs.com/publish/image/1559631228480.png","productKey":"a1gIz51dyRT","productName":"ATTE","status":3}]
     * templateId : 2
     */

    private String actionKey;
    private String actionName;
    private int actionSort;
    private int actionType;
    private int templateId;
    private List<ATBrightnessLightBean> deviceDtoList;

    public String getActionKey() {
        return actionKey;
    }

    public void setActionKey(String actionKey) {
        this.actionKey = actionKey;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public int getActionSort() {
        return actionSort;
    }

    public void setActionSort(int actionSort) {
        this.actionSort = actionSort;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public int getTemplateId() {
        return templateId;
    }

    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }

    public List<ATBrightnessLightBean> getDeviceDtoList() {
        return deviceDtoList;
    }

    public void setDeviceDtoList(List<ATBrightnessLightBean> deviceDtoList) {
        this.deviceDtoList = deviceDtoList;
    }
}
