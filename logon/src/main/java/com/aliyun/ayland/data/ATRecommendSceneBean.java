package com.aliyun.ayland.data;

public class ATRecommendSceneBean {

    /**
     * recommendSceneIcon : http://alisaas.atsmartlife.com我是图片
     * recommendSceneId : 493
     * recommendSceneName : 天气为晴时
     * recommendSceneStatus : 0
     * recommendSceneType : 1
     */

    private String recommendSceneIcon;
    private String recommendSceneId;
    private String recommendSceneName;
    private int recommendSceneStatus;
    private String recommendSceneType;

    public String getRecommendSceneIcon() {
        return recommendSceneIcon;
    }

    public void setRecommendSceneIcon(String recommendSceneIcon) {
        this.recommendSceneIcon = recommendSceneIcon;
    }

    public String getRecommendSceneId() {
        return recommendSceneId;
    }

    public void setRecommendSceneId(String recommendSceneId) {
        this.recommendSceneId = recommendSceneId;
    }

    public String getRecommendSceneName() {
        return recommendSceneName;
    }

    public void setRecommendSceneName(String recommendSceneName) {
        this.recommendSceneName = recommendSceneName;
    }

    public int getRecommendSceneStatus() {
        return recommendSceneStatus;
    }

    public void setRecommendSceneStatus(int recommendSceneStatus) {
        this.recommendSceneStatus = recommendSceneStatus;
    }

    public String getRecommendSceneType() {
        return recommendSceneType;
    }

    public void setRecommendSceneType(String recommendSceneType) {
        this.recommendSceneType = recommendSceneType;
    }
}
