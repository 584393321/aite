package at.smarthome;

import java.util.HashMap;

public class AT_Constants {
	
	public static final class Config{
		public static final int DB_VERSION = 2;
		public static final int DOOR_SERVER_PORT = 9032;
		public static final int DEF_RING_VOLUME= 8;
	}


	public static final class SharedConfig{
		public static final String PREF_TYPE = "type";
		public static final String PREF_RING_NAME = "ring_name";
	}

	public static final class Key{
		public static final String KeyDoorSip = "key_door_sip";
		public static final String DoorDataType = "door_data_type";
		public static final String DoorSingle = "door_single";
		public static final String DoorList = "door_list";
		public static final String ModeMac = "mode_mac";		
		public static final String DeviceType = "device_type";
		public static final String ServerIp = "server_ip";
		public static final String DEVICE_PASSWORD="device_password";
		public static final String MonitorPwd = "monitor_pwd";
		public static final String TYPE_BRIDGE_ACCOUNT = "bridge_account";
	}
	
	public static final class SettingType
	{
		public static final String TYPE_HOME_ADDRESS = "home_address";
		public static final String TYPE_WALL_ADDRESS = "wall_address";
		public static final String TYPE_MODE_MAC = "mode_mac";
		public static final String TYPE_DEVICE_ALIAS = "device_alias";
		public static final String TYPE_COMMUNITY_INFO = "comunity_info";//小区信息
		public static final String TYPE_VOIP_INFO= "voip_info";
		public static final String TYPE_MULTI_RESPONSE ="multi_response";
		public static final String TYPE_CONNECTION_STATE = "connection_state";
		public static final String TYPE_MONITOR_PWD = "monitor_pwd";
		public static final String TYPE_FIRE_ALARM = "fire_alarm";
	}
	
	public static final class Oper
	{
		public static final String OPER_TYPE = "oper_type";
		public static final String OPER_EDIT = "edit";
		public static final String OPER_ADD  = "add";
	}
	
	public static final class Action{
		public static final String ACTION_REG_ONDOOR = "action.regdoor";
		public static final String ACTION_UNREG_ONDOOR = "action.unregdoor";
		public static final String ACTION_PAUSE_MUSIC = "action.pause_music";
		public static final String ACTION_MULTI_PHONE="action.multi.phone";
		public static final String ACTION_MODE_MAC = "action.mode_mac";
		public static final String ACTION_COMMUNITY_SERVER_CHANGED = "action.server_changed";
		public static final String ACTION_STOP_MONITOR = "action.stop_monitor";
		public static final String ACTION_MONITOR_PWD_CHANGED = "action.monitor_pwd_changed";
		public static final String ACTION_SYS_BACKUP = "action.sys_backup";
		public static final String ACTION_SYS_RESTORE = "action.sys_restore";
		public static final String ACTION_BRIDGE_PHONE = "action.bridge_phone";
		public static final String ACTION_SCENE_CHANGED = "scene_changed_action";

		public static final String ACTION_GLOBAL_CONFIG = "action.global_config";
		public static final String ACTION_EXIT_TALKSCREEN = "action.exit_talkscreen";

		public static final String ACTION_SCHEDULE_START_SERVER="action.schedule.service";
	}
	
	public static final class Language{
		public static final String SIMPLIFY_CHINESE = "cn";
		public static final String ENGLISH = "en";
		public static final String TRODITIONAL_CHINESE = "tw";
	}
	
	public static final class VolumeSize{
		public static final int VOLUME_MAX = 15;
		public static final int VOLUME_BIG = 10;
		public static final int VOLUME_MIDIUM = 5;
		public static final int VOLUME_SMALL = 1;
	}
	
	/**
	 * @author fr
	 * 二维码扫描类型
	 */
	public static final class QrType
	{
		public static final String TYPE_MODE = "type_mode";
		
	}

	public static final HashMap<String,String> cannotControlType = new HashMap<String,String>(){		
		private static final long serialVersionUID = 3550291284801927377L;

		/**
		 *不可控制的设备类型 
		 */
		{
			put(AT_DeviceClassType.COORDIN_ZIGBEE, AT_DeviceClassType.COORDIN_ZIGBEE);
			//put(AT_DeviceClassType.COORDIN_XZ_SPEAKER, AT_DeviceClassType.COORDIN_XZ_SPEAKER);
			put(AT_DeviceClassType.PANEL, AT_DeviceClassType.PANEL);
			put(AT_DeviceClassType.COORDIN_KONNEX,AT_DeviceClassType.COORDIN_KONNEX);
			put(AT_DeviceClassType.COORDIN_AIR_HAIER,AT_DeviceClassType.COORDIN_AIR_HAIER);
			put(AT_DeviceClassType.Sensor.SENSOR_CO,AT_DeviceClassType.Sensor.SENSOR_CO);
			put(AT_DeviceClassType.Sensor.SENSOR_HW,AT_DeviceClassType.Sensor.SENSOR_HW);
			put(AT_DeviceClassType.Sensor.SENSOR_MC,AT_DeviceClassType.Sensor.SENSOR_MC);
			put(AT_DeviceClassType.Sensor.SENSOR_RQ,AT_DeviceClassType.Sensor.SENSOR_RQ);
			put(AT_DeviceClassType.Sensor.SENSOR_SJ,AT_DeviceClassType.Sensor.SENSOR_SJ);
			put(AT_DeviceClassType.Sensor.SENSOR_WS,AT_DeviceClassType.Sensor.SENSOR_WS);
			put(AT_DeviceClassType.Sensor.SENSOR_YW,AT_DeviceClassType.Sensor.SENSOR_YW);
			put(AT_DeviceClassType.Sensor.SENSOR_SOS,AT_DeviceClassType.Sensor.SENSOR_SOS);
			put(AT_DeviceClassType.Sensor.SENSOR_BUILTIN,AT_DeviceClassType.Sensor.SENSOR_BUILTIN);
		}
	};


	public static final class ServerMsgType{

	}
}
