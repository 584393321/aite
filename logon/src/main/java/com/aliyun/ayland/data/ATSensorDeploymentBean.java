package com.aliyun.ayland.data;

import java.util.List;

public class ATSensorDeploymentBean {

    /**
     * data : [{"categoryKey":"IRDetector","categoryName":"红外探测器","deployType":1,"deviceName":"f68aba1a004b1200_160","iotId":"bgQFTkAEUN3UvXnyZAVa000100","iotSpaceId":"dca173b6bf1e4e48b4e6d5e245242bdd","productImage":"http://iotx-paas-admin.oss-cn-shanghai.aliyuncs.com/publish/image/1559631844419.png","productKey":"a1qKhYZoZso","productName":"ATTE人体红外探测器","spaceName":"厨房"}]
     * size : 1
     * name : 入侵报警
     */

    private int size;
    private String name;
    private List<DataBean> data;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * categoryKey : IRDetector
         * categoryName : 红外探测器
         * deployType : 1
         * deviceName : f68aba1a004b1200_160
         * iotId : bgQFTkAEUN3UvXnyZAVa000100
         * iotSpaceId : dca173b6bf1e4e48b4e6d5e245242bdd
         * productImage : http://iotx-paas-admin.oss-cn-shanghai.aliyuncs.com/publish/image/1559631844419.png
         * productKey : a1qKhYZoZso
         * productName : ATTE人体红外探测器
         * spaceName : 厨房
         */

        private String categoryKey;
        private String categoryName;
        private int deployType;
        private String deviceName;
        private String iotId;
        private String iotSpaceId;
        private String productImage;
        private String productKey;
        private String productName;
        private String spaceName;

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

        public int getDeployType() {
            return deployType;
        }

        public void setDeployType(int deployType) {
            this.deployType = deployType;
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
    }
}
