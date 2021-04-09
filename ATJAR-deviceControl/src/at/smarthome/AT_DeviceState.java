package at.smarthome;
/**
 * 状态参考
 * @author th
 *
 */
public class AT_DeviceState {
	public final static String LIGHT_SOCKET_CURTAIN="{\"power\":\"on\",\"value\":100}";
	public final static String AUDIO="{\"playstatus\":\"play\",\"playmode\":1,\"cur_play_song\":100,\"total_songs\":100,\"audio_src\":1,\"volume\":50}";
	public final static String THERMOSTAT="{\"power\":\"on\",\"value\":100,\"mode\":\"mode_cool\",\"set_tmpr\":25,\"real_tmpr\":26,\"hour\":12,\"min\":30,\"wind_mode\":\"wind_height\",\"frost\":\"frost_on\"}";
	public final static String FRESH_AIR_SYSTEM="{\"power\":\"on\",\"value\":100,\"set_tmpr\":25,\"real_tmpr\":26,\"hour\":12,\"min\":30,\"wind_mode\":\"wind_height\"}";
    public final static String PLAY_MODE="  0 //单曲播放    1//单曲循环  2//全部播放  3//全部循环 4//随机播放";	
    public final static String SRC=" 1//DVD  2 //MP3 3 //RADIO   8//蓝牙";
    public final static String PLAY_MODE_CN="  0 //单曲播放    1//单曲循环  2//全部播放  3//全部循环 4//随机播放";	
    public final static String SRC_CN=" 1//DVD  2 //MP3 3 //RADIO   8//蓝牙";
}
