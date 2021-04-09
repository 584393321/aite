package at.smarthome;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

/**
 * 
 * @author 孙波海
 * @file AT_FuncValueByDevClass.java
 * @date 2015年11月24日
 * @time 上午8:34:14
 * @describe 根据类型 及功能及value获取func_value
 */
public class AT_FuncValueByDevClass {
	/**
	 * 背景音乐除外，都调用该方法获取func_value
	 * @param dev_class_type 设备类型
	 * @param func_command   设备具备的功能属性
	 * @param value          亮度值和温度值
	 * @return               返回json 格式的字符串
	 */
	public static JSONObject getFuncValue(String dev_class_type,int value)
	{
		JSONObject jsonO=new JSONObject();
		try{
			if(AT_DeviceClassType.LIGHT.equals(dev_class_type)){
				jsonO.put("value",0);
			}else if(AT_DeviceClassType.DIMMER.equals(dev_class_type)
					|| AT_DeviceClassType.AIRCONDITION.equals(dev_class_type)
					|| AT_DeviceClassType.ICOOL.equals(dev_class_type) 
					|| AT_DeviceClassType.CENTRAL_AIR.equals(dev_class_type)){
				jsonO.put("value",value);
			}else{
				jsonO.put("value",0);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return jsonO;
	}
	/**
	 * 背景音乐除外，都调用该方法获取func_value
	 * @param dev_class_type 设备类型
	 * @param func_command   设备具备的功能属性
	 * @param value          亮度值和温度值
	 * @return               返回json 格式的字符串
	 * @throws  
	 */
	public static JSONObject getFuncValue(String dev_class_type,String func_command,int value)
	{
		JSONObject jsonO=new JSONObject();
		try{
			if(AT_DeviceCmdByDeviceType.Aircondition.TEMPERATURE.equals(func_command)) {
				jsonO.put(func_command, value);
			}else if(AT_DeviceCmdByDeviceType.RGBLigth.BRIGHTNESS.equals(func_command)){
					jsonO.put(func_command,value);
			}else if(AT_DeviceCmdByDeviceType.Audio.VOLUME.equals(func_command)){
				jsonO.put(func_command, value);
			}else{
				jsonO.put("value",value);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return jsonO;
	}
	
	
}
