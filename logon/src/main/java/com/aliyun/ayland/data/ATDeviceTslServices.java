package com.aliyun.ayland.data;

import com.alibaba.fastjson.JSONArray;

/**
 * @author guikong on 18/4/12.
 */
public class ATDeviceTslServices {
    private JSONArray outputData;
    private String identifier;
    private JSONArray inputData;
    private String method;
    private String name;
    private String required;
    private String callType;
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

    public JSONArray getInputData() {
        return inputData;
    }

    public void setInputData(JSONArray inputData) {
        this.inputData = inputData;
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

    public String getRequired() {
        return required;
    }

    public void setRequired(String required) {
        this.required = required;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "ATDeviceTslServices{" +
                "outputData='" + outputData + '\'' +
                ", identifier='" + identifier + '\'' +
                ", inputData='" + inputData + '\'' +
                ", method='" + method + '\'' +
                ", name='" + name + '\'' +
                ", required='" + required + '\'' +
                ", callType='" + callType + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
