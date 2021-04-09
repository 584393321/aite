package com.aliyun.ayland.data;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.annotations.SerializedName;

public class ATWeatherBean {
    private String cnGreetings;
    private String enGreetings;
    private JSONObject windLevel;
    private String maximumTemperature;
    private String minimumTemperature;
    private JSONObject windDirection;
    private JSONObject weather;
    private String id;
    private String imgSort;
    private String imgUrl;
    private String jumpUrl;
    @SerializedName("PM2.5")
    private String _$PM25232; // FIXME check this code

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgSort() {
        return imgSort;
    }

    public void setImgSort(String imgSort) {
        this.imgSort = imgSort;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }

    public void setJumpUrl(String jumpUrl) {
        this.jumpUrl = jumpUrl;
    }

    public String getCnGreetings() {
        return cnGreetings;
    }

    public void setCnGreetings(String cnGreetings) {
        this.cnGreetings = cnGreetings;
    }

    public String getEnGreetings() {
        return enGreetings;
    }

    public void setEnGreetings(String enGreetings) {
        this.enGreetings = enGreetings;
    }

    public JSONObject getWindLevel() {
        return windLevel;
    }

    public void setWindLevel(JSONObject windLevel) {
        this.windLevel = windLevel;
    }

    public String getMaximumTemperature() {
        return maximumTemperature;
    }

    public void setMaximumTemperature(String maximumTemperature) {
        this.maximumTemperature = maximumTemperature;
    }

    public String getMinimumTemperature() {
        return minimumTemperature;
    }

    public void setMinimumTemperature(String minimumTemperature) {
        this.minimumTemperature = minimumTemperature;
    }

    public JSONObject getWindDirection() {
        return windDirection;
    }

    public void setWindDirection(JSONObject windDirection) {
        this.windDirection = windDirection;
    }

    public JSONObject getWeather() {
        return weather;
    }

    public void setWeather(JSONObject weather) {
        this.weather = weather;
    }

    @Override
    public String toString() {
        return "ATWeatherBean{" +
                "cnGreetings='" + cnGreetings + '\'' +
                ", enGreetings='" + enGreetings + '\'' +
                ", windLevel=" + windLevel +
                ", maximumTemperature='" + maximumTemperature + '\'' +
                ", minimumTemperature='" + minimumTemperature + '\'' +
                ", windDirection=" + windDirection +
                ", weather=" + weather +
                ", id='" + id + '\'' +
                ", imgSort='" + imgSort + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", jumpUrl='" + jumpUrl + '\'' +
                '}';
    }

    public String get_$PM25232() {
        return _$PM25232;
    }

    public void set_$PM25232(String _$PM25232) {
        this._$PM25232 = _$PM25232;
    }
}
