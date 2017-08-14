package cn.antke.ezy.db;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by liuzhichao on 2017/2/22.
 * 城市数据表
 */
class CityTable {

	static final String TABLE_NAME = "gen_area";//表名
	static final String COLUMN_AREA_ID = "AREA_ID";//主键
	static final String COLUMN_AREA_NO = "AREA_NO";
	static final String COLUMN_AREA_NAME = "AREA_NAME";
	static final String COLUMN_AREA_SHORTNAME = "AREA_SHORTNAME";
	static final String COLUMN_AREA_FULLSPELL = "AREA_FULLSPELL";
	static final String COLUMN_AREA_SHORTSPELL = "AREA_SHORTSPELL";
	static final String COLUMN_AREA_CODE = "AREA_CODE";
	static final String COLUMN_AREA_PARENTNO = "AREA_PARENTNO";
	static final String COLUMN_AREA_OLDNO = "AREA_OLDNO";
	static final String COLUMN_AREA_RANK = "AREA_RANK";
	static final String COLUMN_AREA_ZIPCODE = "AREA_ZIPCODE";

	private static final String DATABASE_CREATE = "create table " + TABLE_NAME +
			"(" + COLUMN_AREA_ID + "INTEGER PRIMARY KEY," +
			COLUMN_AREA_NO + "TEXT," +
			COLUMN_AREA_NAME + "TEXT," +
			COLUMN_AREA_SHORTNAME + "TEXT," +
			COLUMN_AREA_FULLSPELL + "TEXT," +
			COLUMN_AREA_SHORTSPELL + "TEXT," +
			COLUMN_AREA_CODE + "TEXT," +
			COLUMN_AREA_PARENTNO + "TEXT," +
			COLUMN_AREA_OLDNO + "TEXT," +
			COLUMN_AREA_RANK + "INTEGER," +
			COLUMN_AREA_ZIPCODE + "TEXT);";

	public static void create(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	static void upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		create(db);
	}
}
