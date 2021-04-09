package com.aliyun.ayland.data;

import org.json.JSONArray;
import org.json.JSONObject;

public class ATSceneDetailBean {
    private String icon;
    private String name;
    private JSONArray triggers;
    private JSONArray condintios;
    private JSONArray actions;
    private String status;
    private String description;
    private boolean enable;
    private String sceneId;
    private JSONObject spaceInfo;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JSONArray getTriggers() {
        return triggers;
    }

    public void setTriggers(JSONArray triggers) {
        this.triggers = triggers;
    }

    public JSONArray getCondintios() {
        return condintios;
    }

    public void setCondintios(JSONArray condintios) {
        this.condintios = condintios;
    }

    public JSONArray getActions() {
        return actions;
    }

    public void setActions(JSONArray actions) {
        this.actions = actions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public JSONObject getSpaceInfo() {
        return spaceInfo;
    }

    public void setSpaceInfo(JSONObject spaceInfo) {
        this.spaceInfo = spaceInfo;
    }
}
