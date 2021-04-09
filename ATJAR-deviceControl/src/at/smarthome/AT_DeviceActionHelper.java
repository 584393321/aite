package at.smarthome;

import org.json.JSONException;
import org.json.JSONObject;

public class AT_DeviceActionHelper {

	/**
	 * 判断设备动作是否带参
	 * 
	 * @param dev_class_type
	 * @param func_command
	 * @return
	 */
	public static boolean hasParam(String dev_class_type, String func_command) {
		if (AT_DeviceClassType.DIMMER.equals(dev_class_type) || AT_DeviceClassType.RGB_LIGHT.equals(dev_class_type)) {
			if (AT_DeviceCmdByDeviceType.Dimmer.BRIGHTNESS.equals(func_command)) {
				return true;
			}
		} else if (AT_DeviceClassType.AIRCONDITION.equals(dev_class_type)
				|| AT_DeviceClassType.ICOOL.equals(dev_class_type)
				|| AT_DeviceClassType.CENTRAL_AIR.equals(dev_class_type)) {
			if (AT_DeviceCmdByDeviceType.Aircondition.TEMPERATURE.equals(func_command)) {
				return true;
			}
		} else if (AT_DeviceClassType.THERMOSTAT.equals(dev_class_type)) {
			if (AT_DeviceCmdByDeviceType.Aircondition.TEMPERATURE.equals(func_command)) {
				return true;
			}
		} else if (AT_DeviceCmdByDeviceType.Audio.VOLUME.equals(func_command)) {
			return true;
		}

		return false;
	}

	/**
	 * 获取设备动作取参范围
	 * 
	 * @param func_command
	 * @return 空JOSNObject则表示该设备动作不带参，否则返回参数取值范围
	 */
	public static JSONObject getParamRange(String dev_class_type, String func_command) {
		JSONObject result = new JSONObject();
		try {
			if (AT_DeviceClassType.DIMMER.equals(dev_class_type)
					|| AT_DeviceClassType.RGB_LIGHT.equals(dev_class_type)) {
				if (AT_DeviceCmdByDeviceType.Dimmer.BRIGHTNESS.equals(func_command)) {
					result.put("max", 100);
					result.put("min", 1);
				}
			} else if (AT_DeviceClassType.AIRCONDITION.equals(dev_class_type)
					|| AT_DeviceClassType.ICOOL.equals(dev_class_type)
					|| AT_DeviceClassType.CENTRAL_AIR.equals(dev_class_type)) {
				if (AT_DeviceCmdByDeviceType.Aircondition.TEMPERATURE.equals(func_command)) {
					result.put("max", 31);
					result.put("min", 17);
				}
			} else if (AT_DeviceClassType.THERMOSTAT.equals(dev_class_type)) {
				if (AT_DeviceCmdByDeviceType.Aircondition.TEMPERATURE.equals(func_command)) {
					result.put("max", 100);
					result.put("min", 1);
				}
			} else 	if (AT_DeviceCmdByDeviceType.Audio.VOLUME.equals(func_command)) {
					result.put("max", 15);
					result.put("min", 1);
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String getParams(JSONObject func_value)
	{
		if(func_value != null){
			try{
				if(func_value.has(AT_DeviceCmdByDeviceType.Audio.VOLUME)){
					return String.valueOf(func_value.getInt(AT_DeviceCmdByDeviceType.Audio.VOLUME));
				}else if(func_value.has(AT_DeviceCmdByDeviceType.RGBLigth.BRIGHTNESS)){
					return String.valueOf(func_value.getInt(AT_DeviceCmdByDeviceType.RGBLigth.BRIGHTNESS));
				}else if(func_value.has(AT_DeviceCmdByDeviceType.Aircondition.TEMPERATURE)){
					return String.valueOf(func_value.getInt(AT_DeviceCmdByDeviceType.Aircondition.TEMPERATURE));
				}else if(func_value.has("value")){
					return String.valueOf(func_value.getInt("value"));
				}
			}catch(JSONException e){
				e.printStackTrace();
			}
		}
		return "";
	}
	
}
