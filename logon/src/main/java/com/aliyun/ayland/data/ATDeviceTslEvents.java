package com.aliyun.ayland.data;

import com.alibaba.fastjson.JSONArray;

/**
 * @author guikong on 18/4/12.
 */
public class ATDeviceTslEvents {
    private JSONArray outputData;
    private String identifier;
    private String method;
    private String name;
    private String type;
    private String required;
    private String desc;

    public JSONArray getOutputData() {
        return outputData;
    }

    public void setOutputData(JSONArray outputData) {
        this.outputData = outputData;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        return "ATDeviceTslEvents{" +
                "outputData='" + outputData + '\'' +
                ", identifier='" + identifier + '\'' +
                ", method='" + method + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", required='" + required + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
