package at.smarthome;


/**
 * @author fr
 * Sip对讲常量类
 */
public class SipConstants {
	private SipConstants() {}
	public static final String Tag = "sipkit";
	
	public static final String CallCmd = "call_cmd";
	public static final String CallMode = "call_mode";
	public static final String CallSip = "call_sip_addr";
	public static final String CallName = "call_name";
	public static final String ServiceName= "service_name";
	
	public static class Config{
		public static final int SIP_PORT = 8080;
		public static final String PUBLIC_SERVER_NAME_REG = "public"; //注册到公网的服务名 "com.atie.sipclient.public"
		public static final String COMMUNITY_SERVER_NAME_REG = "com.atie.sipclient.community"; //注册到小区服务器的服务名 "com.atie.sipclient.community"
		public static final int DURATION_RECORD_QUESTION = 30*1000; // 询问是否需要留言的时间延时
		public static final int DEFAULT_RECORD_TIME = 60;//30s
		public static final int SIP_NEGATIVE_PREF = 1000;
		public static final int DEFAULT_SIP_FRAME_WIDTH = 640;
		public static final int DEFAULT_SIP_FRAME_HEIGHT = 480;
		public static final boolean DEFAULT_LOCAL_CAMERA_STATE = true;
	}
	
	/**
	 * sip对讲角色
	 */
	public static class SipRole
	{
		public static final int CENTER = 100; //中心机
		public static final int DOOR = 200; //门口机
		public static final int GATEWAY = 300; //室内机
		public static final int PHONE = 400; //手机
	}
	
	public static class SipCmds{
		public static final String SIP_DIAL = "dial";
		public static final String SIP_INCOME = "income";
		public static final String REG_STATE = "registerstate";
		public static final String CALL_STATE = "callstate";
		public static final String RECORD_STATE = "recordstate";
		public static final String USER_MSG = "usrmsg";
	}
				
	public static class SipMode
	{
		public static final String NORMAL = "normal";
		public static final String CENTER = "center";
		public static final String MONITOR = "monitor";
	}
	
	public static class SipResult
	{
		public static final int RegSuccess = 2;
		public static final int NotFound = 404;
		public static final int HangupNormal = 200;
		public static final int DeviceBusy = 486 ;
	}
		
	public static class SipMachineType
	{
		public static final String DOOR = "door";
		public static final String GATEWAY = "gateway";
		public static final String CENTER = "center";
		public static final String LOCALSERVER = "local_server";
		public static final String PUBLICSERVER = "public_server";
	}
	
	public static class BroadCastAction
	{
		public static final String ACTION_REG_ONDOOR = "com.action.regdoor";
		public static final String ACTION_UNREG_ONDOOR = "com.action.unregdoor";
		public static final String ACTION_PAUSE_MUSIC = "com.aite.action_pause_music";
		public static final String ACTION_MULTI_PHONE="com.action.multi.phone";
		public static final String ACTION_MODE_MAC = "com.action.mode_mac";
		public static final String ACTION_BRIDGE_PHONE = "com.action.bridge_phone";
	}
	
	public static class SipKey
	{
		public static final String KeyDoorSip = "key_door_sip";
		public static final String DoorDataType = "door_data_type";
		public static final String DoorSingle = "door_single";
		public static final String DoorList = "door_list";
		public static final String ModeMac = "mode_mac";		
		public static final String DeviceType = "device_type";
	}
	
	public static class TimeTickType
	{
		/**
		 * 留言时间
		 */
		public static final int RECORD_TIME = 0x1;
		
		/**
		 * 呼叫时间
		 */
		public static final int CALL_TIME = 0x2;
	}
	
	/**
	 * 自定义消息
	 */
	public static class Msg
	{
		public static final String USER_NOT_FOUND = "user_not_found";
		public static final String PARAMS_ERROR = "params_error";
		public static final String OPEN_DOOR = "door_open";
		public static final String OPEN_DOOR_SUCCESS = "open_door_success";
		public static final String MONITOR_PWD_ERROR = "monitor_pwd_error";
		public static final String BUSY = "busy";
		public static final String CLOSE_CAMERA = "camera_close";//关闭本地摄像头
	}
	
	public static class MsgCode
	{
		/**
		 * 对端不在线
		 */
		public static final int CODE_PEER_OFFLINE = 0x16000;
		/**
		 * 参数错误
		 */
		public static final int CODE_PARAM_ERROR = 0x16001;
		/**
		 * 通知对讲页面需要关闭
		 */
		public static final int CODE_NEED_FINISH = 0x16002;
		/**
		 * 用户消息，对端发送的消息
		 */
		public static final int CODE_USER_MESSAGE = 0x16003;
		/**
		 * 对方不存在
		 * */
		public static final int CODE_PEER_NOT_EXIST = 0x16004;
		/**
		 * 未注册到相关服务
		 * */
		public static final int CODE_NO_REGISTER_SERVER = 0x16005;
		/**
		 * 对方忙
		 * */
		public static final int CODE_DEVICE_BUSY = 0x16006;
	}
}
