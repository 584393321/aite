package com.aliyun.ayland.data;

import android.os.Parcel;
import android.os.Parcelable;

public class ATEnvironmentTopBean implements Parcelable {

    /**
     * wea : 阴
     * air_level : 优
     * air_tips : 空气很好，可以外出活动，呼吸新鲜空气，拥抱大自然！
     * city : 上海
     * weatherCode : 02
     * humidity : 56%
     * win_speed : 3-4级
     * SoundDecibelValue : 21
     * air_pm25 : 34
     * tem : 21
     * TVOC : 200
     * HCHO : 230
     * CO2 : 100
     * air_pm25 : 30
     */

    private String wea;
    private String air_level;
    private String air_tips;
    private String city;
    private String weatherCode;
    private String humidity;
    private String win_speed;
    private String SoundDecibelValue;
    private String air_pm25;
    private String tem;
    private int TVOC;
    private int HCHO;
    private int CO2;
    private int deviceStatus;

    public String getWea() {
        return wea;
    }

    public void setWea(String wea) {
        this.wea = wea;
    }

    public String getAir_level() {
        return air_level;
    }

    public void setAir_level(String air_level) {
        this.air_level = air_level;
    }

    public String getAir_tips() {
        return air_tips;
    }

    public void setAir_tips(String air_tips) {
        this.air_tips = air_tips;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWeatherCode() {
        return weatherCode;
    }

    public void setWeatherCode(String weatherCode) {
        this.weatherCode = weatherCode;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getWin_speed() {
        return win_speed;
    }

    public void setWin_speed(String win_speed) {
        this.win_speed = win_speed;
    }

    public String getSoundDecibelValue() {
        return SoundDecibelValue;
    }

    public void setSoundDecibelValue(String SoundDecibelValue) {
        this.SoundDecibelValue = SoundDecibelValue;
    }

    public String getAir_pm25() {
        return air_pm25;
    }

    public void setAir_pm25(String air_pm25) {
        this.air_pm25 = air_pm25;
    }

    public String getTem() {
        return tem;
    }

    public void setTem(String tem) {
        this.tem = tem;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(wea);
        out.writeString(air_level);
        out.writeString(air_tips);
        out.writeString(city);
        out.writeString(weatherCode);
        out.writeString(humidity);
        out.writeString(win_speed);
        out.writeString(SoundDecibelValue);
        out.writeString(air_pm25);
        out.writeString(tem);
        out.writeInt(TVOC);
        out.writeInt(HCHO);
        out.writeInt(CO2);
        out.writeInt(deviceStatus);
    }

    public static final Creator<ATEnvironmentTopBean> CREATOR = new Creator<ATEnvironmentTopBean>() {
        @Override
        public ATEnvironmentTopBean[] newArray(int size) {
            return new ATEnvironmentTopBean[size];
        }

        @Override
        public ATEnvironmentTopBean createFromParcel(Parcel in) {
            return new ATEnvironmentTopBean(in);
        }
    };

    public ATEnvironmentTopBean() {

    }

    private ATEnvironmentTopBean(Parcel in) {
        wea = in.readString();
        air_level = in.readString();
        air_tips = in.readString();
        city = in.readString();
        weatherCode = in.readString();
        humidity = in.readString();
        win_speed = in.readString();
        SoundDecibelValue = in.readString();
        air_pm25 = in.readString();
        tem = in.readString();
        TVOC = in.readInt();
        HCHO = in.readInt();
        CO2 = in.readInt();
        deviceStatus = in.readInt();
    }

    public int getTVOC() {
        return TVOC;
    }

    public void setTVOC(int TVOC) {
        this.TVOC = TVOC;
    }

    public int getHCHO() {
        return HCHO;
    }

    public void setHCHO(int HCHO) {
        this.HCHO = HCHO;
    }

    public int getCO2() {
        return CO2;
    }

    public void setCO2(int CO2) {
        this.CO2 = CO2;
    }

    public int getDeviceStatus() {
        return deviceStatus;
    }

    public void setDeviceStatus(int deviceStatus) {
        this.deviceStatus = deviceStatus;
    }
}