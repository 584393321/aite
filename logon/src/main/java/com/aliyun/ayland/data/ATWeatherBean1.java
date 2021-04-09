package com.aliyun.ayland.data;

import com.google.gson.annotations.SerializedName;

public class ATWeatherBean1 {
    /**
     * outdoorsWeather : {"air_level":"优","city":"上海","PM2.5":"40","outdoorsWeather":{"code":"07","cnName":"小雨","enName":"Light rain"},"windLevel":{"code":"0","cnName":"微风","enName":"<5.4m/s"},"maximumTemperature":"36","humidity":"75%","minimumTemperature":"29","windDirection":{"code":"5","cnName":"西南风","enName":"Southwest"},"tem":"31"}
     * roomWeather : {"deviceStatus":-1,"PM2.5":"11","temperature":"22","humidity":"63","TVOC":"20"}
     */

    private OutdoorsWeatherBeanX outdoorsWeather;
    private RoomWeatherBean roomWeather;

    public OutdoorsWeatherBeanX getOutdoorsWeather() {
        return outdoorsWeather;
    }

    public void setOutdoorsWeather(OutdoorsWeatherBeanX outdoorsWeather) {
        this.outdoorsWeather = outdoorsWeather;
    }

    public RoomWeatherBean getRoomWeather() {
        return roomWeather;
    }

    public void setRoomWeather(RoomWeatherBean roomWeather) {
        this.roomWeather = roomWeather;
    }

    public static class OutdoorsWeatherBeanX {
        /**
         * air_level : 优
         * city : 上海
         * PM2.5 : 40
         * outdoorsWeather : {"code":"07","cnName":"小雨","enName":"Light rain"}
         * windLevel : {"code":"0","cnName":"微风","enName":"<5.4m/s"}
         * maximumTemperature : 36
         * humidity : 75%
         * minimumTemperature : 29
         * windDirection : {"code":"5","cnName":"西南风","enName":"Southwest"}
         * tem : 31
         */

        private String air_level;
        private String city;
        @SerializedName("PM2.5")
        private String _$PM25153; // FIXME check this code
        private OutdoorsWeatherBean outdoorsWeather;
        private WindLevelBean windLevel;
        private String maximumTemperature;
        private String humidity;
        private String minimumTemperature;
        private WindDirectionBean windDirection;
        private String tem;

        public String getAir_level() {
            return air_level;
        }

        public void setAir_level(String air_level) {
            this.air_level = air_level;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String get_$PM25153() {
            return _$PM25153;
        }

        public void set_$PM25153(String _$PM25153) {
            this._$PM25153 = _$PM25153;
        }

        public OutdoorsWeatherBean getOutdoorsWeather() {
            return outdoorsWeather;
        }

        public void setOutdoorsWeather(OutdoorsWeatherBean outdoorsWeather) {
            this.outdoorsWeather = outdoorsWeather;
        }

        public WindLevelBean getWindLevel() {
            return windLevel;
        }

        public void setWindLevel(WindLevelBean windLevel) {
            this.windLevel = windLevel;
        }

        public String getMaximumTemperature() {
            return maximumTemperature;
        }

        public void setMaximumTemperature(String maximumTemperature) {
            this.maximumTemperature = maximumTemperature;
        }

        public String getHumidity() {
            return humidity;
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }

        public String getMinimumTemperature() {
            return minimumTemperature;
        }

        public void setMinimumTemperature(String minimumTemperature) {
            this.minimumTemperature = minimumTemperature;
        }

        public WindDirectionBean getWindDirection() {
            return windDirection;
        }

        public void setWindDirection(WindDirectionBean windDirection) {
            this.windDirection = windDirection;
        }

        public String getTem() {
            return tem;
        }

        public void setTem(String tem) {
            this.tem = tem;
        }

        public static class OutdoorsWeatherBean {
            /**
             * code : 07
             * cnName : 小雨
             * enName : Light rain
             */

            private String code;
            private String cnName;
            private String enName;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getCnName() {
                return cnName;
            }

            public void setCnName(String cnName) {
                this.cnName = cnName;
            }

            public String getEnName() {
                return enName;
            }

            public void setEnName(String enName) {
                this.enName = enName;
            }
        }

        public static class WindLevelBean {
            /**
             * code : 0
             * cnName : 微风
             * enName : <5.4m/s
             */

            private String code;
            private String cnName;
            private String enName;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getCnName() {
                return cnName;
            }

            public void setCnName(String cnName) {
                this.cnName = cnName;
            }

            public String getEnName() {
                return enName;
            }

            public void setEnName(String enName) {
                this.enName = enName;
            }
        }

        public static class WindDirectionBean {
            /**
             * code : 5
             * cnName : 西南风
             * enName : Southwest
             */

            private String code;
            private String cnName;
            private String enName;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getCnName() {
                return cnName;
            }

            public void setCnName(String cnName) {
                this.cnName = cnName;
            }

            public String getEnName() {
                return enName;
            }

            public void setEnName(String enName) {
                this.enName = enName;
            }
        }
    }

    public static class RoomWeatherBean {
        /**
         * deviceStatus : -1
         * PM2.5 : 11
         * temperature : 22
         * humidity : 63
         * TVOC : 20
         */

        private int deviceStatus;
        @SerializedName("PM2.5")
        private String _$PM2577; // FIXME check this code
        private String temperature;
        private String humidity;
        private String TVOC;

        public int getDeviceStatus() {
            return deviceStatus;
        }

        public void setDeviceStatus(int deviceStatus) {
            this.deviceStatus = deviceStatus;
        }

        public String get_$PM2577() {
            return _$PM2577;
        }

        public void set_$PM2577(String _$PM2577) {
            this._$PM2577 = _$PM2577;
        }

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }

        public String getHumidity() {
            return humidity;
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }

        public String getTVOC() {
            return TVOC;
        }

        public void setTVOC(String TVOC) {
            this.TVOC = TVOC;
        }
    }
}
