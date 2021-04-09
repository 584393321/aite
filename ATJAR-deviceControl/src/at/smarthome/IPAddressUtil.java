package at.smarthome;

import android.content.Context;

import com.xhc.sbh.tool.lists.logcats.LogUitl;

public class IPAddressUtil {
	public static String recvIPByIpAddr( String ipAddr)
	{
		ipAddr=ipAddr.substring(1,ipAddr.lastIndexOf(":"));
		return ipAddr;
	}
	public static int recvPortByIpAddr( String ipAddr)
	{
		int port=0;
		ipAddr=ipAddr.substring(ipAddr.lastIndexOf(":")+1,ipAddr.length());
		if(ipAddr!=null)
		{
			port=Integer.parseInt(ipAddr);
		}
		return port;
	}
	public static String localMac=null;
	
	public synchronized final static String getMacAddressLowerCase(Context context){ 
		
		  if(localMac==null||localMac.length()==0)
		    {
		    	localMac=AT_Init.getLocalMac();
		    	if(localMac==null)
		    	{
		    		localMac=AT_Init.setLocalMac(context);
		    	}
		    	LogUitl.d("localMac====="+localMac);
		    }
			 return localMac==null?null:localMac;
		

	}
}