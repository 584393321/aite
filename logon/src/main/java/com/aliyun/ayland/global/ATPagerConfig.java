package com.aliyun.ayland.global;

public final class ATPagerConfig {
    private ATPagerConfig(){}

    /**
     * 通用版的页面配置，如果没特殊的配置要求就复用这些页面
     */
    public static final class Common{
        public static final String HomeMain = "/at/home/main";
        public static final String HomeSetting = "/at/home/setting";
        public static final String HomeScreenSaver = "/at/home/screenSaver";
        public static final String HomeScan = "/at/home/scan";
        public static final String HomeReset = "/at/home/reset";
        public static final String HomeSipCall = "/at/home/sipCall";
        public static final String ScanConfig = "/at/setting/scanConfig";
        public static final String FaceRegister = "/at/face/register";
        public static final String FaceUsers = "/at/face/users";
    }

    /**
     * TV版的页面
     */
    public static final class Tv
    {
        public static final String HomeMain = "/tv/home/main";
        public static final String HomeSetting = "/tv/home/setting";
        public static final String HomeScreenSaver = "/tv/home/screenSaver";
        public static final String HomeScan = "/tv/home/scan";
        public static final String HomeReset = "/tv/home/reset";
        public static final String HomeSipCall = "/tv/home/sipCall";
        public static final String ScanConfig = "/tv/setting/scanConfig";
        public static final String FaceRegister = "/tv/face/register";
        public static final String FaceUsers = "/tv/face/users";
    }

    /**
     * 普及
     */
    public static final class PJ
    {
        public static final String HomeMain = "pj/home/main";
    }

}
