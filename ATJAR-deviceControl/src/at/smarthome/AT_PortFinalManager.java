package at.smarthome;

public class AT_PortFinalManager {
//9100-9199间端口号为主分控使用
public final static int BUSINESS_UDPWAITADD=9100;
public final static int BUSINESS_TCPCOMMAND=9101;
public final static int BUSINESS_UDPSMART=9102;//开放的UDP包接收控制端口
//协调器等设备等待搜索的广播端口
public final static int UDP_BROADKEST_PORT=9200;
//remote HTTP服务器
public final static int HTTP_SERVER=9201;
//门口机梯控UDP接收端口
public final static int LIFT_SERVER=9203;
}
