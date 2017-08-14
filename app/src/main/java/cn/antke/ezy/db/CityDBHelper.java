package cn.antke.ezy.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

/**
 * Created by liuzhichao on 2017/2/22.
 * 数据库辅助类
 */
class CityDBHelper extends SQLiteOpenHelper {

	static final String DB_NAME = "city.db";
	private static final int DB_VERSION = 1;
	private static CityDBHelper cityDBHelper;
	private static SQLiteDatabase database;

	private CityDBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	synchronized static CityDBHelper getInstence(Context context) {
		if (cityDBHelper == null) {
			cityDBHelper = new CityDBHelper(context);
		}
		return cityDBHelper;
	}

	/**
	 * @param context 打开或者创建数据库
	 */
	void openOrCreateDB(Context context) {
		if (database != null) {
			database.close();
		}
		String path = context.getFilesDir().getAbsolutePath() + File.separator + DB_NAME;
		database = SQLiteDatabase.openOrCreateDatabase(path, null);
	}

	/**
	 * @return 数据库对象
	 */
	static SQLiteDatabase getDatabase(Context context) {
		if (database == null) {
			String path = context.getFilesDir().getAbsolutePath() + File.separator + DB_NAME;
			database = SQLiteDatabase.openOrCreateDatabase(path, null);
		}
		return database;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		CityTable.create(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		CityTable.upgrade(db, oldVersion, newVersion);
	}
}
