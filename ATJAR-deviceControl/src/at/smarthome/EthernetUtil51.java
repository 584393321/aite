package at.smarthome;

import android.annotation.SuppressLint;
import android.content.Context;

import com.xhc.sbh.tool.lists.logcats.LogUitl;

import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EthernetUtil51 {
	/**
	 * 获取以太网当前的状态
	 * 
	 * @return false 禁用 true 开启 public static final int ETH_STATE_UNKNOWN = 0;
	 *         public static final int ETH_STATE_DISABLED = 1; public static
	 *         final int ETH_STATE_ENABLED = 2;
	 */
	@SuppressLint("NewApi")
	public static boolean getEthernetState(Context context) {
		int result=0;
		try {
			Class<?> cls = Class.forName("android.net.EthernetATManager");
			Constructor c1=cls.getDeclaredConstructor(new Class[]{Context.class});
			c1.setAccessible(true);
			Object EthernetATManager=c1.newInstance(new Object[]{context});
			Method getEthernetEnabled = cls.getDeclaredMethod("getEthernetState");
			getEthernetEnabled.setAccessible(true);
			 result=(Integer) getEthernetEnabled.invoke(EthernetATManager);
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
		return result==0?false:true;
	}

	/**
	 * 以太网开关
	 * 
	 * @return false 执行失败 、 true 执行成功
	 */
	public static boolean setEthernetSwitch(Context context, boolean isSwitch) {
		boolean result = false;
		try {
			Class<?> cls = Class.forName("android.net.EthernetATManager");
			Constructor c1=cls.getDeclaredConstructor(new Class[]{Context.class});
			c1.setAccessible(true);
			Object EthernetATManager=c1.newInstance(new Object[]{context});
			Method getEthernetEnabled = cls.getDeclaredMethod("setEthernetEnabled", boolean.class);
			getEthernetEnabled.setAccessible(true);
			getEthernetEnabled.invoke(EthernetATManager, isSwitch);
			result = true;
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
		return result;
	}

	/**
	 * 获取以太网模式
	 * 
	 * @return dhcp/static/unknown
	 */
	public static String getEthernetMode(Context context) {
		String result = "unknown";
		try {

			Class<?> cls = Class.forName("android.net.EthernetATManager");
			Constructor c1=cls.getDeclaredConstructor(new Class[]{Context.class});
			c1.setAccessible(true);
			Object EthernetATManager=c1.newInstance(new Object[]{context});
			Method getEthernetEnabled = cls.getDeclaredMethod("isStatic");
			getEthernetEnabled.setAccessible(true);
			boolean bool = (Boolean) getEthernetEnabled.invoke(EthernetATManager);
			if (bool) {
				result = "dhcp";
			} else
			{
				result = "static";
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
		return result;
	}

	/**
	 * 设置以太网IP为动态获取
	 * 
	 * @return false 执行失败 、 true 执行成功
	 */
	public static boolean setEthernetModeDHCP(Context context) {
		boolean result = false;
		try {
			Class<?> cls = Class.forName("android.net.EthernetATManager");
			Constructor c1=cls.getDeclaredConstructor(new Class[]{Context.class});
			c1.setAccessible(true);
			Object EthernetATManager=c1.newInstance(new Object[]{context});
			Method getEthernetEnabled = cls.getDeclaredMethod("setDHCP");
			getEthernetEnabled.setAccessible(true);
			getEthernetEnabled.invoke(EthernetATManager);
            result=true;
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
		return result;
	}

	/**
	 * 设置以太网IP为静态
	 * 
	 * @return false 执行失败 、 true 执行成功
	 */
	public static boolean setStaticIpConfiguration(Context context, String mEthIpAddress, String mEthNetmask,
			String mEthGateway, String mEthdns1,String dns2) {
		boolean result = false;
		try {
			Class<?> cls = Class.forName("android.net.EthernetATManager");
			Constructor c1=cls.getDeclaredConstructor(new Class[]{Context.class});
			c1.setAccessible(true);
			Object EthernetATManager=c1.newInstance(new Object[]{context});
			Method getEthernetEnabled = cls.getDeclaredMethod("setStaticIpConfiguration",String.class,String.class,String.class,String.class,String.class);
			getEthernetEnabled.setAccessible(true);
			result = (Boolean) getEthernetEnabled.invoke(EthernetATManager,mEthIpAddress,mEthNetmask,mEthGateway,mEthdns1,dns2);
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
		return result;
	}
	public static JSONObject getEthInfoFromStaticIp(Context context) {
		JSONObject result=null ;
		try {
			Class<?> cls = Class.forName("android.net.EthernetATManager");
			Constructor c1=cls.getDeclaredConstructor(new Class[]{Context.class});
			c1.setAccessible(true);
			Object EthernetATManager=c1.newInstance(new Object[]{context});
			Method getEthernetEnabled = cls.getDeclaredMethod("getEthInfoFromStaticIp");
			getEthernetEnabled.setAccessible(true);
			result = (JSONObject) getEthernetEnabled.invoke(EthernetATManager);
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
		return result;
	}
	public static JSONObject getEthInfoFromDhcp(Context context) {
		JSONObject result=null ;
		try {
			Class<?> cls = Class.forName("android.net.EthernetATManager");
			Constructor c1=cls.getDeclaredConstructor(new Class[]{Context.class});
			c1.setAccessible(true);
			Object EthernetATManager=c1.newInstance(new Object[]{context});
			Method getEthernetEnabled = cls.getDeclaredMethod("getEthInfoFromDhcp");
			getEthernetEnabled.setAccessible(true);
			result = (JSONObject) getEthernetEnabled.invoke(EthernetATManager);
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
		return result;
	}
}
