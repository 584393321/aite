package at.smarthome;

import java.io.File;

import com.xhc.sbh.tool.lists.logcats.LogUitl;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * 
 * @author 孙波海
 * @file DatabaseContext.java
 * @date 2015年12月3日
 * @time 下午3:13:28
 * @describe 重写数据库上下文实现管理共享数据库
 */
public class DatabaseContext extends ContextWrapper {

	public DatabaseContext(Context base) {
		super(base);
		// TODO Auto-generated constructor stub
	}

	@Override
	public File getDatabasePath(String name) {
		// 判断是否存在sd 卡
		boolean sdExists = android.os.Environment.MEDIA_MOUNTED
				.equals(android.os.Environment.getExternalStorageState());
		if (!sdExists) {
			LogUitl.d("sdExists  is not exists");
			return null;
		} else {
			File dbFile = null;
			try {
				String dbDir = android.os.Environment
						.getExternalStorageDirectory().getAbsolutePath();
				dbDir += "/atshared";
				String dbPath = dbDir + "/" + name;
				File dirFile = new File(dbDir);
				if (!dirFile.exists()) {
					boolean result = dirFile.mkdirs();
					LogUitl.d("dbPath==========" + result);
				}
				LogUitl.d("dbPath==========" + dbPath);
				dbFile = new File(dbPath);
				if (!dbFile.exists()) {
					boolean result = dbFile.createNewFile();
					LogUitl.d("dbPath==========" + result);
				}
			} catch (Exception e) {
				dbFile = null;
				LogUitl.d(e.getMessage());
			}
			return dbFile;
		}

	}

	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode,
			CursorFactory factory) {
		// TODO Auto-generated method stub
		// return super.openOrCreateDatabase(name, mode, factory);
		return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
	}

	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode,
			CursorFactory factory, DatabaseErrorHandler errorHandler) {
		// TODO Auto-generated method stub
		// return super.openOrCreateDatabase(name, mode, factory, errorHandler);
		return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), null);
	}

}
