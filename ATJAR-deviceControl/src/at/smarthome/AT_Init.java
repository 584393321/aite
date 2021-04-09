package at.smarthome;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.xhc.sbh.tool.lists.logcats.LogUitl;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author 孙波海
 * @file AT_Init.java
 * @date 2016年1月14日
 * @time 上午10:43:34
 * @describe 通讯初始化连接时，根据配置建立通讯，
 */
public class AT_Init {

	private final static String DIR_PATH = "/data/atshared";// /system/usr/atshared
	private final static String FILE_PATH = "/data/atshared/atinit";// /system/usr/atshared/atinit
	private final static String FILE_PATH_DATA = "/data/atshared/atdata";// /system/usr/atshared/atdata
//	private final static String DIR_PATH = "/mnt/sdcard/atshared";// /system/usr/atshared
//	private final static String FILE_PATH = "/mnt/sdcard/atshared/atinit";// /system/usr/atshared/atinit
	private static String serverAddress = null;
	private static String remoteControl = null;
	private static String serverType=null;
    private static String initMode=null;
    private static int data_length=0;
	public interface Mode {
		public final static String LOCALHOST = "localhost";
		public final static String CLIENT = "client";
		public final static String SERVER = "server";
		public final static String CLOUD_SERVER="cloud_server";
		public final static String CLOUD_CLIENT="cloud_client";
	}

	/**
	 * 默认配置
	 * 
	 * @return
	 */
	private static final JSONObject getDefaultConfig(String wifiMac) {
		JSONObject jsonO = new JSONObject();
		try {
			if(PropProperties.getPropertiesString("service.device.pid","0002").equals("0009"))
			{
				jsonO.put("server_address", wifiMac);
				jsonO.put("remote_control", "enable");
			}else {
				jsonO.put("server_address", "localhost");
				jsonO.put("remote_control", "disable");
			}
		} catch (Exception e) {

		}
		return jsonO;
	}

	/**
	 * 设置本机MAC
	 * 
	 * @param context
	 * @return
	 */
	public static final String setLocalMac(Context context) {
		WifiManager wifiMan = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		String mac = null;
		if (wifiMan.isWifiEnabled()) {
			try {
				WifiInfo info = wifiMan.getConnectionInfo();
				mac = info.getMacAddress();// 获得本机的MAC地址
				mac = mac.replace(":", "");
				LogUitl.d("mac===" + mac);
				File file = new File(DIR_PATH);
				if (!file.exists())
					file.mkdir();
				file = new File(FILE_PATH);
				if (!file.exists()) {
					LogUitl.d("setLocalMac==!file.exists()=");
					file.createNewFile();
					FileOutputStream out = new FileOutputStream(FILE_PATH);
					out.write(getDefaultConfig(mac).toString().getBytes());
					out.flush();
					out.close();
					String command = "chmod 777 " + FILE_PATH;
					Runtime runtime = Runtime.getRuntime();
					Process proc = runtime.exec(command);
				}
				FileInputStream input = new FileInputStream(FILE_PATH);
				byte[] res = new byte[2048];
				int length = input.read(res);
				if (length > 10) {
					JSONObject jsonO=null;
					String ss = new String(res, 0, length);
					try{
					 jsonO = new JSONObject(ss);
					jsonO.put("local_mac", mac);
					}catch(Exception e)
					{
						jsonO=getDefaultConfig(mac);
					}
					input.close();
					input = null;
					FileOutputStream out = new FileOutputStream(FILE_PATH);
					out.write(jsonO.toString().getBytes());
					out.flush();
					out.close();

				}else
				{
					FileOutputStream out = new FileOutputStream(FILE_PATH);
					out.write(getDefaultConfig(mac).toString().getBytes());
					out.flush();
					out.close();
					String command = "chmod 777 " + FILE_PATH;
					Runtime runtime = Runtime.getRuntime();
					Process proc = runtime.exec(command);
					LogUitl.d("setLocalMac==file.exists()=");

				}
			} catch (Exception e) {
                LogUitl.d(e.getMessage());
			}
		} else {
			wifiMan.setWifiEnabled(true);
		}
		wifiMan = null;
		return mac;
	}
    
	/**
	 * 获取本机MAC
	 * 
	 * @return
	 */
	public static final String getLocalMac() {
		String mac = null;
		try {
            File file=new File(FILE_PATH);
            if(file.exists()) {
				FileInputStream input = new FileInputStream(FILE_PATH);
				byte[] res = new byte[2048];
				int length = input.read(res);
				if (length > 0) {
					String ss = new String(res, 0, length);
					JSONObject jsonO = new JSONObject(ss);
					mac = jsonO.getString("local_mac");
					input.close();
					input = null;

				}
			}
		} catch (Exception e) {
LogUitl.d(e.getMessage());
		}
        LogUitl.d("getLocalMac===="+mac);
		return mac;
	}

	/**
	 * 当为客户端模式时，获取连接服务端的地址
	 * 
	 * @return
	 */
	public static final String getServerAddress() {
		if (serverAddress != null)
			return serverAddress;

		FileInputStream input = null;
		try {
			input = new FileInputStream(FILE_PATH);
			byte[] res = new byte[2048];
			int length = input.read(res);
			if (length > 0) {
				String ss = new String(res, 0, length);
				JSONObject jsonO = new JSONObject(ss);
				serverAddress = jsonO.getString("server_address");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUitl.d(e.getMessage());
		}
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return serverAddress = serverAddress == null ? "localhost" : serverAddress;
	}
	/**
	 * 当为客户端模式时，获取连接服务端的地址
	 * 
	 * @return
	 */
	public static final String getServerType() {
		if (serverType != null)
			return serverType;

		FileInputStream input = null;
		try {
			input = new FileInputStream(FILE_PATH);
			byte[] res = new byte[2048];
			int length = input.read(res);
			if (length > 0) {
				String ss = new String(res, 0, length);
				JSONObject jsonO = new JSONObject(ss);
				serverType = jsonO.has("server_type")?jsonO.getString("server_type"):"gateway";
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUitl.d(e.getMessage());
		}
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return serverType = serverType == null ? "gateway" : serverType;
	}

	/**
	 * 服务端模式时，获取远程控制支持是不是允许 enable disable
	 * 
	 * @return
	 */
	public static final String getRemoteControlMode() {
		if (remoteControl != null)
			return remoteControl;
		FileInputStream input = null;
		try {
			input = new FileInputStream(FILE_PATH);
			byte[] res = new byte[2048];
			int length = input.read(res);
			if (length > 0) {
				String ss = new String(res, 0, length);
				JSONObject jsonO = new JSONObject(ss);
				remoteControl = jsonO.getString("remote_control");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUitl.d(e.getMessage());
		}
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return remoteControl = remoteControl == null ? "disable" : remoteControl;
	}

	/**
	 * 获取当前模式，出厂默认为本地模式
	 * 
	 * @return localhost 本地模式 client 主分模式的客户端模式 server主分模式的服务端模式
	 */
	public static final String getInitMode(Context context) {
		if(initMode!=null)
		{
			return initMode;
		}
		String	result = Mode.LOCALHOST;
		FileInputStream input = null;
		try {
			input = new FileInputStream(FILE_PATH);
			byte[] res = new byte[2048];
			int length = input.read(res);
			if (length > 0) {
				String ss = new String(res, 0, length);
				JSONObject jsonO = new JSONObject(ss);
				String remote_control = jsonO.getString("remote_control");
				String server_address = jsonO.getString("server_address");
				String mode=jsonO.has("mode")?jsonO.getString("mode"):null;
				if(mode!=null&&mode.length()>0)
				{
					initMode=mode;
                    result=mode;
				}else {
					if (remote_control.equals("enable")) {
						result = Mode.SERVER;
					} else if (server_address.equals("localhost")
							|| server_address.equals(IPAddressUtil.getMacAddressLowerCase(context))) {
						result = Mode.LOCALHOST;
					} else {
						result = Mode.CLIENT;
					}
					initMode = result;
				}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUitl.d(e.getMessage());
		}
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(PropProperties.getPropertiesString("service.device.pid","0002").equals("0009"))
		{//室内机没有service.device.pid,所以要默认为0002，0002是室内机
		    result=Mode.SERVER;
		}else {
			result = result == null ? "localhost" : result;
		}
		return result;
	}

	public static final int getDataLength() {
		if(data_length!=0)
		{
			return data_length;
		}
		int result=4;
		FileInputStream input = null;
		try{
		File file = new File(FILE_PATH);		
		if (file.exists())
		{
			file = null;
			input = new FileInputStream(FILE_PATH);
			byte[] res = new byte[2048];
			int length = input.read(res);
			if (length > 0) {
				String ss = new String(res, 0, length);
				JSONObject jsonO = new JSONObject(ss);
				data_length=jsonO.has("data_length")?jsonO.getInt("data_length"):4;
				result=data_length;
				input.close();
				input = null;
			}
		}
		}catch(Exception e)
		{
			
		}
		return result;
	}
	/**
	 * 设置数据长度
	 * 
	 * @return true 成功 false 失败
	 */
	public static final boolean setDataLength(int dataLength) {
		boolean result = false;
		FileInputStream input = null;
		try {
			File file = new File(DIR_PATH);
			if (!file.exists())
				file.mkdir();
			file = new File(FILE_PATH);
			if (!file.exists())
				file.createNewFile();
			file = null;
			input = new FileInputStream(FILE_PATH);
			byte[] res = new byte[2048];
			int length = input.read(res);
			if (length > 0) {
				String ss = new String(res, 0, length);
				JSONObject jsonO = new JSONObject(ss);
				jsonO.put("data_length", dataLength);
				input.close();
				input = null;
				FileOutputStream out = new FileOutputStream(FILE_PATH);
				out.write(jsonO.toString().getBytes());
				out.flush();
				out.close();
				result = true;
			}else
			{
				JSONObject jsonO = new JSONObject();
				jsonO.put("data_length", dataLength);
				input.close();
				input = null;
				FileOutputStream out = new FileOutputStream(FILE_PATH);
				out.write(jsonO.toString().getBytes());
				out.flush();
				out.close();
				result = true;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUitl.d(e.getMessage());
			result = false;
		}
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	/**
	 * 设置为本地模式
	 * 
	 * @return true 成功 false 失败
	 */
	public static final boolean setInitModeLocalHost() {
		boolean result = false;
		FileInputStream input = null;
		try {
			File file = new File(DIR_PATH);
			if (!file.exists())
				file.mkdir();
			file = new File(FILE_PATH);
			if (!file.exists())
				file.createNewFile();
			file = null;
			input = new FileInputStream(FILE_PATH);
			byte[] res = new byte[2048];
			int length = input.read(res);
			if (length > 0) {
				String ss = new String(res, 0, length);
				JSONObject jsonO = new JSONObject(ss);
				jsonO.put("server_address", "localhost");
				jsonO.put("remote_control", "disable");
				jsonO.put("mode",Mode.LOCALHOST);
				input.close();
				input = null;
				FileOutputStream out = new FileOutputStream(FILE_PATH);
				out.write(jsonO.toString().getBytes());
				out.flush();
				out.close();
				result = true;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUitl.d(e.getMessage());
			result = false;
		}
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 设置为主分模式的服务端模式
	 * 
	 * @return true 成功 false 失败
	 */
	public static final boolean setInitModeServer(Context context) {
		boolean result = false;
		FileInputStream input = null;
		try {
			File file = new File(DIR_PATH);
			if (!file.exists())
				file.mkdir();
			file = new File(FILE_PATH);
			if (!file.exists())
			{
				file.createNewFile();
				LogUitl.d("setInitModeServer !file.exists==========");
			}
			file = null;
			input = new FileInputStream(FILE_PATH);
			byte[] res = new byte[2048];
			int length = input.read(res);
			LogUitl.d("setInitModeServer !file.exists=========="+length);
			if (length > 0) {
				String ss = new String(res, 0, length);
				JSONObject jsonO = new JSONObject(ss);
				//jsonO.put("server_address", IPAddressUtil.getMacAddressLowerCase(context));
				jsonO.put("server_address", "127.0.0.1");
				jsonO.put("remote_control", "enable");
				jsonO.put("mode",Mode.SERVER);
				input.close();
				input = null;
				FileOutputStream out = new FileOutputStream(FILE_PATH);
				out.write(jsonO.toString().getBytes());
				out.flush();
				out.close();
				result = true;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUitl.d("setInitModeServer=error=="+e.getMessage());
			result = false;
		}
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	public static final boolean setValue(Context context,String key,String value) {
		boolean result = false;
		FileInputStream input = null;
		try {
			File file = new File(DIR_PATH);
			if (!file.exists())
				file.mkdir();
			file = new File(FILE_PATH_DATA);
			if (!file.exists())
			{
				file.createNewFile();
				String command = "chmod 777 " + FILE_PATH_DATA;
				Runtime runtime = Runtime.getRuntime();
				Process proc = runtime.exec(command);
			}
			file = null;
			input = new FileInputStream(FILE_PATH_DATA);
			byte[] res = new byte[2048];
			int length = input.read(res);
			LogUitl.d("setInitModeServer !file.exists=========="+length);
			if (length > 0) {
				String ss = new String(res, 0, length);
				JSONObject jsonO = new JSONObject(ss);
				//jsonO.put("server_address", IPAddressUtil.getMacAddressLowerCase(context));
				jsonO.put(key, value);
				input.close();
				input = null;
				FileOutputStream out = new FileOutputStream(FILE_PATH_DATA);
				out.write(jsonO.toString().getBytes());
				out.flush();
				out.close();
				result = true;
			}else
			{
				JSONObject jsonO = new JSONObject();
				jsonO.put(key, value);
				FileOutputStream out = new FileOutputStream(FILE_PATH_DATA);
				out.write(jsonO.toString().getBytes());
				out.flush();
				out.close();
				result = true;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUitl.d("setInitModeServer=error=="+e.getMessage());
			result = false;
		}
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	public static final String getValue(Context context,String key) {
		String result = null;
		FileInputStream input = null;
		try {
			File file = new File(DIR_PATH);
			if (!file.exists())
				file.mkdir();
			file = new File(FILE_PATH_DATA);
			if (!file.exists())
			{
				file=null;
				return result;
			}
			input = new FileInputStream(FILE_PATH_DATA);
			byte[] res = new byte[2048];
			int length = input.read(res);
			LogUitl.d("setInitModeServer !file.exists=========="+length);
			if (length > 0) {
				String ss = new String(res, 0, length);
				JSONObject jsonO = new JSONObject(ss);
				//jsonO.put("server_address", IPAddressUtil.getMacAddressLowerCase(context));
				result=jsonO.getString(key);
				input.close();
				input = null;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUitl.d("setInitModeServer=error=="+e.getMessage());
		}
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	/**
	 * 设置为主分模式的客户端模式
	 * 
	 * @return true 成功 false 失败
	 */
	public static final boolean setInitModeClient(String serverAddress, Context context) {

		boolean result = false;
		if (serverAddress == null) {
			return result;
		} else if (serverAddress.length() <8) {
			return result;
		}
		Pattern pa = Pattern.compile(
				"^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])$");
		Matcher mat = pa.matcher(serverAddress);

		if (!mat.matches()) {
			Log.d("test", "!mat.matches()   ");
			if (serverAddress.equals(IPAddressUtil.getMacAddressLowerCase(context))) {
				Log.d("test", "!mat.matches()   xx==" + IPAddressUtil.getMacAddressLowerCase(context));
				// 不可为本机MAC
				return result;
			}
		} else {
			// 确保IP不是为本机的
		}
		FileInputStream input = null;

		try {
			File file = new File(DIR_PATH);
			if (!file.exists())
				file.mkdir();
			file = new File(FILE_PATH);
			if (!file.exists())
				file.createNewFile();
			file = null;
			input = new FileInputStream(FILE_PATH);
			byte[] res = new byte[2048];
			int length = input.read(res);
			if (length > 0) {
				String ss = new String(res, 0, length);
				JSONObject jsonO = new JSONObject(ss);
				jsonO.put("server_address", serverAddress);
				jsonO.put("server_type", "gateway");
				jsonO.put("remote_control", "disable");
				jsonO.put("mode",Mode.CLIENT);
				input.close();
				input = null;
				FileOutputStream out = new FileOutputStream(FILE_PATH);
				out.write(jsonO.toString().getBytes());
				out.flush();
				out.close();
				result = true;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("test", e.getMessage());
			result = false;
		}
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	/**
	 * 设置为主分模式的客户端模式
	 * 
	 * @return true 成功 false 失败
	 */
	public static final boolean setInitModeClient(String serverAddress, Context context,String serverType) {

		boolean result = false;
		if (serverAddress == null) {
			return result;
		} else if (serverAddress.length() <8) {
			return result;
		}
		Pattern pa = Pattern.compile(
				"^(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d{1,2}|1\\d\\d|2[0-4]\\d|25[0-5])$");
		Matcher mat = pa.matcher(serverAddress);

		if (!mat.matches()) {
			Log.d("test", "!mat.matches()   ");
			if (serverAddress.equals(IPAddressUtil.getMacAddressLowerCase(context))) {
				Log.d("test", "!mat.matches()   xx==" + IPAddressUtil.getMacAddressLowerCase(context));
				// 不可为本机MAC
				return result;
			}
		} else {
			// 确保IP不是为本机的
		}
		FileInputStream input = null;
		try {
			File file = new File(DIR_PATH);
			if (!file.exists())
				file.mkdir();
			file = new File(FILE_PATH);
			if (!file.exists())
				file.createNewFile();
			file = null;
			input = new FileInputStream(FILE_PATH);
			byte[] res = new byte[2048];
			int length = input.read(res);
			if (length > 0) {
				String ss = new String(res, 0, length);
				JSONObject jsonO = new JSONObject(ss);
				jsonO.put("server_address", serverAddress);
				jsonO.put("remote_control", "disable");
				jsonO.put("server_type", serverType);
				jsonO.put("mode",Mode.CLIENT);
				input.close();
				input = null;
				FileOutputStream out = new FileOutputStream(FILE_PATH);
				out.write(jsonO.toString().getBytes());
				out.flush();
				out.close();
				result = true;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("test", e.getMessage());
			result = false;
		}
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	/**2017-8-14新增
	 * 设置为主分模式的客户端模式
	 *
	 * @return true 成功 false 失败
	 */
	public static final boolean setInitModeCloudServer(String cloudStr, Context context,String serverType) {

		boolean result = false;
		if (cloudStr == null) {
			return result;
		} else if (cloudStr.length() <4) {
			return result;
		}
		Pattern pa = Pattern.compile("^[A-Za-z0-9]+$");
		Matcher mat = pa.matcher(cloudStr);
		if (!mat.matches()) {
			Log.d("test", "!mat.matches()   ");
			return result;
		} else {
			// 确保是数据或字母
		}
		FileInputStream input = null;
		try {
			File file = new File(DIR_PATH);
			if (!file.exists())
				file.mkdir();
			file = new File(FILE_PATH);
			if (!file.exists())
				file.createNewFile();
			file = null;
			input = new FileInputStream(FILE_PATH);
			byte[] res = new byte[2048];
			int length = input.read(res);
			if (length > 0) {
				String ss = new String(res, 0, length);
				JSONObject jsonO = new JSONObject(ss);
				jsonO.put("server_address", cloudStr);
				jsonO.put("remote_control", "enable");
				jsonO.put("server_type", serverType);
				jsonO.put("mode",Mode.CLOUD_SERVER);
				input.close();
				input = null;
				FileOutputStream out = new FileOutputStream(FILE_PATH);
				out.write(jsonO.toString().getBytes());
				out.flush();
				out.close();
				result = true;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("test", e.getMessage());
			result = false;
		}
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	/**2017-8-14新增
	 * 设置为主分模式的客户端模式
	 *
	 * @return true 成功 false 失败
	 */
	public static final boolean setInitModeCloudClient(String cloudStr, Context context,String serverType) {

		boolean result = false;
		if (cloudStr == null) {
			return result;
		} else if (cloudStr.length() <4) {
			return result;
		}
		Pattern pa = Pattern.compile("^[A-Za-z0-9]+$");
		Matcher mat = pa.matcher(cloudStr);
		if (!mat.matches()) {
			Log.d("test", "!mat.matches()   ");
			return result;
		} else {
			// 确保是数据或字母
		}
		FileInputStream input = null;
		try {
			File file = new File(DIR_PATH);
			if (!file.exists())
				file.mkdir();
			file = new File(FILE_PATH);
			if (!file.exists())
				file.createNewFile();
			file = null;
			input = new FileInputStream(FILE_PATH);
			byte[] res = new byte[2048];
			int length = input.read(res);
			if (length > 0) {
				String ss = new String(res, 0, length);
				JSONObject jsonO = new JSONObject(ss);
				jsonO.put("server_address", cloudStr);
				jsonO.put("remote_control", "enable");
				jsonO.put("server_type", serverType);
				jsonO.put("mode",Mode.CLOUD_CLIENT);
				input.close();
				input = null;
				FileOutputStream out = new FileOutputStream(FILE_PATH);
				out.write(jsonO.toString().getBytes());
				out.flush();
				out.close();
				result = true;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("test", e.getMessage());
			result = false;
		}
		if (input != null) {
			try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
}
