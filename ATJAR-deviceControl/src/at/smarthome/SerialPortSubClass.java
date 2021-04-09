package at.smarthome;


/**
 * 此类不允许修改，公共类，需要修改请继承实现
 * @author th
 *
 */
public interface SerialPortSubClass {	
	public void recvMsg(String msg) ;
	public void closeSerial(int serial) ;
}
