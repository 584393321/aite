package at.smarthome;

/**
 * 针对原子设备才有flag，父设备及模块则没有
 * 
 * @author th
 *
 */
public class AT_DevClassFlag {
	public static final String WIFI = "wifi";
	public static final String REPEATER = "repeater";
	public static final String ZIGBEE = "zigbee";
	public static final String IO = "io";
	public static final String KONNEX = "konnex";
	public static final String NETWORK = "network";


	public static final String getDevClasFlag(String dev_class_type_b, String dev_class_type) {
		if (dev_class_type_b.equals(AT_DeviceClassType.COORDIN_ZIGBEE)||dev_class_type_b.equals(AT_DeviceClassType.ZIGBEE_BUILTIN)||dev_class_type_b.equals(AT_DeviceClassType.IR_DEV)) {
			if (dev_class_type.equals(AT_DeviceClassType.AIRCONDITION)
					|| dev_class_type.equals(AT_DeviceClassType.AMPLIFIER)
					|| dev_class_type.indexOf("dvb")==0 || dev_class_type.equals(AT_DeviceClassType.DVD)
					|| dev_class_type.equals(AT_DeviceClassType.TV)
					|| dev_class_type.equals(AT_DeviceClassType.FAN)
					|| dev_class_type.equals(AT_DeviceClassType.PROJECTOR)
					|| dev_class_type.equals(AT_DeviceClassType.AIR_PURIFIER)) {
				return REPEATER;
			} else {
				return ZIGBEE;
			}
		} else if (dev_class_type_b.equals(AT_DeviceClassType.ICOOL)
				|| dev_class_type_b.equals(AT_DeviceClassType.COORDIN_XZ_SPEAKER)
						|| dev_class_type_b.equals(AT_DeviceClassType.COORDIN_XB_ROBOT)
						|| dev_class_type_b.equals(AT_DeviceClassType.AQMS)) {
			return WIFI;
		} else if (dev_class_type_b.equals(AT_DeviceClassType.COORDIN_KONNEX)) {

			if (dev_class_type.equals(AT_DeviceClassType.COORDIN_KONNEX)) {
				return NETWORK;
			} else {
				return KONNEX;
			}
		} else if (dev_class_type_b.equals(AT_DeviceClassType.COORDIN_AIR_HAIER)) {
			return WIFI; 
		}else if (dev_class_type_b.equals(AT_DeviceClassType.COORDIN_AT_SPEAKER)||dev_class_type_b.equals(AT_DeviceClassType.COORDIN_BSP_SPEAKER)||dev_class_type_b.equals(AT_DeviceClassType.SPEAKER_PANEL)||dev_class_type_b.equals(AT_DeviceClassType.COORDIN_DSP_SPEAKER)) {
			return NETWORK; 
		}else if (dev_class_type_b.equals(AT_DeviceClassType.SAFE_BUILTIN)) {
			return IO; 
		}else if (dev_class_type_b.equals(AT_DeviceClassType.CAMERA_ONVIF)||dev_class_type_b.equals(AT_DeviceClassType.IPCAM)||dev_class_type_b.indexOf("central_air")>-1) {
			return NETWORK; 
		}
		return NETWORK;
	}
	
	public static String getDevClasFlag(int uptype) {
		String result = "";
		switch (uptype) {
		case 0x12:
		case 0x13:
		case 0x14:			
		case 0x15:		
		case 0x16:			
		case 0x17:				
		case 0x18://AT_DeviceClassType.THERMOSTAT;			
		case 0x1a:// 情景面板
		case 0x1c://T_DeviceClassType.RGB_LIGHT;
		case 0xA0:
			//人体热释红外传感器
		case 0xA1:
			//门磁
		case 0xA2:
			//烟雾传感器
		case 0xA3:
			//可燃气体传感器
		case 0xA4:
			//一氧化碳传感器
		case 0xA5:			
			//温湿度传感器
		case 0xA6:
			//水侵
		case 0xA7:
			//SOS紧急按钮
		case 0xB0:
			//智能门锁
		case 0xb1:
			//智能晾衣架
		case 0xB3://锁，毫力士

			case 0xB5:
			//杜亚内置窗帘
			case 0xB6:
				//窗帘面板
			case 0xb7://勒奇
				result =ZIGBEE;
			break;
		case 0x1b:// 红外转发器
			result =REPEATER;
			break;		
		default:
			result = "";
		}
		return result;
	}
}
