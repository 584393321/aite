package at.smarthome;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 室内机中心机根据家庭地址生成密码
 * @author 孙波海
 * @file AT_CreateInitPassword.java
 * @date 2016年3月10日
 * @time 上午10:27:37
 * @describe
 */
public class AT_CreateInitPassword {
	//udcvns11a11b111g
	public static String getAESKey(String doorSipAddress) {
		String result = null;
		String doorSip="";
		if (doorSipAddress != null) {
			int index = doorSipAddress.lastIndexOf("u");
			if (index > 0) {// zg_jsnjxwq_xiaoguanhuayuan_udcvn_s11a11b11u11f11r2n1_g
				doorSip = doorSipAddress.substring(0, index );
				doorSip=doorSip+doorSipAddress.substring(doorSipAddress.lastIndexOf("n")+1, doorSipAddress.length());
				doorSip=doorSip.replace("_", "");
				if (doorSip.length() < 16) {
					doorSip = "000000000000000000"+doorSip;
				}
				result = doorSip.substring(doorSip.length() - 16, doorSip.length());
			}
		}
		return result;
	}
	public static String getAESKey3(String doorSipAddress) {
		String result = null;
		String doorSip="";
		if (doorSipAddress != null) {
			int index = doorSipAddress.lastIndexOf("b");
			if (index > 0) {// zg_jsnjxwq_xiaoguanhuayuan_udcvn_s11a11b11u11f11r2n1_g
				doorSip = doorSipAddress.substring(0, index );
				//doorSip=doorSip+doorSipAddress.substring(doorSipAddress.lastIndexOf("n")+1, doorSipAddress.length());
				doorSip=doorSip.replace("_", "");
				if (doorSip.length() < 16) {
					doorSip = "000000000000000000"+doorSip;
				}
				result = doorSip.substring(doorSip.length() - 16, doorSip.length());
			}
		}
		return result;
	}
public static String getPasswordBySipAddress(String sipAddress)
{
	
		StringBuffer buf = null;
		String password = sipAddress.replace(",", "");
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
			byte b[] = md.digest();
			int i;
			buf = new StringBuffer("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return buf.toString();
	
	
}
}
