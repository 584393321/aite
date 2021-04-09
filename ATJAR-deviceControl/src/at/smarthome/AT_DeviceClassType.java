package at.smarthome;

/**
 * 设备类型
 *
 * @author CHT
 *
 */
public interface AT_DeviceClassType {

    // 非调光开关
    public final static String LIGHT = "light";
    // 可调光开关
    public final static String DIMMER = "dimmer";
    // 窗帘
    public final static String CURTAIN = "curtain";
    // 插座
    public final static String SOCKET = "socket";
    // 背景音乐
    public final static String AUDIO = "audio";
    // 新风系统
    public final static String FRESH_AIR_SYSTEM = "fresh_air_system";
    // 地暖
    public final static String FLOOR_WARM = "floor_warm";
    // 面板
    public static final String PANEL = "panel";
    // 舒睡宝
    public static final String ICOOL = "icool";
    // 协调器
    public static final String COORDIN_ZIGBEE = "coordin_zigbee";
    // 室内机
    public static final String GATEWAY = "gateway";//带屏智能终端
    public static final String GATEWAY_VOICE="gateway_voice";//语音网关
    public final static String MIRROR="mirror";
    // 内置安防
    public static final String SAFE_BUILTIN = "safe_builtin";
    // 内置zigbee
    public static final String ZIGBEE_BUILTIN = "zigbee_builtin";
    // 小智音箱
    public static final String COORDIN_XZ_SPEAKER = "coordin_xz_speaker";
    // AT多功能面板
    public static final String COORDIN_AT_SPEAKER = "coordin_at_speaker";
    // 音乐面板
    public static final String SPEAKER_PANEL = "speaker_panel";
    // 贝斯普背景音乐
    public static final String COORDIN_BSP_SPEAKER = "coordin_bsp_speaker";
    // 迪士普背景音乐
    public static final String COORDIN_DSP_SPEAKER = "coordin_dsp_speaker";
    // 协调器
    public static final String COORDIN_KONNEX = "coordin_konnex";
    // 海尔空气盒子
    public static final String COORDIN_AIR_HAIER = "coordin_air_haier";
    // 红外转发器
    public final static String REPEATER = "repeater";
    // 机顶盒
    public static final String DVB = "dvb";
    // 机顶盒
    public static final String DVB_NET = "dvb_net";
    // 电视
    public final static String TV = "tv";
    // 空调
    public final static String AIRCONDITION = "aircondition";
    // DVDs
    public final static String DVD = "dvd";
    // 功放
    public final static String AMPLIFIER = "amplifier";
    // 智能晾衣架
    public final static String DRYINGRACKS = "dryingracks";
    // 智能门锁
    public final static String SMARTLOCK = "smartlock";
    // 豪力士智能门锁
    public final static String SMARTLOCK_HL = "smartlock_hl";
    // 艾特智能门锁
    public final static String SMARTLOCK_AT = "smartlock_at";
    // 耶鲁指纹锁
    public final static String SMARTLOCK_LATE = "smartlock_late";
    // 机器人
    public static final String COORDIN_XB_ROBOT = "coordin_xb_robot";
    /*//弗徕威 维拉机器人
    public static final String COORDIN_WL_ROBOT = "coordin_wl_robot";*/
    // 调光灯
    public static final String RGB_LIGHT = "rgb_light";
    // 艾特空气盒子
    public static final String AQMS = "aqms";
    // 艾特空气盒子
    public static final String AQMS_A="aqms_a";

    // 摄像头
    public static final String CAMERA_ONVIF = "camera_onvif";// onvif摄像头

    public static final String IPCAM= "ipcam";// 艾特摄像头
    public static final String IPCAM_LIGHT= "ipcam_light";// 灯泡摄像头
    public static final String CURTAIN_PANEL= "curtain_panel";// 艾特摄像头

    public static final String MAOYAN="smart_cat_eye"; //猫眼


    public static final String HCF_MAOYAN="hcf_maoyan";//好辰防猫眼

    // 极米投影仪
    // public static final String PROJECTOR_JM="projector_jm";
    // 中央空调
    public static final String CENTRAL_AIR_GL = "central_air_gl";
    public static final String CENTRAL_AIR_MD = "central_air_md";
    public static final String CENTRAL_AIR_DG = "central_air_dg";
    public static final String CENTRAL_AIR_DZ = "central_air_dz";
    public static final String CENTRAL_AIR_IRACC = "central_air_iracc";
    public static final String CENTRAL_AIR_RL = "central_air_rl";
    public static final String CENTRAL_AIR_MIT = "central_air_mit";//Mitsubishi
    public static final String CENTRAL_AIR_ZH = "central_air_zh";
    public static final String CENTRAL_AIRWATER_LONGER = "central_airwater_longer";
    public static final String CENTRAL_AIR_IRRAC = "central_air_iracc";
    public static final String CENTRAL_AIR = "central_air";//VRV类
    public static final String CENTRAL_AIRWATER = "central_airwater";//水机类
    //A-OK窗帘
    public static final String CURTAIN_AOK_M="curtain_aok_m";//奥科AM68-1/80-EM-p

    public static final String COORDIN_AT_SENSOR = "coordin_at_sensor"; //安防模组
    public static final String CURTAIN_KECO_K="curtain_keco_k";//凯高KT35-10/17-E
    // 新红外转发器
    public final static String IR_DEV = "ir_dev";//遥看方案

    public final static String PROJECTOR = "projector";//投影仪
    public final static String FAN = "fan";//风扇
    public final static String AIR_PURIFIER = "air_purifier";//空气净化器
    public final static String AIR_PURIFIER_CC = "air_purifier_cc";//空气净化器coclean
    public final static String AIR_PURIFIER_BS = "air_purifier_bs";//空气净化器coclean
    public final static String FRESH_AIR_NT6="fresh_air_nt6";//兰舍
    public final static String FRESH_AIR_NT1="fresh_air_nt1";//兰舍
    public final static String FLOOR_WARM_LONGER="floor_warm_longer";//longer永伟
    public final static String VALVE_CONTROLLER="valve_controller";//阀门控制器
    public final static String NOISE="noise";//噪音检测
    public final static String NOISE_JHM="noise_jhm";//北京京海鸣noise
    public final static String TELECONTROLLER_A9="telecontroller_a9";//摇控器
    public final static String ZIGBEE_TO_485="zigbee_to_485";//摇控器
    public final static String THERMOSTAT_JG_BS="thermostat_jg_bs";//际高温控,父类
    public final static String THERMOSTAT_JG="thermostat_jg";//温控面板,非标准的统一加公司标签
    public final static String THERMOSTAT_OKOFOFF_BS="thermostat_okonoff_bs";//柯耐弗
    public final static String THERMOSTAT="thermostat";//温控面板,标准的统一用这个
    public final static String DRY_CONTACT_SECURITY="dry_contact_security";
    public final static String CURTAIN_SF="curtain_sf";//尚飞
    public final static String THERMOSTAT_KNF="thermostat_knf";//柯耐弗
    public final static String AIR_DETECT_SJ="air_detect_sj";//胜洁

    public class Health{
        public final static String HEALTH_WEIGHT = "health_weight";//体重
        public final static String HEALTH_TEMPERATURE = "health_temperature";//体温
        public final static String HEALTH_B_PRESSURE="health_b_pressure";//血压
        public final static String HEALTH_FAT="health_fat";//脂肪
        public final static String HEALTH_B_SUGAR="health_b_sugar";//血糖
        public final static String HEALTH_B_OXYGEN="health_b_oxygen";//血氧
        public final static String HEALTH_BELTER = "health_belter"; //倍泰脂肪秤

        public static final class State{
            public static final String START = "start";
            public static final String FINISH = "finish";
            public static final String SHUTDOWN = "shutdown";
            public static final String TESTING = "testing";
        }
    }

    public class Sensor {
        public final static String SENSOR_PREFIX = "sensor_";
        // 门磁报警器
        public final static String SENSOR_MC = "sensor_mc";
        // 人体红外探测器
        public final static String SENSOR_HW = "sensor_hw";
        // 烟雾传感器
        public final static String SENSOR_YW = "sensor_yw";
        // 温湿度传感器
        public final static String SENSOR_WS = "sensor_ws";//是环境设备
        // 一氧化碳传感器
        public final static String SENSOR_CO = "sensor_co";
        // 水侵探测器
        public final static String SENSOR_SJ = "sensor_sj";
        // SOS紧急按钮
        public final static String SENSOR_SOS = "sensor_sos";
        // 可燃气体传感器
        public final static String SENSOR_RQ = "sensor_rq";
        // 内置安防
        public final static String SENSOR_BUILTIN = "sensor_builtin";
        public final static String SENSOR_BUILTOUT= "sensor_builtout";
    }

}
