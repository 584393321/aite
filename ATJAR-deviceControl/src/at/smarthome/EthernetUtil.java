package at.smarthome;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.util.Log;

import com.xhc.sbh.tool.lists.logcats.LogUitl;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class EthernetUtil {
	/*
	 * public void getEthInfoFromDhcp(){ EthernetManager mEthManager;
	 * mEthManager = (EthernetManager)
	 * getSystemService(Context.ETHERNET_SERVICE); String tempIpInfo; String
	 * iface = mEthManager.getEthernetIfaceName();
	 * 
	 * tempIpInfo = SystemProperties.get("dhcp."+ iface +".ipaddress"); if
	 * ((tempIpInfo != null) && (!tempIpInfo.equals("")) ){ mEthIpAddress =
	 * tempIpInfo; } else { mEthIpAddress = nullIpInfo; }
	 * 
	 * tempIpInfo = SystemProperties.get("dhcp."+ iface +".mask"); if
	 * ((tempIpInfo != null) && (!tempIpInfo.equals("")) ){ mEthNetmask =
	 * tempIpInfo; } else { mEthNetmask = nullIpInfo; }
	 * 
	 * tempIpInfo = SystemProperties.get("dhcp."+ iface +".gateway"); if
	 * ((tempIpInfo != null) && (!tempIpInfo.equals(""))){ mEthGateway =
	 * tempIpInfo; } else { mEthGateway = nullIpInfo; }
	 * 
	 * tempIpInfo = SystemProperties.get("dhcp."+ iface +".dns1"); if
	 * ((tempIpInfo != null) && (!tempIpInfo.equals(""))){ mEthdns1 =
	 * tempIpInfo; } else { mEthdns1 = nullIpInfo; }
	 * 
	 * tempIpInfo = SystemProperties.get("dhcp."+ iface +".dns2"); if
	 * ((tempIpInfo != null) && (!tempIpInfo.equals(""))){ mEthdns2 =
	 * tempIpInfo; } else { mEthdns2 = nullIpInfo; } }
	 */
	static DatagramSocket rootsocket = null;

	public synchronized static void rootcmd(final String cmd) {
		new Thread() {
			public void run() {
				try {
					if (rootsocket == null)
						rootsocket = new DatagramSocket();
					InetAddress serverAddress;
					serverAddress = InetAddress.getByName("127.0.0.1");
					byte data[] = cmd.getBytes();
					DatagramPacket dp = new DatagramPacket(data, data.length, serverAddress, 5555);
					LogUitl.d("cmd=====" + cmd);
					rootsocket.send(dp);
				} catch (SocketException e) {
				} catch (UnknownHostException e) {
				} catch (IOException e) {
				}
			};
		}.start();
	}
	public synchronized static void rootcmd(final ArrayList<String>cmds) {

				try {
					if (rootsocket == null)
						rootsocket = new DatagramSocket();
					InetAddress serverAddress;
					serverAddress = InetAddress.getByName("127.0.0.1");
					for (String cmd:cmds) {
						byte data[] = cmd.getBytes();
						DatagramPacket dp = new DatagramPacket(data, data.length, serverAddress, 5555);
						LogUitl.d("cmd=====" + cmd);
						rootsocket.send(dp);
						Thread.sleep(500);
					}
				} catch (SocketException e) {
				} catch (UnknownHostException e) {
				} catch (Exception e) {
				}


	}
	/*
	 * 设置以太网，cmd参数如下： 静态IP:
	 * {"ipaddr":"192.168.100.100","maskip":"255.255.255.0","gateway":
	 * "192.168.100.1","isstatic":"on"} 动态ip：
	 * {"ipaddr":"192.168.100.100","maskip":"255.255.255.0","gateway":
	 * "192.168.100.1","isstatic":"off"}
	 * 
	 * 获取以太网: 读取/data/misc/ethernet/config文件
	 */
	/**
	 * 获取以太网当前的状态
	 * 
	 * @return false 禁用 true 开启
	 */
	public static boolean getEthernetState(Context context) {
		// {"isenable":"off","ipaddr":"192.168.100.222","maskip":"255.255.255.0","gateway":"192.168.100.1","isstatic":"on"}
		if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 23) {
			File file = new File("data/misc/ethernet/config");
			boolean isenable = false;
			if (file.exists()) {
				byte[] content = new byte[2048];
				FileInputStream input = null;
				try {
					input = new FileInputStream(file);
					int len = input.read(content);
					String res = new String(content, 0, len);
					JSONObject json = new JSONObject(res);
					if (json.getString("isenable").equals("on")) {
						isenable = true;
					}
				} catch (Exception e) {

				} finally {
					if (input != null) {
						try {
							input.close();
						} catch (Exception w) {

						}
						input = null;
					}
					file = null;
				}
			}
			return isenable;
		}else if(Integer.parseInt(android.os.Build.VERSION.SDK) >=22){
			return EthernetUtil51.getEthernetState(context);
		} else {
			return Settings.Secure.getInt(context.getContentResolver(), "ethernet_on", 1) == 1 ? true : false;
		}
	}

	/**
	 * 关闭以太网
	 * 
	 * @return false 执行失败 、 true 执行成功
	 */
	public static boolean EthernetDisable(Context context) {
		boolean result = false;
		if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 23) {
			Log.d("network","11111111111111111111111111111111111");
			File file = new File("data/misc/ethernet/config");
			if (file.exists()) {
				Log.d("network","333333333333333333");
				byte[] content = new byte[2048];
				FileInputStream input = null;
				try {
					input = new FileInputStream(file);
					int len = input.read(content);
					String res = new String(content, 0, len);
					Log.d("network","44444444444444444444444444");
					JSONObject json = new JSONObject(res);
					if (!json.has("ipaddr"))
						json.put("ipaddr", "");
					if (!json.has("maskip"))
						json.put("maskip", "");
					if (!json.has("maskip"))
						json.put("gateway", "");
					if (!json.has("isstatic"))
						json.put("isstatic", "off");
					json.put("isenable", "off");
					rootcmd(json.toString());
					result = true;
				} catch (Exception e) {
					Log.d("network","exception =="+e.getMessage());
				} finally {
					Log.d("network","555555555555555555555555555555");
					if (input != null) {
						try {
							input.close();
						} catch (Exception w) {

						}
						input = null;
					}
					file = null;
				}
			}
			return result;
		}else if(Integer.parseInt(android.os.Build.VERSION.SDK) >=22){
			return EthernetUtil51.setEthernetSwitch(context,false);
		} else {
			Log.d("network","222222222222222222222222222222222222");
			result = setEthernet(context, false);
			//Secure.putInt(context.getContentResolver(), "ethernet_on", 0);
		}
		return result;
	}

	/**
	 * 打开以太网
	 * 
	 * @return false 执行失败 、 true 执行成功
	 */
	public static boolean EthernetEnable(Context context) {
		boolean result = false;
		if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 23) {
			Log.d("network","11111111111111111111111111111111111");
			File file = new File("data/misc/ethernet/config");
			if (file.exists()) {
				Log.d("network","333333333333333333");
				byte[] content = new byte[2048];
				FileInputStream input = null;
				try {
					input = new FileInputStream(file);
					int len = input.read(content);
					Log.d("network","444444444444444444444444444444");
					String res = new String(content, 0, len);
					JSONObject json = new JSONObject(res);
					if (!json.has("ipaddr"))
						json.put("ipaddr", "");
					if (!json.has("maskip"))
						json.put("maskip", "");
					if (!json.has("maskip"))
						json.put("gateway", "");
					if (!json.has("isstatic"))
						json.put("isstatic", "off");
					json.put("isenable", "on");
					rootcmd(json.toString());
					result = true;
				} catch (Exception e) {
					Log.d("network","exception =="+e.getMessage());
				} finally {
					if (input != null) {
						try {
							input.close();
						} catch (Exception w) {

						}
						input = null;
					}
					file = null;
				}
			}
			return result;
		}else if(Integer.parseInt(android.os.Build.VERSION.SDK) >=22){
			return EthernetUtil51.setEthernetSwitch(context,true);
		} else {
			Log.d("network","22222222222222222222222222222");
			//EthernetManager mEthManager = (EthernetManager) getSystemService(Context.ETHERNET_SERVICE);
			//Secure.putInt(context.getContentResolver(), "ethernet_on", 1);
			result = setEthernet(context, true);
		}
		return result;
	}
	/**
	 * 反射打开以态网和关闭以态网
	 * 
	 * @return dhcp/static/unknown
	 */
	public static boolean setEthernet(Context context,boolean bool) {
		boolean result = false;
		try {
			//获取ETHERNET_SERVICE参数  
            String ETHERNET_SERVICE = (String) Context.class.getField("ETHERNET_SERVICE").get(null);                
            Class<?> ethernetManagerClass = Class.forName("android.net.ethernet.EthernetManager");  
            //获取ethernetManager服务对象  
            Object ethernetManager =context.getSystemService(ETHERNET_SERVICE); 
            //获取在EthernetManager中的抽象类mService成员变量  
            Field mService = ethernetManagerClass.getDeclaredField("mService");                
            //修改private权限  
            mService.setAccessible(true);                
            //获取抽象类的实例化对象  
            Object mServiceObject = mService.get(ethernetManager);                
            Class<?> iEthernetManagerClass = Class.forName("android.net.ethernet.IEthernetManager");                
            Method[] methods = iEthernetManagerClass.getDeclaredMethods();                
            for (Method ms : methods) {

                if (ms.getName().equals("setEthernetEnabled")){  
                	result=(Boolean)ms.invoke(mServiceObject,bool);  
                    break;
                }  
                  
            }  
		} catch (InvocationTargetException e) {
			// 被调用方出异常
			LogUitl.d("InvocationTargetException==" + e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			LogUitl.d("ClassNotFoundException==" + e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d("network","setEthernet =="+result);
		return result;
	}
	/**
	 * 获取以太网模式
	 * 
	 * @return dhcp/static/unknown
	 */
	public static String EthernetMode(Context context) {
		String result = "unknown";
		if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 23) {
			File file = new File("data/misc/ethernet/config");
			//boolean isenable = false;
			if (file.exists()) {
				byte[] content = new byte[2048];
				FileInputStream input = null;
				try {
					input = new FileInputStream(file);
					int len = input.read(content);
					String res = new String(content, 0, len);
					JSONObject json = new JSONObject(res);
					if (json.getString("isstatic").equals("on")) {
						result = "static";
					}else if (json.getString("isstatic").equals("off")) {
						result = "dhcp";
					}
				} catch (Exception e) {

				} finally {
					if (input != null) {
						try {
							input.close();
						} catch (Exception w) {

						}
						input = null;
					}
					file = null;
				}
			}
		}else if(Integer.parseInt(android.os.Build.VERSION.SDK) >=22){
			return EthernetUtil51.getEthernetMode(context);
		} else {
		result = Settings.System.getInt(context.getContentResolver(), "ethernet_use_static_ip", 0) == 1 ? "static"
				: "dhcp";
		}
		return result;
	}

	/**
	 * 设置以太网IP为动态获取
	 * 
	 * @return false 执行失败 、 true 执行成功
	 */
	public static boolean EthernetModeDHCP(Context context) {
		if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 23) {
			try {
				JSONObject jsonO = new JSONObject();
				jsonO.put("ipaddr", "");
				jsonO.put("maskip", "");
				jsonO.put("gateway", "");
				jsonO.put("isstatic", "off");
				if (!jsonO.has("isenable"))
					jsonO.put("isenable", "on");				
				rootcmd(jsonO.toString());
				return true;
			} catch (Exception e) {
			}

			return false;
		}else if(Integer.parseInt(android.os.Build.VERSION.SDK) >=22){
			return EthernetUtil51.setEthernetModeDHCP(context);
		} else {
			android.provider.Settings.System.putInt(context.getContentResolver(), "ethernet_use_static_ip", 0);
			boolean enable = Secure.getInt(context.getContentResolver(), "ethernet_on", 1) == 1;
			if (enable) {

				Secure.putInt(context.getContentResolver(), "ethernet_on", 0);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}

				Secure.putInt(context.getContentResolver(), "ethernet_on", 1);
			}else
			{
				Secure.putInt(context.getContentResolver(), "ethernet_on", 1);
			}
			return true;
		}
	}

	/**
	 * 设置以太网IP为静态
	 * 
	 * @return false 执行失败 、 true 执行成功
	 */
	public static boolean EthernetModeStatic(Context context, String mEthIpAddress, String mEthNetmask,
			String mEthGateway, String mEthdns1, String mEthdns2) {
		LogUitl.d("android.os.Build.VERSION.SDK===" + android.os.Build.VERSION.SDK);
		if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 23) {
			try {
				JSONObject jsonO = new JSONObject();
				jsonO.put("ipaddr", mEthIpAddress);
				jsonO.put("maskip", mEthNetmask);
				jsonO.put("gateway", mEthGateway);
				jsonO.put("isstatic", "on");
				if (!jsonO.has("isenable"))
					jsonO.put("isenable", "on");
				rootcmd(jsonO.toString());
				if(mEthIpAddress!=null&&mEthIpAddress.length()>7)
				{
					LogUitl.d("save static ip");
				context.getSharedPreferences("ethernetInfo",0).edit().putString("ipaddr", mEthIpAddress)
				.putString("maskip", mEthNetmask).putString("gateway",mEthGateway ).commit();
				}
				return true;
			} catch (Exception e) {
              LogUitl.d(e.getMessage());
			}
			return false;
		}else if(Integer.parseInt(android.os.Build.VERSION.SDK) >=22){
			return EthernetUtil51.setStaticIpConfiguration(context,mEthIpAddress,mEthNetmask,mEthGateway,mEthdns1,mEthdns2);
		} else {
			ContentResolver mcContentResolver = context.getContentResolver();
			Settings.System.putString(mcContentResolver, "ethernet_static_ip", mEthIpAddress);
			Settings.System.putString(mcContentResolver, "ethernet_static_gateway", mEthGateway);
			Settings.System.putString(mcContentResolver, "ethernet_static_netmask", mEthNetmask);
			Settings.System.putString(mcContentResolver, "ethernet_static_dns1", mEthdns1);
			Settings.System.putString(mcContentResolver, "ethernet_static_dns2", mEthdns2);
			Settings.System.putInt(mcContentResolver, "ethernet_use_static_ip", 1);
			boolean enable = Secure.getInt(context.getContentResolver(), "ethernet_on", 1) == 1;
			if (enable) {
				Secure.putInt(mcContentResolver, "ethernet_on", 0);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
				}
				Secure.putInt(mcContentResolver, "ethernet_on", 1);
			}
			return true;
		}
	}

}
