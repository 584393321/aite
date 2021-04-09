package com.aliyun.ayland.data;

public class ATLinkageLogBean {

    /**
     * traceId : f1c121a0-113b-4d28-a5bd-5df8e96ef9de
     * messageType : SCENE_RESULT
     * createTime : 2019-08-08 09:00
     * openId : 1140807701130522624
     * success : 成功
     * sceneName : 按钮
     * sceneId : 66802acec5ab4b7db45bb804c301fdeb
     * id : 30
     * gmtCreate : 1565226030332
     * errors : []
     */

    private String traceId;
    private String messageType;
    private String createTime;
    private String openId;
    private String success;
    private String sceneName;
    private String sceneId;
    private int id;
    private long gmtCreate;
    private String errors;

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "ATLinkageLogBean{" +
                "traceId='" + traceId + '\'' +
                ", messageType='" + messageType + '\'' +
                ", createTime='" + createTime + '\'' +
                ", openId='" + openId + '\'' +
                ", success='" + success + '\'' +
                ", sceneName='" + sceneName + '\'' +
                ", sceneId='" + sceneId + '\'' +
                ", id=" + id +
                ", gmtCreate=" + gmtCreate +
                ", errors='" + errors + '\'' +
                '}';
    }
}
