package com.aliyun.ayland.data;

public class ATSFDeviceListBean {

    /**
     * attributesStr : [{"attribute":"CountDown","value":"{}"},{"attribute":"CountDownList","value":"{}"},{"attribute":"LocalTimer","value":"[]"},{"attribute":"PANID","value":"{\"SetPanid\":0,\"Tips\":0}"}]
     * categoryKey : GeneralGateway
     * deviceName : 80000000570b_test
     * iotId : dMRa12zcidPdML3FdFTa000000
     * iotSpaceId : cbb54c0f798e496cbacea89b972ecf52
     * myImage : /pic/deviceLinkage/home_ld_ico_GeneralGateway.png
     * productImage : http://iotx-paas-admin.oss-cn-shanghai.aliyuncs.com/publish/image/1559628665015.png
     * productKey : a1HUlYJhpwZ
     * productName : ATTE网关
     * spaceName : 613室
     * status : 1
     */

    private String attributesStr;
    private String categoryKey;
    private String deviceName;
    private String iotId;
    private String iotSpaceId;
    private String myImage;
    private String productImage;
    private String productKey;
    private String productName;
    private String spaceName;
    private int status;

    public String getAttributesStr() {
        return attributesStr;
    }

    public void setAttributesStr(String attributesStr) {
        this.attributesStr = attributesStr;
    }

    public String getCategoryKey() {
        return categoryKey;
    }

    public void setCategoryKey(String categoryKey) {
        this.categoryKey = categoryKey;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getIotId() {
        return iotId;
    }

    public void setIotId(String iotId) {
        this.iotId = iotId;
    }

    public String getIotSpaceId() {
        return iotSpaceId;
    }

    public void setIotSpaceId(String iotSpaceId) {
        this.iotSpaceId = iotSpaceId;
    }

    public String getMyImage() {
        return myImage;
    }

    public void setMyImage(String myImage) {
        this.myImage = myImage;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductKey() {
        return productKey;
    }

    public void setProductKey(String productKey) {
        this.productKey = productKey;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
