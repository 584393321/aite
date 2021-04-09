package com.aliyun.ayland.data;

// "imageUrl": "http://iotx-paas-admin.oss-cn-shanghai.aliyuncs.com/publish/image/1559631270079.png",
//         "categoryKey": "Airbox",                    //类别key
//         "superId": 54,                            //上级id
//         "state": 1,
//         "categoryName": "空气盒子",
//         "categoryId": 62                        //本身的id，查下级子菜单用
public class ATDiscoveryDeviceFirstLevelBean {
    private String imageUrl;
    private String categoryKey;
    private String categoryName;
    private int superId;
    private int state;
    private int categoryId;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

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

    public int getSuperId() {
        return superId;
    }

    public void setSuperId(int superId) {
        this.superId = superId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "ATDiscoveryDeviceFirstLevelBean{" +
                "imageUrl='" + imageUrl + '\'' +
                ", categoryKey='" + categoryKey + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", superId='" + superId + '\'' +
                ", state='" + state + '\'' +
                ", categoryId='" + categoryId + '\'' +
                '}';
    }
}
