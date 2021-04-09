package at.smarthome;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import org.apache.commons.net.util.Base64;

/**
 * @author Mr.Zheng
 * @date 2014年8月22日 下午1:44:23
 */
public class RSAUtil {	
	private synchronized static PublicKey getPublicKeyStr() {
		
			StringBuffer sb = new StringBuffer();
			sb.append("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA54dIlq44FJP1WaWpUF1Z\n");
			sb.append("I60W8mcZnK0ADr+dKe+bnjcjdztjjzeFzCQJGbDnfUzMrDXkep6SENNNVlZ7pl5V\n");
			sb.append("bJOfaZHKiuD1mvrSQv8TybfixRmnBuJtEA6jKV1oKLnViR0vGQI2ty+1U04xRQcS\n");
			sb.append("SZ+W+Y6ZnMwZLV6jBQsfPMmTUH4cCv6Nk98q3cT1h1xFA4+JMh9XmhG/bUWGKZIx\n");
			sb.append("AQBJPbWWMnixZGlmNv9G67qP8QWy0vKbm+J7JYKayjDZXbss68+MHzjg6z5lcK4G\n");
			sb.append("5NQ9+/2F6T5QQ8Qo5r9gBO0r/y1AZRC2jNWC7DyaVc85FxsTSmiqFOJYI2EIp76o\n");
			sb.append("bwIDAQAB\n");			
		
			return loadPublicKey(sb.toString());
		
	}
	private final static String RSA = "RSA/ECB/PKCS1Padding";
	
	/**
	 * 用公钥加密 <br>
	 * 每次加密的字节数，不能超过密钥的长度值减去11
	 * 
	 * @param data
	 *            需加密数据的byte数据
	 * @param pubKey
	 *            公钥
	 * @return 加密后的byte型数据
	 */
	public static byte[] encryptData(byte[] data) {
		try {
			//pher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			Cipher cipher = Cipher.getInstance(RSA);
			// 编码前设定编码方式及密钥
			cipher.init(Cipher.ENCRYPT_MODE, getPublicKeyStr());
			// 传入编码数据并返回编码结果
			return cipher.doFinal(data);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 用私钥解密
	 * 
	 * @param encryptedData
	 *            经过encryptedData()加密返回的byte数据
	 * @param privateKey
	 *            私钥
	 * @return
	 */
	public static byte[] decryptData(byte[] encryptedData) {
		try {
			Cipher cipher = Cipher.getInstance(RSA);
			cipher.init(Cipher.DECRYPT_MODE, getPublicKeyStr());
			return cipher.doFinal(encryptedData);
		} catch (Exception e) {
			return null;
		}
	}


	/**
	 * 从字符串中加载公钥
	 * 
	 * @param publicKeyStr
	 *            公钥数据字符串
	 * @throws Exception
	 *             加载公钥时产生的异常
	 */
	private static PublicKey loadPublicKey(String publicKeyStr) {
		try {
			byte[] buffer = Base64.decodeBase64(publicKeyStr);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			return (RSAPublicKey) keyFactory.generatePublic(keySpec);
		} catch (NoSuchAlgorithmException e) {
			//throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			//throw new Exception("公钥非法" + e.getMessage());
		} catch (NullPointerException e) {
			//throw new Exception("公钥数据为空");
		}
		return null;
	}

	/**
	 * 从字符串中加载私钥<br>
	 * 加载时使用的是PKCS8EncodedKeySpec（PKCS#8编码的Key指令）。
	 * 
	 * @param privateKeyStr
	 * @return
	 * @throws Exception
	 */
	private static PrivateKey loadPrivateKey(String privateKeyStr) throws Exception {
		try {
			byte[] buffer = Base64.decodeBase64(privateKeyStr);			
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(buffer);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("私钥非法");
		} catch (NullPointerException e) {
			throw new Exception("私钥数据为空");
		}
	}

	

	
}
