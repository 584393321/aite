package at.smarthome;

import java.lang.reflect.Method;

import com.xhc.sbh.tool.lists.logcats.LogUitl;

public class PropProperties {
	/*
	 * private static native String native_get(String key); private static
	 * native String native_get(String key, String def); private static native
	 * int native_get_int(String key, int def); private static native long
	 * native_get_long(String key, long def); private static native boolean
	 * native_get_boolean(String key, boolean def); private static native void
	 * native_set(String key, String def); private static native void
	 * native_add_change_callback();
	 * at.home.break  1 disable other anable
	 */
	
	public static final String HOME_KEY = "service.at.home.break";
	public static final String ENABLE_HOME_KEY = "0" ; 
	public static final String DISABLE_HOME_KEY = "1" ;   
	public static final String PID="service.device.pid";
	public static String getPropertiesString(String key, String def) {
		String result=def;
		try {			
			Class<?> clzIActMag = Class.forName("android.os.SystemProperties");
			Method get = clzIActMag.getDeclaredMethod("native_get", String.class);
			get.setAccessible(true);//"sf.power.control"
			Object am = get.invoke(clzIActMag, key);
			result= am.toString();
			result=result.length()==0?def:result;
			return result;
		} catch (Exception e) {
			LogUitl.d(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	public static int getPropertiesInt(String key, int def) {
		int result=def;
		try {			
			Class<?> clzIActMag = Class.forName("android.os.SystemProperties");
			Method get = clzIActMag.getDeclaredMethod("native_get_int", String.class,int.class);
			get.setAccessible(true);
			Object am = get.invoke(clzIActMag, key,def);
			result=Integer.parseInt(am.toString());			
		} catch (Exception e) {
			LogUitl.d(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	public static boolean setPropertiesInt(String key, String def) {
		try {			
			Class<?> clzIActMag = Class.forName("android.os.SystemProperties");
			Method get = clzIActMag.getDeclaredMethod("native_set", String.class,String.class);
			get.setAccessible(true);
			get.invoke(clzIActMag, key,def);
			return true;		
		} catch (Exception e) {
			LogUitl.d(e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
}
