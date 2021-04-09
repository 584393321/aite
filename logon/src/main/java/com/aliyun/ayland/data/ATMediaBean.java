package com.aliyun.ayland.data;

public class ATMediaBean {
    private String mediaName;
    private String path;
    private String type;

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ATMediaBean{" +
                "mediaName='" + mediaName + '\'' +
                ", path='" + path + '\'' +
                ", path='" + type + '\'' +
                '}';
    }
}
