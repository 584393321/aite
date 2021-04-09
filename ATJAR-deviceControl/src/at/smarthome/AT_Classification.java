package at.smarthome;

public class AT_Classification {
    /* 第1位是否具备家电特性，第2位是否具备安防探测特性
     第3位是否具备环境探测特性，第4位是否具备开锁，开门的特性
     第5位是否具备机器人特性，第6位是否具备可视如摄像头特性*/

    public static int getClassificationByUpType(String dev_class_type) {

        if (AT_DeviceClassType.COORDIN_AT_SENSOR.equals(dev_class_type) ||
                AT_DeviceClassType.SAFE_BUILTIN.equals(dev_class_type) ||
                AT_DeviceClassType.COORDIN_ZIGBEE.equals(dev_class_type)||
                AT_DeviceClassType.DRY_CONTACT_SECURITY.equals(dev_class_type)) {
            return 0;
        } else if (AT_DeviceClassType.ICOOL.equals(dev_class_type)) {
            return 5;
        } else if (AT_DeviceClassType.COORDIN_AIR_HAIER.equals(dev_class_type) || AT_DeviceClassType.Sensor.SENSOR_WS.equals(dev_class_type) || AT_DeviceClassType.AQMS.equals(dev_class_type)) {
            return 4;
        } else if (AT_DeviceClassType.COORDIN_KONNEX.equals(dev_class_type) || AT_DeviceClassType.COORDIN_BSP_SPEAKER.equals(dev_class_type)) {
            return 0;
        } else if (AT_DeviceClassType.SMARTLOCK.equals(dev_class_type) || AT_DeviceClassType.SMARTLOCK_HL.equals(dev_class_type)|| AT_DeviceClassType.SMARTLOCK_LATE.equals(dev_class_type)) {
            return 8;
        } else if (AT_DeviceClassType.CAMERA_ONVIF.equals(dev_class_type) || AT_DeviceClassType.IPCAM.equals(dev_class_type)) {
            return 32;
        } else if (dev_class_type.indexOf("sensor_") == 0 && !dev_class_type.equals("sensor_ws")) {
            return 2;
        } else {
            return 1;
        }

    }

    public static int getClassificationByUpType(int dev_uptype) {
        int result = 1;
        switch (dev_uptype) {
            case 0x12:
            case 0x13:
            case 0x14:
            case 0x15:
            case 0x16:
            case 0x17:
            case 0x18:

            case 0xb1:
            case 0x1a:// 情景面板
            case 0x1b:// 红外转发器不会被加入到数据库保存，只保存下面的设备
            case 0x1c:
            case 0x1d:
            case 0x1e:
                result = 1;
                break;
            case 0xB3:
            case 0xB0:
            case 0xB4:
            case 0xB7:
                result = 8;
                break;
            case 0xA0:
            case 0xA1:
            case 0xA2:
            case 0xA3:
            case 0xA6:
            case 0xA7:
            case 0xA4:
                result = 2;
                break;
            case 0xA5:
                //温湿度传感器
                result = 4;
                break;

            default:
                result = 1;
        }
        return result;
    }
}
