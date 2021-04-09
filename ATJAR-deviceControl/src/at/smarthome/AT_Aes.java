package at.smarthome;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


/**
 * 加解密类 不能随便修改
 * 
 * @author th
 *
 */
public class AT_Aes {
	/*
	 * static { System.loadLibrary("atsmarthome"); }
	 */

	public static String setEncode(String src) {
		if (src == null || src.length() == 0) {
			return null;
		} else {// 'a','t','s','m','a','r','t','2','0','1','5','1','1','0','4','9'
			return setEncodeByKey(src, "atsmart201511049");
		}
	}

	public static String getDecode(String src) {
		if (src == null || src.length() == 0) {
			return null;
		} else {
			return getDecodeByKey(src, "atsmart201511049");
		}
	}
	

	/*
	 * public static String setEncodeByKey(String src,String key) {
	 * if(src==null||src.length()==0||key==null||(key.length()%16)!=0) { return
	 * null; }else { return encodeStrByKey(src,key); } }
	 */
	/*
	 * public static String getDecodeByKey(String src,String key) {
	 * if(src==null||src.length()==0||key==null||(key.length()%16)!=0) { return
	 * null; }else {
	 * 
	 * return decodeStrByKey(src,key); } }
	 */
	public static String setEncodeByKey(String plainText, String keyStr) {
		byte[] encrypt = null;
		try {
			if (plainText == null || plainText.length() == 0 || keyStr == null || (keyStr.length() % 16) != 0) {
				return null;
			} else {
				SecretKeySpec keySpec = new SecretKeySpec(keyStr.getBytes(), "AES");
				Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
				cipher.init(Cipher.ENCRYPT_MODE, keySpec);
				int pad = plainText.getBytes().length % 16;
				if (pad != 0) {
					for (int i = 0; i < 16 - pad; i++) {
						plainText = plainText + "\0";
					}
				}
				byte c[] = plainText.getBytes();
				encrypt = cipher.doFinal(plainText.getBytes());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new String(Base64Utils.encode(encrypt));
	}

	public static String getDecodeByKey(String encryptData, String keyStr) {
		byte[] decrypt = null;
		try {
			if (encryptData == null || encryptData.length() == 0 || keyStr == null || (keyStr.length() % 16) != 0) {
				return null;
			} else {
				SecretKeySpec keySpec = new SecretKeySpec(keyStr.getBytes(), "AES");
				Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
				cipher.init(Cipher.DECRYPT_MODE, keySpec);
				decrypt = cipher.doFinal(Base64Utils.decode(encryptData));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (decrypt == null)
			return null;
		else
			return new String(decrypt).trim();
	}

	private native static String encodeStr(String src);

	private native static String decodeStr(String src);

	private native static String encodeStrByKey(String src, String key);

	private native static String decodeStrByKey(String src, String key);
}
