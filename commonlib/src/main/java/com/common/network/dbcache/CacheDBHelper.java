package com.common.network.dbcache;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by songxudong
 * on 2015/5/12.
 */
public class CacheDBHelper extends SQLiteOpenHelper {
	public static final String DATABASE_NAME = "fwjikacache";
	private static final int DATABASE_VERSION = 1;
	private static final String TAG = CacheDBHelper.class.getSimpleName();

	public CacheDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static interface Cache extends BaseColumns {
		String TABLE_NAME = "fcache";
		String COLUMN_ID = _ID;
		String COLUMN_KEY = "key";
		String COLUMN_DATA = "data";
		String COLUMN_LOG = "log";
		String COLUMN_TIME = "time";
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS " + Cache.TABLE_NAME
				+ "("
				+ Cache.COLUMN_ID + " INTEGER  primary key autoincrement,"
				+ Cache.COLUMN_KEY + " TEXT,"
				+ Cache.COLUMN_DATA + " TEXT,"
				+ Cache.COLUMN_LOG + " TEXT,"
				+ Cache.COLUMN_TIME + " INTEGER"
				+ ");");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


	}

}
