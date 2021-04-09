package at.smarthome;


public class AT_DevUpTypeFinalManager {


    public static String getDevClass(int uptype) {
        String result = "";
        switch (uptype) {
            case 0x12:
            case 0x13:
            case 0x14:
                result = AT_DeviceClassType.LIGHT;
                break;
            case 0x15:
                result = AT_DeviceClassType.DIMMER;
                break;
            case 0x16:
                result = AT_DeviceClassType.CURTAIN;
                break;
            case 0x17:
                result = AT_DeviceClassType.SOCKET;
                break;
            case 0x18:
                result = AT_DeviceClassType.THERMOSTAT;
                break;
            case 0x1a:// 情景面板
                result = AT_DeviceClassType.PANEL;
                break;
            case 0x1b:// 红外转发器
                result = AT_DeviceClassType.REPEATER;
                break;
            case 0x1c:
                result = AT_DeviceClassType.RGB_LIGHT;
                break;
            case 0x1d:
                result = AT_DeviceClassType.FRESH_AIR_SYSTEM;
                break;
            case 0x1e:
                result = AT_DeviceClassType.VALVE_CONTROLLER;
                break;
            case  0x20:
               result = AT_DeviceClassType.ZIGBEE_TO_485;
                break;
            case 0xA0:
                //人体热释红外传感器
                result = AT_DeviceClassType.Sensor.SENSOR_HW;
                break;
            case 0xA1:
                //门磁
                result = AT_DeviceClassType.Sensor.SENSOR_MC;
                break;
            case 0xA2:
                //烟雾传感器
                result = AT_DeviceClassType.Sensor.SENSOR_YW;
                break;
            case 0xA3:
                //可燃气体传感器
                result = AT_DeviceClassType.Sensor.SENSOR_RQ;
                break;
            case 0xA4:
                //一氧化碳传感器
                result = AT_DeviceClassType.Sensor.SENSOR_CO;
                break;
            case 0xA5:

                //温湿度传感器
                result = AT_DeviceClassType.Sensor.SENSOR_WS;
                break;
            case 0xA6:
                //水侵
                result = AT_DeviceClassType.Sensor.SENSOR_SJ;
                break;
            case 0xA7:
                //SOS紧急按钮
                result = AT_DeviceClassType.Sensor.SENSOR_SOS;
                break;
            case 0xB0://汇泰龙
            case 0xb7://勒奇
                //智能门锁
                result = AT_DeviceClassType.SMARTLOCK;
                break;
            case 0xb1:
                //智能晾衣架
                result = AT_DeviceClassType.DRYINGRACKS;
                break;
            case 0xB3://锁，毫力士
                result = AT_DeviceClassType.SMARTLOCK_HL;
                break;
            case 0xB4://艾特
                result = AT_DeviceClassType.SMARTLOCK_AT;
                break;
            case 0xB5://杜亚内置窗帘
                result = AT_DeviceClassType.CURTAIN;
                break;
            case 0xB6://艾特双窗帘面板
                result = AT_DeviceClassType.CURTAIN_PANEL;
                break;
            case 0x3f:
                result = AT_DeviceClassType.IR_DEV;
                break;

            default:
                result = "";
        }
        return result;
    }


    public static int isCanControl(String dev_class_type) {
        if (AT_DeviceClassType.SAFE_BUILTIN.equals(dev_class_type) || AT_DeviceClassType.COORDIN_ZIGBEE.equals(dev_class_type)|| AT_DeviceClassType.ZIGBEE_TO_485.equals(dev_class_type)) {
            return 0;
        } else if (AT_DeviceClassType.ICOOL.equals(dev_class_type) || AT_DeviceClassType.COORDIN_AT_SPEAKER.equals(dev_class_type) || AT_DeviceClassType.COORDIN_AIR_HAIER.equals(dev_class_type)) {
            return 1;
        } else if (AT_DeviceClassType.COORDIN_KONNEX.equals(dev_class_type) || AT_DeviceClassType.COORDIN_XB_ROBOT.equals(dev_class_type)) {
            return 0;
        } else if (dev_class_type.indexOf("sensor_") > -1) {  //内置zigbee安防
            return 0;
        } else if (AT_DeviceClassType.COORDIN_AT_SENSOR.equals(dev_class_type) || AT_DeviceClassType.IR_DEV.equals(dev_class_type) || dev_class_type.indexOf("floor_warm_") >= 0 || dev_class_type.indexOf("fresh_air_nt") > -1 || dev_class_type.equals(AT_DeviceClassType.SMARTLOCK_LATE)) {
            return 0;
        } else if (dev_class_type.indexOf("central_air_") > -1 ||dev_class_type.indexOf("central_airwater_") > -1 ||dev_class_type.indexOf("noise") > -1 || dev_class_type.equals(AT_DeviceClassType.COORDIN_BSP_SPEAKER) || dev_class_type.equals(AT_DeviceClassType.CURTAIN_KECO_K)) {  //中央空调网关
            return 0;
        } else if (dev_class_type.equals(AT_DeviceClassType.CAMERA_ONVIF) || dev_class_type.equals(AT_DeviceClassType.IPCAM)) {
            return 1;
        } else if (dev_class_type.indexOf("_bs")>0||AT_DeviceClassType.DRY_CONTACT_SECURITY.equals(dev_class_type)) {
            return 0;
        }else {
            return 1;
        }

    }

    public static int isCanControl(int dev_uptype) {
        int i = 0;
        switch (dev_uptype) {
            case 0x12:
            case 0x13:
            case 0x14:
            case 0x15:
            case 0x16:
            case 0x17:
            case 0x18:
            case 0xB0:
            case 0xb1:
            case 0x1b:// 红外转发器不会被加入到数据库保存，只保存下面的设备
            case 0x1c:
            case 0x1d:
            case 0x1e:
            case 0xb3://毫力士锁
            case 0xb4://艾特锁
            case 0xb7://勒奇
            case 0xb5://杜亚内置窗帘
                i = 1;
                break;
            case 0xb6://窗帘面板
            case 0x1a:// 情景面板
                i = 0;
                break;
            case 0xA0:
            case 0xA1:
            case 0xA2:
            case 0xA3:
            case 0xA6:
            case 0xA7:
            case 0xA4:
                i = 0;
                break;
            case 0xA5:
                //温湿度传感器
                i = 0;
                break;
            case 0x20://zigbee to 485
                i = 0;
                break;

            default:
                i = 0;
        }
        return i;
    }
}