package at.smarthome;

import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class NetworkUtil {
	public static String getBroadcast() {
	    System.setProperty("java.net.preferIPv4Stack", "true");
	    try{
	    for (Enumeration<NetworkInterface> niEnum = NetworkInterface.getNetworkInterfaces(); niEnum.hasMoreElements();) {
	        NetworkInterface ni = niEnum.nextElement();
	        if(ni.getName().toLowerCase().equals("wlan0"))
			{
	        if (!ni.isLoopback()) {
	            for (InterfaceAddress interfaceAddress : ni.getInterfaceAddresses()) {
	            	if (interfaceAddress.getBroadcast() != null) {
	            		return interfaceAddress.getBroadcast().toString().substring(1);
					}
	            }
	        }
			}
	    }
	    }catch(Exception e)
	    {
	    	
	    }
	    return null;
	}
}
