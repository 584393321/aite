package at.smarthome;

/**
 * msg_type
 * 
 * @author CHT
 * 
 */
public interface AT_MsgTypeFinalManager {
	public final static String CHECK_USERNAME_PERMISSIONS = "check_username_permissions";
	public final static String SEARCH = "search";
	public final static String GATEWAY_MANAGER = "gateway_manager";
	public final static String ROOM_MANAGER = "room_manager";
	public final static String DEVICE_MANAGER = "device_manager";	
	public final static String SENSOR_MANAGER = "sensor_manager";
	public final static String HEARTBEAT = "heartbeat";
	public final static String CONNECT="connect";
	public final static String SECURITY_MANAGER = "security_manager";
	public final static String COMBINATION_CONTROL_MANAGER = "combination_control_manager";
	public final static String TIMER_CONTROL_MANAGER = "timer_control_manager";
	public final static String ENVIRONMENT_CONTROL_MANAGER = "environment_control_manager";
	public final static String SECURITY_CONTROL_MANAGER = "security_control_manager";	
	public final static String HOST_MANAGER = "host_manager";
	public final static String PHONE_MANAGER = "phone_manager";
	public final static String LIFT_INFO = "lift_info";
	public final static String DEVICE_HAVE_FUNCTION = "device_have_function";
	public final static String DEVICE_CLASS_INFO = "device_class_info";
	public final static String ROOM_CLASS_INFO = "room_class_info";
	
	public final static String ALARM_VOICE_INFO = "alarm_voice_info";
	public final static String ALARM_LOGS_INFO = "alarm_logs_info";
	public final static String ALARM_PICS_INFO = "alarm_pics_info";
	public final static String DEVICE_STATE_INFO = "device_state_info";
	public final static String DEVICE_CONTROL = "device_control";
	public final static String ELECTRICAL_LOGS = "electrical_logs";
	public final static String SECURITY_MODE_CHANGE = "security_mode_change";

	public final static String ZIGBEE_PANID_INFO = "zigbee_panid_info";
	
	public final static String SENSOR_NEW_DEVICE_MANAGER = "sensor_new_device_manager";
	public final static String SENSOR_ALARM = "sensor_alarm";
	public final static String SYSTEM_PASSWORD_MANAGER = "system_password_manager";
	public final static String REMOTE_ONLINE_STATE = "remote_online_state";
	public final static String RESTORE_DEFAULT_PASSWORD = "restore_default_password";
	public final static String PANEL_DEVICE_KEY_BIND = "panel_device_key_bind";
	public final static String DEVICE_BS_MANAGER = "device_bs_manager";//父节点设备查询，模块状态查询
	public final static String DEVICE_UP_AGAIN = "device_up_again";
	public final static String DEVICE_ALLOW_NETWORK = "device_allow_network";
	//public final static String ZIGBEE_DEVICE_CONFIG = "zigbee_device_config";
	public final static String DEV_SEARCH_BY_CLASS = "dev_search_by_class";// UI层用来查找与上报的msg_type
    public final static String DEVICE_CLASS_BASE="device_class_base";
    public final static String MODULE_STATE_INFO = "module_state_info";//模块状态
    public final static String MORE_CONTROL_DEVICE="more_control_device";
    //UI下发都是这个命令
    public final static String INFRARED_CODE_CONTROL="infrared_code_control";
    //infrared_code_study 只有收到zigbee 模块学习回复时才用,UI被 动接收才有
    public final static String INFRARED_CODE_STUDY="infrared_code_study";
    public final static String SCENE_INFO="scene_info";   
    public final static String DEVICE_STATE_UP_BY_ADDR="dev_state_up_by_addr";
    public final static String COORDIN_COMBINATION_MANAGER="coordin_combination_manager";
    public final static String UPGRADE_FIRMWARE="upgrade_firmware";
    public final static String LOCK_USERS_INFO="lock_users_info";
    public final static String LOCK_PASSWORD_MANAGER="lock_password_manager";
    public final static String DEVICE_EXCEPTION_HANDING="device_exception_handing";
    public final static String DEVICE_BIND_CODE="device_bind_code";
    public final static String SYSTEM_TEST_SAFE="system_test_safe";
    public final static String CONNECT_STATE="connect_state"; 
    public final static String LOCK_OPEN_LOGS="lock_open_logs";
    public final static String TV_CHANNEL_COLLECT="tv_channel_collect";
    public final static String CAMERA_MONITOR_MANAGER="camera_monitor_manager";
    public final static String DEVICE_PASSWORD_MANAGER="device_password_manager";
    public final static String PHONE_ONLINE_NOTIFICATION="phone_online_notification";
    public final static String WIFI_MANAGER="wifi_manager";
    public final static String ETHERNET_MANAGER="ethernet_manager";
    public final static String SYSTEM_SETTING="system_setting";
	public final static String DEVICE_DOORBELL="device_doorbell";
	public final static String DEVICE_ABNORMAL_STATE="device_abnormal_state";
	public final static String HOMEKIT_CONTROL = "homekit_control";
	public final static String COMMAND_REDIRECT="command_redirect";
	public final static String DEVICE_CLASS_BY_PARENT="device_class_by_parent";
	public final static String DEVICE_ATTRIBUTE_SAFE="device_attribute_safe";
	public final static String ALARM_ELIMINATION = "alarm_elimination";
    public final static String SYSTEM_INFO="system_info";
	public final static String SYSTEM_SET="system_set";
	public final static String TIMEZONE_MANAGER="timezone_manager";
	public final static String FRIEND_MANAGER="friend_manager";
	public final static String NOISE_NOTIFY="noise_notify";
	public final static String NOISE_NOTIFY_TO_PHONE="noise_notify_to_phone";
	public final static String NOISE_WARN = "noise_warn";
	public final static String NOISE_COMPLAINT = "noise_complaint";
	public final static String NOISE_STATE = "noise_state";
	public final static String SECURITY_MODE_STATE = "security_mode_state";
	public final static String HEALTH_INFO = "health_info";
	public final static String HOST_MODE_MANAGER="host_mode_manager";
    public final static String FAN_MANAGER="fan_manager";
	public final static String VERSION_UPGRADE="version_upgrade";
	public interface HCOMM {
		public final static String  SENSOR_STATE_CHANGE="sensor_state_change";
		public final static String DEVICE_CHANGE_NOTICE = "device_change_notice";
		public final static String NEW_DEVICE_UP = "new_device_up";// hcomm与外设用的，UI层不用这个
		//public final static String PANEL_DEVICE_KEY = "panel_device_key";
		public final static String ENVIRONMENTAL_MONITORING = "environmental_monitoring";
		public final static String SINGLE_ACTION_CONTROL = "single_action_control";
		public final static String DEVICE_NEW_MANAGER="device_new_manager";
		public final static String DEVICE_SYNC="device_sync";
		public final static String ZIGBEE_DATA_SYNC="zigbee_data_sync";
		public final static String REPEATER_DEVICE_SYNC="repeater_device_sync";
		//需要business做转发给所有客户端时，外面包装一层json ,msg_type=business_redirect_to_all_client，原数据放 data中
		public final static String BUSINESS_REDIRECT_TO_ALL_CLIENT="business_redirect_to_all_client";
		//需要business做转发给公网或小区时，外面包装一层json ,msg_type=business_redirect_to_remote  原数据放 data中
		public final static String BUSINESS_REDIRECT_TO_REMOTE="business_redirect_to_remote";

	}
	public interface  Test{
		public final static String  SECURITY_MODE_STATE="security_mode_state";
	}

}
