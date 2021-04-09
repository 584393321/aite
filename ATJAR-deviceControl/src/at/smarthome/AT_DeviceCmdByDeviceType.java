package at.smarthome;

/**
 * 根据设备类型查询设备功能
 *
 * @author CHT
 *
 */
public class AT_DeviceCmdByDeviceType {
	public class Light {
		// 灯光功能 func_command 取值
		public final static String ON = "on";
		public final static String OFF = "off";

		// 状态上报
		public class State {
			// dev_state:{"power":"on","value":0}
			public final static String POWER = "power"; // 取值func_command
			public final static String VALUE = "value";// 该参数所有状态都会带有，有些设备无需要该值
		}
	}

	public class Dimmer {
		// 调灯光功能 func_command 取值
		public final static String ON = "on";
		public final static String OFF = "off";
		public final static String BRIGHTNESS = "brightness";
		public final static String BRIGHTNESS_ADD = "brightness+";
		public final static String BRIGHTNESS_MINUS = "brightness-";

		// 状态上报
		public class State {
			// dev_state:{"power":"on","value":0-100}
			public final static String POWER = "power";
			public final static String VALUE = "value";// 当前调光值，该参数所有状态都会带有，有些设备无需要该值
		}
	}

	public class Curtain {
		// 窗帘功能 func_command 取值
		public final static String OPEN = "open";
		public final static String CLOSE = "close";
		public final static String STOP = "stop";
		public final static String RESET = "reset";
		public final static String REVERSE = "reverse";
		public final static String POSITION="position";

		// 状态上报
		public class State {
			// dev_state:{"power":"on","value":0-100}
			public final static String POWER = "power";
			public final static String VALUE = "value";//
		}
	}

	public class Socket {
		// 插座功能 func_command 取值
		public final static String ON = "on";
		public final static String OFF = "off";
		public final static String ELECTRICAL = "electrical";
		public final static String UNLOCK="unlock";
		public final static String LOCK="lock";
		// 状态上报
		public class State {
			// dev_state:{"power":"on","value":0}
			public final static String POWER = "power";
			public final static String VALUE = "value";// 查电量时
			public final static String C_L_T="c_l_t";
			public final static String P="P";
			public final static String V="V";
			public final static String A="A";
			public final static String E="E";
		}

	}


	public class Fresh_air_system {
		// 新风功能 func_command 取值
		public final static String ON = "on";
		public final static String OFF = "off";
		public final static String WIND_SPEED_HEIGHT = "wind_speed_height";
		public final static String WIND_SPEED_MIDDLE = "wind_speed_middle";
		public final static String WIND_SPEED_LOW = "wind_speed_low";

		public class State {
			// dev_state:{"power":"on","wind_speed":"wind_speed_height"}
			public final static String POWER = "power";
			public final static String WIND_SPEED = "wind_speed";//
		}

	}
	public class Central_air {
		//
		public final static String ON = "on";
		public final static String OFF = "off";
		public final static String WIND_SPEED_HEIGHT = "wind_speed_height";
		public final static String WIND_SPEED_HEIGHT_H = "wind_speed_height_h";
		public final static String WIND_SPEED_MIDDLE = "wind_speed_middle";
		public final static String WIND_SPEED_LOW = "wind_speed_low";
		public final static String WIND_SPEED_LOW_L = "wind_speed_low_l";
		public final static String WIND_SPEED_AUTO = "wind_speed_auto";
		public final static String TEMPERATURE = "temperature";
		public final static String MODE_COOL = "mode_cool";
		public final static String MODE_HOT = "mode_hot";
		public final static String MODE_WIND = "mode_wind";
		public final static String MODE_WET = "mode_wet";
		public final static String MODE_AUTO = "mode_auto";
		public class State {
			public final static String POWER = "power";
			public final static String WIND_SPEED = "wind_speed";//
			public final static String MODE = "mode";// 模式
			public final static String SET_TEMPERATURE = "set_temperature";//
			public final static String CURR_TEMPERATURE = "curr_temperature";//
		}

	}
	/**
	 * 需祥细重写
	 *
	 * @author CHT
	 *
	 */
	public class Floor_Warm {
		// 地暖功能 func_command 取值
		public final static String ON = "on";
		public final static String OFF = "off";
		public final static String MODE_AUTO = "mode_auto";
		public final static String MODE_HAND = "mode_hand";
		public final static String TEMPERATURE = "temperature";

		public class State {
			// dev_state:{"power":"on","value":0,"mode":"mode_auto","set_temperature":30,"curr_temperature":30}
			public final static String POWER = "power";
			public final static String SET_TEMPERATURE = "set_temperature";//
			public final static String CURR_TEMPERATURE = "curr_temperature";//
			public final static String MODE = "mode_auto";//
		}
	}

	public class TV {
		// 电视功能 func_command 取值 无状态
		public final static String ON_OFF = "on_off";
		public final static String AT_TV = "at_tv";// 视频源
		public final static String PING_XIAN = "pingxian";// 屏显
		public final static String WANG_FAN = "wangfan";// 往返
		public final static String ZHI_SHI = "zhishi";// 制式 zhishi
		public final static String NORMAL = "normal";// 正常 normal
		public final static String LI_YIN = "liyin";// 丽音 liyin
		public final static String PIC_IN_PIC = "picinpic";// 画中画 picinpic
		public final static String SLEEP = "sleep";// 睡眠 sleep
		public final static String SOUND = "sound";// 伴音 sound
		public final static String SWITCH = "switch";// 切换
		public final static String PROPORTION = "proportion";// 比例 proportion
		public final static String LEFT = "left";
		public final static String RIGHT = "right";
		public final static String UP = "up";
		public final static String DOWN = "down";
		public final static String OK = "ok";
		public final static String BACK = "back";
		public final static String CHANNAL_ADD = "channel+";
		public final static String CHANNAL_MINUS = "channel-";
		public final static String CHANNAL = "channel";
		public final static String CHANNAL_NUM = "channel_num";
		public final static String VOLUME_MINUS = "volume-";
		public final static String VOLUME_ADD = "volume+";
		public final static String MUTE = "mute";
		public final static String MENU="menu";
		public final static String KEY_0 = "key_0";
		public final static String KEY_1 = "key_1";
		public final static String KEY_2 = "key_2";
		public final static String KEY_3 = "key_3";
		public final static String KEY_4 = "key_4";
		public final static String KEY_5 = "key_5";
		public final static String KEY_6 = "key_6";
		public final static String KEY_7 = "key_7";
		public final static String KEY_8 = "key_8";
		public final static String KEY_9 = "key_9";

	}

	public class DVB {
		// 机顶盒功能 func_command 取值 无状态
		public final static String ON_OFF = "on_off";

		public final static String PLAY_BILL = "playbill";// 节目表 playbill
		public final static String AUDIO_CHANNEL = "audioch";// 声道 audioch
		public final static String EXIT = "exit";// 退出 exit
		public final static String PAGE_UP = "pageup";// 上翻页 pageup
		public final static String PAGE_DOWN = "pagedn";// 下翻页 pagedn
		public final static String ZI_XUN = "zixun";// 资讯 zixun
		public final static String FAVOR = "favor";// 喜爱 favor
		public final static String INFO = "info";// 信息 info
		public final static String RED = "red";// 红 red
		public final static String GREEN = "green";// 绿 green
		public final static String YELLOW = "yellow";// 黄 yellow
		public final static String BLUE = "blue";// 蓝 blue
		public final static String AUDIO_CHANGE="audio_change";
		public final static String LEFT = "left";
		public final static String RIGHT = "right";
		public final static String UP = "up";
		public final static String DOWN = "down";
		public final static String OK = "ok";
		public final static String BACK = "back";
		public final static String CHANNAL_ADD = "channel+";
		public final static String CHANNAL = "channel";
		public final static String CHANNAL_NUM = "channal_num";

		public final static String CHANNAL_MINUS = "channel-";
		public final static String MENU = "menu";
		public final static String VOLUME_MINUS = "volume-";
		public final static String VOLUME_ADD = "volume+";
		public final static String MUTE = "mute";
		public final static String KEY_0 = "key_0";
		public final static String KEY_1 = "key_1";
		public final static String KEY_2 = "key_2";
		public final static String KEY_3 = "key_3";
		public final static String KEY_4 = "key_4";
		public final static String KEY_5 = "key_5";
		public final static String KEY_6 = "key_6";
		public final static String KEY_7 = "key_7";
		public final static String KEY_8 = "key_8";
		public final static String KEY_9 = "key_9";

	}

	public class Aircondition {
		// 空调功能 func_command 取值 暂时无状态
		public final static String ON = "on";
		public final static String OFF = "off";
		public final static String MODE_COOL = "mode_cool";
		public final static String MODE_HOT = "mode_hot";
		public final static String MODE_WET = "mode_wet";
		public final static String MODE_WIND = "mode_wind";
		public final static String MODE_AUTO = "mode_auto";
		public final static String WIND_SPEED_HEIGHT = "wind_speed_height";
		public final static String WIND_SPEED_MIDDLE = "wind_speed_middle";
		public final static String WIND_SPEED_LOW = "wind_speed_low";
		public final static String WIND_SPEED_AUTO = "wind_speed_auto";
		public final static String TEMPERATURE = "temperature";
		public final static String TEMPERATURE_UP = "temperature+";
		public final static String TEMPERATURE_DOWN = "temperature-";
		public final static String WIND_DIRECTION_H = "wind_direction_horizontal";// 左右扫风
		public final static String WIND_DIRECTION_V = "wind_direction_vertical";// 上下扫风
		public final static String WIND_DIRECTION_HAND= "wind_direction_hand";//手动风向，停止摆向
		public final static String SLEEP_ON = "sleep_on";// 睡眠
		public final static String SLEEP_OFF = "sleep_off";// 睡眠
		public final static String HEAT_ON = "heat_on";// 电辅热
		public final static String HEAT_OFF = "heat_off";// 电辅热
		public final static String STRONG_ON = "strong_on";// 强力
		public final static String STRONG_OFF = "strong_off";// 强力
		public final static String LIGHT_ON = "light_on";// 灯光
		public final static String LIGHT_OFF = "light_off";// 灯光
		public final static String AIRCLEAR_ON = "airclear_on";// 空清
		public final static String AIRCLEAR_OFF = "airclear_off";// 空清
		public final static String ECONOMIC_ON = "economic_on";// 经济
		public final static String ECONOMIC_OFF = "economic_off";// 经济

		public class State {
			public final static String POWER = "power";
			public final static String VALUE = "value";//
			public final static String MODE = "mode";// 模式
			public final static String SET_TEMPERATURE = "set_temperature";//
			public final static String CURR_TEMPERATURE = "curr_temperature";//
			public final static String WIND_DIRECTION = "wind_direction";
			public final static String WIND_SPEED = "wind_speed";//
			public final static String SLEEP = "sleep_on";//
			public final static String HEAT = "heat_on";//
			public final static String STRONG = "strong_on";//
			public final static String LIGHT = "light_on";//
			public final static String AIRCLEAR = "airclear_on";//
			public final static String ECONOMIC = "economic_on";//
			public final static String HOUR = "hour";//
			public final static String MIN = "min";//
		}

	}

	public class DVD {
		// DVD功能 func_command 取值 无状态
		public final static String ON_OFF = "on_off";
		public final static String PLAY = "play";
		public final static String PAUSE = "pause";
		public final static String STOP = "stop";
		public final static String PLAY_PREVIOUS = "play_previous";
		public final static String PLAY_NEXT = "play_next";
		public final static String VOLUME_MINUS = "volume-";
		public final static String VOLUME_ADD = "volume+";
		public final static String WAREHOUSE_IN_OUT = "warehouse_in_out";

	}

	public class Amplifier {
		// 功放功能 func_command 取值 无状态
		public final static String ON_OFF = "on_off";
		public final static String VOLUME_MINUS = "volume-";
		public final static String VOLUME_ADD = "volume+";
		public final static String AUDIOCH_LEFT = "audioch_left";
		public final static String AUDIOCH_RIGHT = "audioch_right";
		public final static String SOURCE_TV = "source_tv";
		public final static String SOURCE_DVD = "source_dvd";
	}
	public class RGBLigth{
		public final static String ON = "on";
		public final static String OFF = "off";
		public final static String BRIGHTNESS = "brightness";
		public final static String RGB_MODE="rgb_mode";
		public final static String RGB_TONING="rgb_toning";
		public final static String RGB_TONING_BRIGHTNESS="rgb_toning_brightness";
		public class RGB_MODE_Parameter {
			public final static String RED = "red";
			public final static String ORANGE="orange";
			public final static String YELLOW = "yellow";
			public final static String GREEN = "green";
			public final static String CYAN = "cyan";//青色
			public final static String BLUE = "blue";
			public final static String PURPLE = "purple";
			public final static String COLOUR = "colour";
		}
		public class RGB_TONING_Parameter {
			public final static String R = "r";
			public final static String G = "g";
			public final static String B = "b";
		}
	}
	public class Audio {
		// 背景音乐功能 func_command 取值 无状态
		public final static String VOLUME_MINUS = "volume-";
		public final static String VOLUME_ADD = "volume+";
		public final static String VOLUME = "volume";
		public final static String PLAY = "play";
		public final static String PAUSE = "pause";
		public final static String PLAY_PREVIOUS = "play_previous";
		public final static String PLAY_NEXT = "play_next";
		public final static String PLAY_MODE = "play_mode";
		public final static String AUDIO_SRC = "audio_src";
		public final static String MUTE_ON = "mute_on";
		public final static String MUTE_OFF="mute_off";
		public class Source_Parameter {
			public final static String DVD = "dvd";
			public final static String MP3 = "mp3";
			public final static String RADIO = "radio";
			public final static String BLUETOOTH = "bluetooth";
			public final static String NETWORK = "network";
		}

		public class PlayMode_Parameter {

			public final static String SINGLE_PLAY = "single_play";
			public final static String SINGLE_LOOP = "single_loop";
			public final static String SORT_PLAY = "sort_play";
			public final static String SORT_LOOP = "sort_loop";
			public final static String RANDOM = "random";

		}

		public class State {
			// dev_state:{"power":"on","value":0,"playstatus":"play","cur_play_song":"东风破","total_songs":30,"audio_src":"mp3",
			// "volume":0-15}
			public final static String PLAY_STATUS = "playstatus";
			public final static String PLAY_MODE = "play_mode";
			public final static String CUR_PLAY_SONG = "cur_play_song";
			public final static String AUDIO_SRC = "audio_src";
			public final static String VOLUME = "volume";
			public final static String TOTALTIME = "totaltime";
			public final static String CURRENT = "current";
			public final static String VALUE = "value";
			public final static String MUTE = "mute";

		}
	}

	public class Smartlock {
		public final static String COUNTER_LOCK = "counter_lock";
		public final static String OPEN_LOCK = "open_lock";

		// dev_state:{
		// "tongue":0,"motor":0,"fingerprint_surplus":0,"card_surplus":0,"pwd_surplus":0,"battery":0,"volume":0,"language":0,
		// "open_type':"close/card_open/pwd_open/fungerprint_open/key_open","alarm":"
		// pwd_alarm / door_alarm / hijack_alarm / tmper_alarm / open_alarm/
		// fingerprint_alarm/ card_alarm "}
		public class OpenType{
			public final static String FINGERPRINT_OPEN = "fingerprint_open";
			public final static String CARD_OPEN = "card_open";
			public final static String PWD_OPEN = "pwd_open";
			public final static String KEY_OPEN = "key_open";
			public final static String REMOTE_OPEN = "remote_open";
			public final static String DEFAULT_OPEN="default_open";
		}
		public class State {
			//public final static String fingerprint_surplus = "fingerprint_surplus";
			//public final static String fingerprint_surplus = "card_surplus";
			//public final static String fingerprint_surplus = "pwd_surplus";
			//public final static String fingerprint_surplus = "battery";
		}

	}

	/*
	 * 晾衣架
	 *
	 * @author CHT
	 *
	 */
	public class DryingRacks {
		// 地暖功能 func_command 取值
		public final static String RISE = "rise";// 上升
		public final static String FALL = "fall";// 下降
		public final static String STOP = "stop";
		public final static String VOICE_ON = "voice_on";
		public final static String VOICE_OFF = "voice_off";
		public final static String DIS_ON = "dis_on";
		public final static String DIS_OFF = "dis_off";

		public final static String BAKEDRY_ON = "bakedry_on";// 烘干
		public final static String BAKEDRY_OFF = "bakedry_off";
		public final static String WINDDRY_ON = "winddry_on";
		public final static String WINDDRY_OFF = "winddry_off";

		public final static String LIGHT_ON = "light_on";
		public final static String LIGHT_OFF = "light_off";
		public final static String ANION_ON = "anion_on";// 负离子
		public final static String ANION_OFF = "anion_off";// 负离子
		public final static String WINDDRYT = "winddryt";
		public final static String BAKEDRYT = "bakedryt";
		public final static String DIST = "dist";

		public class State {
			// "{""voice":"voice_on","dis":"dis_off"，"bakedry":"bakedry_on","winddry":"winddry_on","light":"light_on",
			// "anion":"anion_on","position":"other",motor":"stop","bakedryt":0,"winddryt":0,"dist":0}
			public final static String VOICE = "voice";
			public final static String DIS = "dis";
			public final static String BAKEDRY = "bakedry";//
			public final static String WINDDRY = "winddry";//
			public final static String ANION = "anion";//// 负离子
			public final static String POSITION = "position";
			public final static String MOTOR = "motor";
			public final static String LIGHT = "light";
			public final static String WINDDRYT = "winddryt";
			public final static String BAKEDRYT = "bakedryt";
			public final static String DIST = "dist";

		}

	}

	public class ICool {
		public class SceneMode {
			public final static String HOME = "home";
			public final static String LEAVE = "leave";
			public final static String SLEEP = "sleep";
			public final static String DESICCANT = "desiccant";
		}
	}
	//
	public class Fan {
		// 功放功能 func_command 取值 无状态
		public final static String ON_OFF = "on_off";
		public final static String OSCILLATION = "oscillation";
		public final static String FANSPEED = "fanspeed";
	}

	public class Projector {
		// 投影
		public final static String ON_OFF = "on_off";
		public final static String VOLUME_MINUS = "volume-";
		public final static String VOLUME_ADD = "volume+";
		public final static String MUTE = "mute";
		public final static String LEFT = "left";
		public final static String RIGHT = "right";
		public final static String UP = "up";
		public final static String DOWN = "down";
		public final static String OK = "ok";
		public final static String PIC_IN = "pic+";
		public final static String PIC_OUT = "pic-";
	}
	public class AIR_Purifier {
		// 净化器
		public final static String ON_OFF = "on_off";
		public final static String ON = "on";
		public final static String OFF = "off";
		public final static String HUMIDIFY= "humidify";//加湿
		public final static String ATOMIZE = "atomize";//雾化
		/**
		 * @Description
		 */
		public final static String FANSPEED = "fanspeed";//
		/**
		 * @Description
		 */
		public final static String ANION = "anion";//负离子

		public final static String STERILIZE = "sterilize";
		public final static String MODE_AUTO = "mode_auto";
		public final static String MODE_HAND = "mode_hand";
		public final static String WIND_SPEED_HEIGHT = "wind_speed_height";
		public final static String WIND_SPEED_MIDDLE = "wind_speed_middle";
		public final static String WIND_SPEED_LOW = "wind_speed_low";
		public class State {
			public final static String POWER = "power";
			public final static String HUMIDIFY= "humidify";//加湿
			public final static String TEMPERATURE = "temperature";
			public final static String PM2_5="pm2_5";
			public final static String MODE="mode";
			public final static String WIND_SPEED="wind_speed";
			//"dev_state":{"pm2_5":100,"time":1488348280,"voc":100,"humidity":61,"temperature":25}
		}
	}
	public class Valve_controller {
		//阀门控制器
		public final static String OPEN = "open";
		public final static String CLOSE= "close";
		public class State {
			public final static String POWER = "power";
		}
	}
	public class Temperature {
		//空调
		// 温控器功能 func_command 取值
		public final static String ON = "on";
		public final static String OFF = "off";
		public final static String MODE_COOL = "mode_cool";
		public final static String MODE_HOT = "mode_hot";
		public final static String MODE_COOLSLEEP = "mode_coolsleep";
		public final static String MODE_HOTSLEEP = "mode_hotsleep";
		public final static String WIND_SPEED_HEIGHT = "wind_speed_height";
		public final static String WIND_SPEED_MIDDLE = "wind_speed_middle";
		public final static String WIND_SPEED_LOW = "wind_speed_low";
		public final static String WIND_SPEED_AUTO = "wind_speed_auto";
		public final static String FROST_ON = "frost_on";
		public final static String FROST_OFF = "frost_off";
		public final static String TIMER_ON = "timer_on";
		public final static String TIMER_OFF = "timer_off";
		public final static String TEMPERATURE = "temperature";
		public final static String MODE_WIND = "mode_wind";

		// 状态上报
		public class State {
			// dev_state:{"power":"on","value":0,"mode":"mode_cool","set_temperature":30,"curr_temperature":30,"hour":5,
			// "min":20,"wind_speed":"wind_speed_auto","frost":"frost_on"}
			public final static String POWER = "power";
			public final static String VALUE = "value";// 查电量时
			public final static String MODE = "mode";// 模式
			public final static String SET_TEMPERATURE = "set_temperature";//
			public final static String CURR_TEMPERATURE = "curr_temperature";//
			public final static String HOUR = "hour";//
			public final static String MIN = "min";//
			public final static String WIND_SPEED = "wind_speed";//
			public final static String FROST = "frost";// 除霜
		}
	}
	public class Thermostat_jg {

		public final static String HUMIDIFY= "humidify";//加湿
		public final static String TEMPERATURE = "temperature";

		public class State {
			public final static String POWER = "power";
			public final static String CURR_TEMPERATURE = "curr_temperature";
			public final static String CURR_HUMIDIFY= "curr_humidify";
			public final static String SET_TEMPERATURE = "set_temperature";
			public final static String SET_HUMIDIFY= "set_humidify";
		}
	}
	public class Thermostat {
		//
		public final static String ON = "on";
		public final static String OFF = "off";
		public final static String MODE_COOL = "mode_cool";
		public final static String MODE_HOT = "mode_hot";
		public final static String HUMIDIFY= "humidify";//加湿
		public final static String TEMPERATURE = "temperature";
		public class State {
			public final static String POWER = "power";
			public final static String MODE="mode";
			public final static String CURR_TEMPERATURE = "curr_temperature";
			public final static String CURR_HUMIDIFY= "curr_humidify";
			public final static String SET_TEMPERATURE = "set_temperature";
			public final static String SET_HUMIDIFY= "set_humidify";
		}
	}
	public class Noise {
		//噪音传感 器
		public class State {
			public final static String NOISE  = "noise";
		}
	}
	public class Telecontroller {
		public final static String LEFT = "left";
		public final static String RIGHT = "right";
		public final static String UP = "up";
		public final static String DOWN = "down";
		public final static String OK = "ok";
		public final static String BACK = "back";
		public final static String VOLUME_MINUS = "volume-";
		public final static String VOLUME_ADD = "volume+";
		public class State {

		}
	}
}
