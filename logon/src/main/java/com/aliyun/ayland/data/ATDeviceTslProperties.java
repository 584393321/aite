package com.aliyun.ayland.data;

import com.alibaba.fastjson.JSONObject;

/**
 * @author guikong on 18/4/12.
 */
public class ATDeviceTslProperties {
    private String identifier;
    private JSONObject dataType;
    private String name;
    private String accessMode;
    private String required;
    private String desc;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public JSONObject getDataType() {
        return dataType;
    }

    public void setDataType(JSONObject dataType) {
        this.dataType = dataType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccessMode() {
        return accessMode;
    }

    public void setAccessMode(String accessMode) {
        this.accessMode = accessMode;
    }

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "ATDeviceTslProperties{" +
                "identifier='" + identifier + '\'' +
                ", dataType='" + dataType + '\'' +
                ", name='" + name + '\'' +
                ", accessMode='" + accessMode + '\'' +
                ", required='" + required + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
