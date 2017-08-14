package com.common.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by songxudong
 * on 2015/7/24.
 */
public class LastModified {
	private static final String LAST_MODIFIED_SP = "last_modified_sp";

	public static void saveLastModified(Context context, String key, String lastModified) {
		SharedPreferences sp = context.getSharedPreferences(LAST_MODIFIED_SP,
				Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.putString(key, lastModified);
		editor.commit();
	}

	public static String getLastModified(Context context, String key) {
		SharedPreferences sp = context.getSharedPreferences(LAST_MODIFIED_SP,
				Context.MODE_PRIVATE);
		return sp.getString(key, "");
	}

}
