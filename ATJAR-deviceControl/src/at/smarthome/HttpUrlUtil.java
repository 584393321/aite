package at.smarthome;

import com.xhc.sbh.tool.lists.logcats.LogUitl;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class HttpUrlUtil {

	public static String getServerIp() {
		try {
			InetAddress x = java.net.InetAddress.getByName("talk.atsmartlife.com");
			String res = x.getHostAddress();// 得到字符串形式的ip地址
			return res;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUitl.d(e.getMessage());
		}
		return null;
	}

	public synchronized static String getSeverPostUrl() {
		StringBuffer server_post =null;
		try {
			InetAddress x = java.net.InetAddress.getByName("server.atsmartlife.com");
			String res = x.getHostAddress();// 得到字符串形式的ip地址
			LogUitl.d("getSeverPostUrl===="+res);
			if (res != null) {
				server_post = new StringBuffer();
				server_post.append("http://");
//				server_post.append(res);
//				server_post.append("192.168.18.32");
				server_post.append("119.23.56.17");
				server_post.append("/postmsg");
//				server_post.append("http://zhihuigu.atsmartlife.com/postmsg_private");
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUitl.d(e.getMessage());
		}
		return server_post==null?null:server_post.toString();
	}

	public synchronized static String getSeverPostUrlByLogin() {
		StringBuffer server_login = null;
		try {
			InetAddress x = java.net.InetAddress.getByName("server.atsmartlife.com");
			String res = x.getHostAddress();// 得到字符串形式的ip地址
			if (res != null) {
				server_login = new StringBuffer();
				server_login.append("http://");
//				server_login.append(res);
//				server_login.append("192.168.18.32");
//				server_login.append("/secureport");
				server_login.append("119.23.56.17");
				server_login.append("/secureport_aes");
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUitl.d(e.getMessage());
			server_login=null;
		}
		return server_login==null?null:server_login.toString();
	}

	public synchronized static String getSeverPostWWUrlByLogin() {
		StringBuffer server_login = null;
		try {
			InetAddress x = java.net.InetAddress.getByName("server.atsmartlife.com");
			String res = x.getHostAddress();// 得到字符串形式的ip地址
			if (res != null) {
				server_login = new StringBuffer();
				server_login.append("http://");
//				server_login.append(res);
//				server_login.append("192.168.18.32");
				server_login.append("119.23.56.17");
				server_login.append("/secureport");
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUitl.d(e.getMessage());
			server_login=null;
		}

		return server_login==null?null:server_login.toString();
	}

	public synchronized static String getSeverGetUrl(String token) {
		StringBuffer server_get = null;
		try {
			if (token != null) {
				server_get = new StringBuffer();
				InetAddress x = java.net.InetAddress.getByName("server.atsmartlife.com");
				String res = x.getHostAddress();// 得到字符串形式的ip地址
				if (res != null) {
					server_get.append("http://");
//					server_get.append(res);
//					server_get.append("192.168.18.32");
					server_get.append("119.23.56.17");
					server_get.append("/getmsg_all?token=");
					server_get.append(token);
				}
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LogUitl.d(e.getMessage());
		}
		return server_get==null?null:server_get.toString();
	}
}
