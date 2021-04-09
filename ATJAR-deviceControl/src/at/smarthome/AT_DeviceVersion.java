package at.smarthome;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Method;

import org.json.JSONObject;

import com.xhc.sbh.tool.lists.logcats.LogUitl;

public class AT_DeviceVersion {
	public static void setVersion(String version) {
		try {
			File file = new File("/data/atshared/version.txt");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream output = new FileOutputStream(file);
			JSONObject jsonO = new JSONObject();
			jsonO.put("version", version);
			output.write(jsonO.toString().getBytes());
			output.flush();
			output.close();
			ATHelp.rootcmd("sync");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUitl.d(e.getMessage());
		}
	}

	private static String getPropertiesString() {
		String result = "";
		try {
			Class<?> clzIActMag = Class.forName("android.os.SystemProperties");
			Method get = clzIActMag.getDeclaredMethod("native_get", String.class);
			get.setAccessible(true);// "sf.power.control"
			Object am = get.invoke(clzIActMag, "service.device.version");
			result = am.toString();
			return am.toString();
		} catch (Exception e) {
			LogUitl.d(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	public static String getVersion() {
		String version = "";
		FileInputStream input = null;
		try {
			File file = new File("/data/atshared/version.txt");
			if (!file.exists()) {
				file.createNewFile();
			}
			input = new FileInputStream(file);
			byte[] res = new byte[2048];
			int length = input.read(res);
			if (length > 0) {
				String ss = new String(res, 0, length);
				JSONObject jsonO = new JSONObject(ss);
				version = jsonO.getString("version");
			} else {
				input.close();
				version = getPropertiesString();
				FileOutputStream output = new FileOutputStream(file);
				JSONObject jsonO = new JSONObject();
				jsonO.put("version", version);
				output.write(jsonO.toString().getBytes());
				output.flush();
				output.close();
				ATHelp.rootcmd("sync");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUitl.d(e.getMessage());
		}
		return version;
	}
}
