package at.smarthome;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 设备控制,根据设备类型传入参数得到控制命令
 * 不能随便修改
 * @author CHT
 *
 */
public class AT_DeviceControl {

	static {
		System.loadLibrary("atsmarthome");
	}
	//非调光灯
	public static String getDevControlCmdByLight(String role,String account, String roomName,
			String devName, String cmd,int dev_id) {
		return getCmdByLight(role, account, roomName, devName, cmd,dev_id);
	}

	private static native String getCmdByLight(String role, String account,
			String roomName, String devName, String cmd,int dev_id);
    //可调光
	public static String getDevControlCmdByDimmer(String role,String account, String roomName,
			String devName, String cmd,int dev_id, int value) {
		return getCmdByDimmer(role, account, roomName, devName, cmd,dev_id, value);
	}

	private static native String getCmdByDimmer(String role, String account,
			String roomName, String devName, String cmd,int dev_id, int value);
    //窗帘
	public static String getDevControlCmdByCurtain(String role,String account, String roomName,
			String devName, String cmd,int dev_id) {
		return getCmdByCurtain(role, account, roomName, devName, cmd,dev_id);
	}

	private static native String getCmdByCurtain(String role, String account,
			String roomName, String devName, String cmd,int dev_id);
    //插座
	public static String getDevControlCmdBySocket(String role,String account, String roomName,
			String devName, String cmd,int dev_id) {
		return getCmdBySocket(role, account, roomName, devName, cmd,dev_id);
	}

	private static native String getCmdBySocket(String role, String account,
			String roomName, String devName, String cmd,int dev_id);
	//温控器
	public static String getDevControlCmdByTemperature(String role,String account, String roomName,
			String devName, String cmd,int dev_id, int value) {
		return getCmdByTemperature(role, account, roomName, devName, cmd,dev_id, value);
	}

	private static native String getCmdByTemperature(String role,
			String account, String roomName, String devName, String cmd,int dev_id,
			int value);
	//新风
	public static String getDevControlCmdByFreshAirSystem(String role,String account, String roomName,
			String devName, String cmd,int dev_id, int value) {
		return getCmdByFreshAirSystem(role, account, roomName, devName, cmd,dev_id,value);
	}

	private static native String getCmdByFreshAirSystem(String role,
			String account, String roomName, String devName, String cmd,int dev_id,int value);
	//地暖
	public static String getDevControlCmdByFloorWarm(String role,String account, String roomName,
			String devName, String cmd,int dev_id, int value) {
		return getCmdByFreshAirSystem(role, account, roomName, devName, cmd,dev_id,value);
	}
    //底层未实现
	private static native String getCmdByFloorWarm(String role,
			String account, String roomName, String devName, String cmd,int dev_id,int value);
	//电视
	public static String getDevControlCmdByTV(String role,String account, String roomName,
			String devName, String cmd,int dev_id, int value) {
		return getCmdByTV(role, account, roomName, devName, cmd, dev_id,value);
	}
	
	private static native String getCmdByTV(String role, String account,
			String roomName, String devName, String cmd,int dev_id, int value);
	//空调
	public static String getDevControlCmdByAircondition(String role,String account, String roomName,
			String devName, String cmd,int dev_id, int value) {
		return getCmdByAircondition(role, account, roomName, devName, cmd,dev_id, value);
	}
	private static native String getCmdByAircondition(String role,
			String account, String roomName, String devName, String cmd,int dev_id,
			int value);
	//DVD
	public static String getDevControlCmdByDVD(String role,String account, String roomName,
			String devName, String cmd,int dev_id) {
		return getCmdByDVD(role, account, roomName, devName, cmd,dev_id);
	}
	private static native String getCmdByDVD(String role, String account,
			String roomName, String devName, String cmd,int dev_id);
	//功放
		public static String getDevControlCmdByAmplifier(String role,String account, String roomName,
				String devName, String cmd,int dev_id) {
			return getCmdByDVD(role, account, roomName, devName, cmd,dev_id);
		}
		private static native String getCmdByAmplifier(String role, String account,
				String roomName, String devName, String cmd,int dev_id);
    //背景音乐
		
	/*private static String getDevControlCmdByAudio(String role,String account,
			String roomName, String devName, String cmd,int dev_id, int play_mode,
			int audio_src_id, int song_id,int value)
	{
		return getCmdByAudio(role,account, roomName, devName, cmd,dev_id, play_mode, audio_src_id, song_id,value);
	}*/
	private static native String getCmdByAudio(String role, String account,
			String roomName, String devName, String cmd,int dev_id, int play_mode,
			int audio_src_id, int song_id,int value);
//面板
	public static String getDevControlCmdByPanel(String role,String account,
			String roomName, String devName, int dev_key,int dev_id)
	{
		return getCmdByPanel(role,account, roomName, devName, dev_key,dev_id);
	}
	private static native String getCmdByPanel(String role, String account,
			String roomName, String devName, int dev_key,int dev_id);
}
