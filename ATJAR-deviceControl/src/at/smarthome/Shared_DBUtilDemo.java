package at.smarthome;


/**
 * 
 * @author
 * @describe 共享数据库应用调用封装类，数据库采用单例模式，不允许再创建实例
 * 拷贝该类到自己项目下，修改类名，去掉注释
 */
public class Shared_DBUtilDemo {

//	private Shared_DBUtilDemo(){};
//	private static SharedDBHelperDemo sharedDB;
//
//	private static SQLiteDatabase read_sqliteDatabase;
//	private static SQLiteDatabase write_sqliteDatabase;
//
//	public synchronized static SQLiteDatabase getReadSqliteDatabase(
//			Context context) {
//		if (read_sqliteDatabase == null)
//			read_sqliteDatabase = getDB(context).getReadableDatabase();
//
//		return read_sqliteDatabase;
//	}
//
//	public synchronized static SQLiteDatabase getWriteSqliteDatabase(
//			Context context) {
//		if (write_sqliteDatabase == null)
//			write_sqliteDatabase = getDB(context).getWritableDatabase();
//		return write_sqliteDatabase;
//	}
//
//	private synchronized static SharedDBHelperDemo getDB(Context context) {
//		if (sharedDB == null)
//			sharedDB = new SharedDBHelperDemo(new DatabaseContext(context));
//		return sharedDB;
//	}
}
