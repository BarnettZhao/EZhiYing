package cn.antke.ezy.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import cn.antke.ezy.network.entities.CityEntity;
import cn.antke.ezy.utils.StreamUtil;

/**
 * Created by liuzhichao on 2017/2/22.
 * 城市数据库管理
 */
public class CityDBManager {

	/**
	 * 导入城市数据库到sql
	 */
	public static void introducedCityDB(Context context) {
		try {
			InputStream inputStream = context.getAssets().open(CityDBHelper.DB_NAME);
			StreamUtil.saveStreamToFile(inputStream, context.getFilesDir().getAbsolutePath() + File.separator + CityDBHelper.DB_NAME);
		} catch (IOException e) {
			e.printStackTrace();
		}
		CityDBHelper cityDBHelper = CityDBHelper.getInstence(context);
		cityDBHelper.openOrCreateDB(context);
	}

	/**
	 * @return 获取所有省
	 */
	public static List<CityEntity> getAllProvince(Context context) {
		List<CityEntity> provinceList = new ArrayList<>();
		Cursor cursor = null;
		try {
			SQLiteDatabase database = CityDBHelper.getDatabase(context);
			cursor = database.query(CityTable.TABLE_NAME, null, CityTable.COLUMN_AREA_RANK + " = ?", new String[]{"1"}, null, null, CityTable.COLUMN_AREA_ID);
			while (cursor.moveToNext()) {
				String no = cursor.getString(cursor.getColumnIndex(CityTable.COLUMN_AREA_NO));
				String province = cursor.getString(cursor.getColumnIndex(CityTable.COLUMN_AREA_NAME));
				if (!TextUtils.isEmpty(province)) {
					provinceList.add(new CityEntity(no, province));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return provinceList;
	}

	/**
	 * @param provinceNo 省id
	 * @return 获取某个省下的所有市
	 */
	public static List<CityEntity> getCityByProvince(Context context, String provinceNo) {
		List<CityEntity> cityList = new ArrayList<>();
		Cursor cursor = null;
		try {
			SQLiteDatabase database = CityDBHelper.getDatabase(context);
			cursor = database.query(CityTable.TABLE_NAME, null, CityTable.COLUMN_AREA_PARENTNO + " = ?", new String[]{provinceNo}, null, null, CityTable.COLUMN_AREA_ID);
			if (cursor.moveToFirst()) {
				do {
					String no = cursor.getString(cursor.getColumnIndex(CityTable.COLUMN_AREA_NO));
					String city = cursor.getString(cursor.getColumnIndex(CityTable.COLUMN_AREA_NAME));
					if (!TextUtils.isEmpty(city)) {
						cityList.add(new CityEntity(no, city));
					}
				} while (cursor.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return cityList;
	}

	/**
	 *
	 * @param cityNo 市id
	 * @return 获取某个市下面的所有区
	 */
	public static List<CityEntity> getDistrictByCity(Context context, String cityNo) {
		List<CityEntity> districtList = new ArrayList<>();
		Cursor cursor = null;
		try {
			SQLiteDatabase database = CityDBHelper.getDatabase(context);
			cursor = database.rawQuery("select * from " + CityTable.TABLE_NAME + " where " + CityTable.COLUMN_AREA_PARENTNO + " = ?", new String[]{cityNo});
			if (cursor != null && cursor.getCount() > 0) {
				while (cursor.moveToNext()) {
					String no = cursor.getString(cursor.getColumnIndex(CityTable.COLUMN_AREA_NO));
					String district = cursor.getString(cursor.getColumnIndex(CityTable.COLUMN_AREA_NAME));
					if (!TextUtils.isEmpty(district)) {
						districtList.add(new CityEntity(no, district));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return districtList;
	}
}
