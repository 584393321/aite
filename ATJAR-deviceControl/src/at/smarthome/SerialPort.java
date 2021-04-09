package at.smarthome;

import com.xhc.sbh.tool.lists.logcats.LogUitl;

public class SerialPort {
	static {
		System.loadLibrary("atsmarthome");
		//ReLinker.loadLibrary(context, “mylibrary”)
	}
	
	public static native int init(int port, int speed, int databits, int stopbits, String parity);
	public static native void destory();
	public static void closeCall(int serialNum)
	{
		LogUitl.d("serial close="+serialNum);
		//串口退出上报
		if(mSerialPortSubClass!=null)
		{
			mSerialPortSubClass.closeSerial(serialNum);
		}else
		{
			LogUitl.d("mSerialPortSubClass2 is null");
		}
	}
	public static native int send(String content);
	//字节异或运算
	public static native  int crc(String content);
	static SerialPortSubClass mSerialPortSubClass;
	//odd 奇校验，even偶校验
	public static void setSerialPortSubClassCall(SerialPortSubClass mmSerialPortSubClass)
	{
		mSerialPortSubClass=mmSerialPortSubClass;
	}
	public static boolean isSetCall()
	{
		return mSerialPortSubClass==null?true:false;
	}
	private static void serialDataCall(String string) {
		LogUitl.d("java recv string="+string);
		if(mSerialPortSubClass!=null)
		{
			mSerialPortSubClass.recvMsg(string);
		}else
		{
			LogUitl.d("mSerialPortSubClass is null");
		}
	}
	
	public static native int init2(int port, int speed, int databits, int stopbits, String parity);
	public static native void destory2();
	public static native int send2(String content);
	public static native  int crc2(String content);
	private static void serialDataCall2(String string) {
		//LogUitl.d("java recv2 string="+string);
		if(mSerialPortSubClass2!=null)
		{
			mSerialPortSubClass2.recvMsg(string);
		}else
		{
			LogUitl.d("mSerialPortSubClass2 is null");
		}
	}
	public static void closeCall2(int serialNum)
	{
		LogUitl.d("serial close="+serialNum);
		//串口退出上报
		if(mSerialPortSubClass2!=null)
		{
			mSerialPortSubClass2.closeSerial(serialNum);
		}else
		{
			LogUitl.d("mSerialPortSubClass2 is null");
		}
	}
	static SerialPortSubClass mSerialPortSubClass2;
	public static void setSerialPortSubClassCall2(SerialPortSubClass mmSerialPortSubClass)
	{
		mSerialPortSubClass2=mmSerialPortSubClass;
	}
	public static boolean isSetCall2()
	{
		return mSerialPortSubClass2==null?true:false;
	}
}
