package com.aliyun.ayland.data;


import java.util.ArrayList;

/**
 * @author guikong on 18/4/12.
 */
public class ATDeviceTcaList {
    /**
     * categoryKey : IRDetector
     * categoryName : 红外探测器
     * imageUrl : http://iotx-paas-admin.oss-cn-shanghai.aliyuncs.com/publish/image/1559631717673.png
     * condition : [{"deviceName":"832400000053","iotId":"P7v3hyJF1Nc8c0MD0TWF000100","productImage":"http://iotx-paas-admin.oss-cn-shanghai.aliyuncs.com/publish/image/1559631844419.png","productKey":"a1qKhYZoZso","productName":"艾特人体红外探测器"},{"deviceName":"LIU_TEST_002","iotId":"xtjZSt7bJRFxpaCrF1ml0010ecfd00","productImage":"http://iotx-paas-admin.oss-cn-shanghai.aliyuncs.com/publish/image/1526474167321.png","productKey":"a1FKWjbnVYC","productName":"A6"}]
     * action : [{"deviceName":"832400000053","iotId":"P7v3hyJF1Nc8c0MD0TWF000100","productImage":"http://iotx-paas-admin.oss-cn-shanghai.aliyuncs.com/publish/image/1559631844419.png","productKey":"a1qKhYZoZso","productName":"艾特人体红外探测器"},{"deviceName":"LIU_TEST_002","iotId":"xtjZSt7bJRFxpaCrF1ml0010ecfd00","productImage":"http://iotx-paas-admin.oss-cn-shanghai.aliyuncs.com/publish/image/1526474167321.png","productKey":"a1FKWjbnVYC","productName":"A6"}]
     * trigger : [{"deviceName":"832400000053","iotId":"P7v3hyJF1Nc8c0MD0TWF000100","productImage":"http://iotx-paas-admin.oss-cn-shanghai.aliyuncs.com/publish/image/1559631844419.png","productKey":"a1qKhYZoZso","productName":"艾特人体红外探测器"},{"deviceName":"LIU_TEST_002","iotId":"xtjZSt7bJRFxpaCrF1ml0010ecfd00","productImage":"http://iotx-paas-admin.oss-cn-shanghai.aliyuncs.com/publish/image/1526474167321.png","productKey":"a1FKWjbnVYC","productName":"A6"}]
     */

    private String categoryKey;
    private String categoryName;
    private String imageUrl;
    private ArrayList<ATDeviceBean> condition;
    private ArrayList<ATDeviceBean> action;
    private ArrayList<ATDeviceBean> trigger;

    public String getCategoryKey() {
        return categoryKey;
    }

    public void setCategoryKey(String categoryKey) {
        this.categoryKey = categoryKey;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ArrayList<ATDeviceBean> getCondition() {
        return condition;
    }

    public void setCondition(ArrayList<ATDeviceBean> condition) {
        this.condition = condition;
    }

    public ArrayList<ATDeviceBean> getAction() {
        return action;
    }

    public void setAction(ArrayList<ATDeviceBean> action) {
        this.action = action;
    }

    public ArrayList<ATDeviceBean> getTrigger() {
        return trigger;
    }

    public void setTrigger(ArrayList<ATDeviceBean> trigger) {
        this.trigger = trigger;
    }
}
