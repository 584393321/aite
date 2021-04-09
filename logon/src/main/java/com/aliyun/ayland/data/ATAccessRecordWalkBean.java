package com.aliyun.ayland.data;

public class ATAccessRecordWalkBean {
    /**
     * eventId : 0BC15F116186531D72CA92C5658FC8D7
     * scopeId : EDEB4BD423E142188CC2E0B2754FEAC8
     * mediaType : face
     * userName : 测试账
     * userId : 1140807701130522624
     * deviceName : 太川人脸门口机2号
     * accessType : 1
     * itemId : 525791
     * iotId : DlBTAoMYd2jqkqWxUxs1000100
     * createTime : 2019-09-29 12:00:10
     * eventTime : 1569729610000
     * id : 277
     * userType : OPEN
     */

    private String eventId;
    private String scopeId;
    private String mediaType;
    private String userName;
    private String userId;
    private String deviceName;
    private int accessType;
    private int itemId;
    private String iotId;
    private String createTime;
    private long eventTime;
    private int id;
    private String userType;

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getScopeId() {
        return scopeId;
    }

    public void setScopeId(String scopeId) {
        this.scopeId = scopeId;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getAccessType() {
        return accessType;
    }

    public void setAccessType(int accessType) {
        this.accessType = accessType;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getIotId() {
        return iotId;
    }

    public void setIotId(String iotId) {
        this.iotId = iotId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public long getEventTime() {
        return eventTime;
    }

    public void setEventTime(long eventTime) {
        this.eventTime = eventTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}