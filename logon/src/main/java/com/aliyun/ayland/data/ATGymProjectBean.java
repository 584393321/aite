package com.aliyun.ayland.data;

import com.alibaba.fastjson.JSONArray;

public class ATGymProjectBean {
    private String checkedIcon;
    private String unCheckedIcon;
    private String price;
    private JSONArray time;
    private String projectName;
    private String projectId;

    public String getCheckedIcon() {
        return checkedIcon;
    }

    public void setCheckedIcon(String checkedIcon) {
        this.checkedIcon = checkedIcon;
    }

    public String getUnCheckedIcon() {
        return unCheckedIcon;
    }

    public void setUnCheckedIcon(String unCheckedIcon) {
        this.unCheckedIcon = unCheckedIcon;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public JSONArray getTime() {
        return time;
    }

    public void setTime(JSONArray time) {
        this.time = time;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    @Override
    public String toString() {
        return "ATGymProjectBean{" +
                "checkedIcon='" + checkedIcon + '\'' +
                ", unCheckedIcon='" + unCheckedIcon + '\'' +
                ", price=" + price +
                ", time='" + time + '\'' +
                ", projectName='" + projectName + '\'' +
                ", projectId='" + projectId + '\'' +
                '}';
    }
}