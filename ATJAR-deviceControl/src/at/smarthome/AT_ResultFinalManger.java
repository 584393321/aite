package at.smarthome;
/**
 * 命令返回值
 * @author CHT
 *
 */
public class AT_ResultFinalManger {
	public final static String SUCCESS="success";//成功
	public final static String JSONERROR="json_error";//命令参数键错误
	public final static String SAME="same";//重复名称
	public final static String FAILURE="failure";//失败 一般为包头错误 如command
	public final static String DATAERROR="data_error";//命令参数值错误
	public final static String DATA_FORMAT_ERROR="data_format_error";//命令参数值错误
	public final static String SQLERROR="sql_error";//操作数据库失败
	public final static String PASSWORD_ERROR="password_error";//密码错误
	public final static String PERMISSIONS_ERROR="permissions_error";//无权限
	public final static String FORCED_EXIT="forced_exit";//被迫退出，如同一 个账号被后来上线端挤下线的通知
	public final static String MAC_FORMAT_ERROR="mac_format_error";//Mac格式错误，搜索时可能返回
	public final static String EXISTS="exists";//如布防有探头处于触发状态时的回复
	public static final String WIFI_STATE_DISABLED="wifi_state_disabled";//wifi已关闭
	public static final String WIFI_STATE_DISABLING="wifi_state_disabling";//wifi正在关闭
	public static final String WIFI_STATE_ENABLED="wifi_state_enabled";//wifi已打开
	public static final String WIFI_STATE_ENABLING="wifi_state_enabling";//wifi正在打开
	public static final String WIFI_STATE_UNKNOWN="wifi_state_unknown";//wifi未知状态
	public static final String EXECUTING="executing";//组合控制正在执行
	public static final String NOT_EXISTS="not_exists";//组合控制不存在
    public static final String ACK="ack";
    public static final String DEVICE_EXISTS="device_exists";//设备存在
    public static final String PART_SUCCESS="part_success";//部份操作成功
    public static final String ZIGBEE_GW_NOT_LINE="zigbee_gw_not_line";//网关不在线
    public static final String CODE_OVERFLOW="code_overflow";
    public static final String DEV_NOT_STUDIED="dev_not_studied";
    public final static String DEV_IS_LOCK="dev_is_lock";//设备已锁住，xx秒钟后自动解锁
    public final static String DEV_LOCK_DISABLE="dev_lock_disable";//设备未开启远程
    public final static String UNKNOWN="unknown";//未知命令
	public final  static String NOT_SUPPORTED="not_supported";//不支持(不支持的功能)

}
