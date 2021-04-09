package at.smarthome;

import java.io.File;

import com.xhc.sbh.tool.lists.logcats.LogUitl;

import android.database.sqlite.SQLiteDatabase;
/**
 * 共享数据库管理工具
 * @author 孙波海
 * @file AT_DB_SharedUitl.java
 * @date 2015年12月14日
 * @time 下午3:08:38
 * @describe
 */
public class AT_DB_SharedUitl {
	private static SQLiteDatabase read_sqliteDatabase;
	public synchronized static SQLiteDatabase getReadSqliteDatabase()
	{
		try{
		if(read_sqliteDatabase==null)
		{
			if(new File("mnt/sdcard/atshared/atshared.db").exists())
			{
			read_sqliteDatabase = SQLiteDatabase.openDatabase("mnt/sdcard/atshared/atshared.db", null, SQLiteDatabase.OPEN_READWRITE);
			}
		}
		LogUitl.d("read_sqliteDatabase is null="+(read_sqliteDatabase==null));
		}catch(Exception e)
		{
			LogUitl.d(e.getMessage());
		}
		return read_sqliteDatabase;
	}

	
}
