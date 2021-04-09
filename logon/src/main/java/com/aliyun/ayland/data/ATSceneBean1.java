package com.aliyun.ayland.data;

public class ATSceneBean1 {
    private String sceneIcon;
    private String sceneId;
    private String sceneName;
    private boolean isAdd;

    public String getSceneIcon() {
        return sceneIcon;
    }

    public void setSceneIcon(String sceneIcon) {
        this.sceneIcon = sceneIcon;
    }

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public boolean isAdd() {
        return isAdd;
    }

    public void setAdd(boolean add) {
        isAdd = add;
    }

    @Override
    public String toString() {
        return "ATSceneBean1{" +
                "sceneIcon='" + sceneIcon + '\'' +
                ", sceneId='" + sceneId + '\'' +
                ", sceneName='" + sceneName + '\'' +
                '}';
    }
}
