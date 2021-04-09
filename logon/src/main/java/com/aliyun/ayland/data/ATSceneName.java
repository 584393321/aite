package com.aliyun.ayland.data;


import android.support.annotation.NonNull;

/**
 * @author guikong on 18/4/12.
 */
public class ATSceneName implements Comparable<ATSceneName>{
    private String name;
    private String content;
    private String params;
    private String uri;
    private String dataType;
    private long currentTime;

    public ATSceneName() {
        this.currentTime = System.currentTimeMillis();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @Override
    public String toString() {
        return "ATSceneName{" +
                "name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", params='" + params + '\'' +
                ", uri='" + uri + '\'' +
                ", dataType='" + dataType + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NonNull ATSceneName ATSceneName) {
        return (int)(this.currentTime - ATSceneName.currentTime);
    }
}