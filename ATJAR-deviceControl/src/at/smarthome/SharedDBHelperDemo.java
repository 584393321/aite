package at.smarthome;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/** 
 * @author 孙波海
 * @describe 共享数据库管理类，可实用于室内机各应用数据共享
 * 拷贝该类到自己项目下，修改类名为SharedDBHelper根据需要创建表项
 */
public class SharedDBHelperDemo extends SQLiteOpenHelper {

	public SharedDBHelperDemo(Context context) {
		super(context, "atshared.db", null, 2);
		// TODO Auto-generated constructor stub
	}

	public SharedDBHelperDemo(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
//		/*主页设备表
//		 * **/
//		db.execSQL("create table if not exists home_device(key_id integer primary key autoincrement,room_id integer,dev_id integer,dev_name varchar(10),room_name varchar(10),"
//				+ " dev_class_type varchar(10),key_image varchar(10),command varchar(10))");
//		
//		/*首页快捷键
//		 * **/
//		db.execSQL("create table if not exists home_helper(_id integer primary key autoincrement,type varchar(10), name varchar(10),"
//				+ " icon_res integer default -1,is_show integer default 1)");
//		
//		/*场景表
//		 * **/
//		db.execSQL("create table if not exists home_scene (_id integer primary key autoincrement,control_id integer,scene_name varchar(30),security_mode varchar(30),scene_image varchar(10))");
//		
//		db.execSQL("create table if not exists home_weather_info (city_name varchar(10),city_numbler varchar(10),weather varchar(10),refresh_time varchar(15))");
//		db.execSQL("create table if not exists home_device_control(dev_name varchar(10),room_name varchar(10),dev_id integer,dev_class_type varchar(15))");
//		
//		initHomeHelper(db);
	}

//	//添加一项需要在StaticValue.java中定义该类型的中英资源名
//	private void initHomeHelper(SQLiteDatabase db) {
//		db.execSQL("insert into home_helper(_id,type,name,is_show)values(0,'videomessage','留影留言',1)");
//		db.execSQL("insert into home_helper(_id,type,name,is_show)values(1,'systeminfo','系统信息',1)");
//		db.execSQL("insert into home_helper(_id,type,name,is_show)values(2,'calllift','召梯',1)");
//		db.execSQL("insert into home_helper(_id,type,name,is_show)values(3,'album','图库',1)");
//		//db.execSQL("insert into home_helper(_id,type,name,is_show)values(3,'remotemonitoring','远程监控',1)");
//	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
//		db.execSQL("replace into sys_setting(_id,type,value)values(9,'connection_state','')"); 
//		db.execSQL("replace into sys_setting(_id,type,value)values(10,'wall_address','')"); 
		
	}
}
