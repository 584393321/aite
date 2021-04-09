package at.smarthome;

public class ServiceProcess {
	static {
		System.loadLibrary("atsmarthome");
	}
	static native int open(); // 打开接口 成功返回0
	static native void close();// 关闭接口
}
