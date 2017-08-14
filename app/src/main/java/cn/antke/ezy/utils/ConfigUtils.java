package cn.antke.ezy.utils;

import android.content.Context;

import com.common.utils.PreferencesUtils;

/**
 * Created by jacktian on 15/9/24.
 * 配置信息
 */
public class ConfigUtils {

	private static final String CONFIG_KEY_PRE_LANGUAGE = "key_pre_language";
	private static final String CONFIG_KEY_PRE_LANGUAGE_NAME = "key_pre_language_name";
	private static final String CONFIG_KEY_PRE_AREA = "key_pre_area";

	public static void setPreLanguage(Context context, String language) {
		PreferencesUtils.putString(context, CONFIG_KEY_PRE_LANGUAGE, language);
	}

	public static String getPreLanguage(Context context) {
		return PreferencesUtils.getString(context, CONFIG_KEY_PRE_LANGUAGE);
	}

	public static void setPreArea(Context context, String area) {
		PreferencesUtils.putString(context, CONFIG_KEY_PRE_AREA, area);
	}

	public static String getPreArea(Context context) {
		return PreferencesUtils.getString(context, CONFIG_KEY_PRE_AREA);
	}

//	public static String getPreAreaName(Context context) {
//		return PreferencesUtils.getString(context, CONFIG_KEY_PRE_LANGUAGE_NAME, context.getString(R.string.mainland));
//	}
//
//	public static void setPreAreaName(Context context, String areaName) {
//		PreferencesUtils.putString(context, CONFIG_KEY_PRE_LANGUAGE_NAME, areaName);
//	}
}
