package com.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

public class PreferencesUtils {

	public static String PREFERENCE_NAME = "VersionUpdate";

	/**
	 * put boolean preferences
	 *
	 * @param context
	 * @param key     The name of the preference to modify
	 * @param value   The new value for the preference
	 * @return True if the new values were successfully written to persistent storage.
	 */
	public static boolean putBoolean(Context context, String key, boolean value) {
		SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(key, value);
		return editor.commit();
	}

	/**
	 * get boolean preferences
	 *
	 * @param context
	 * @param key     The name of the preference to retrieve
	 * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
	 * name that is not a long
	 * @see #getBoolean(Context, String, boolean)
	 */
	public static boolean getBoolean(Context context, String key) {
		return getBoolean(context, key, false);
	}

	/**
	 * get boolean preferences
	 *
	 * @param context
	 * @param key          The name of the preference to retrieve
	 * @param defaultValue Value to return if this preference does not exist
	 * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
	 * this name that is not a long
	 */
	public static boolean getBoolean(Context context, String key, boolean defaultValue) {
		SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		return settings.getBoolean(key, defaultValue);
	}

	/**
	 * put int preferences
	 *
	 * @param context
	 * @param key     The name of the preference to modify
	 * @param value   The new value for the preference
	 * @return True if the new values were successfully written to persistent storage.
	 */
	public static boolean putInt(Context context, String key, int value) {
		SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(key, value);

		return editor.commit();
	}

	/**
	 * get int preferences
	 *
	 * @param context
	 * @param key     The name of the preference to retrieve
	 * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
	 * name that is not a long
	 * @see #getInt(Context, String, int)
	 */
	public static int getInt(Context context, String key) {
		return getInt(context, key, -1);
	}

	/**
	 * get long preferences
	 *
	 * @param context
	 * @param key          The name of the preference to retrieve
	 * @param defaultValue Value to return if this preference does not exist
	 * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
	 * this name that is not a long
	 */
	public static int getInt(Context context, String key, int defaultValue) {
		SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		return settings.getInt(key, defaultValue);
	}


	/**
	 * put long preferences
	 *
	 * @param context
	 * @param key     The name of the preference to modify
	 * @param value   The new value for the preference
	 * @return True if the new values were successfully written to persistent storage.
	 */
	public static boolean putLong(Context context, String key, long value) {
		SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong(key, value);

		return editor.commit();
	}

	/**
	 * get long preferences
	 *
	 * @param context
	 * @param key     The name of the preference to retrieve
	 * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
	 * name that is not a long
	 * @see #getLong(Context, String, long)
	 */
	public static long getLong(Context context, String key) {
		return getLong(context, key, -1);
	}

	/**
	 * get long preferences
	 *
	 * @param context
	 * @param key          The name of the preference to retrieve
	 * @param defaultValue Value to return if this preference does not exist
	 * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
	 * this name that is not a long
	 */
	public static long getLong(Context context, String key, long defaultValue) {
		SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		return settings.getLong(key, defaultValue);
	}

	/**
	 * put String preferences
	 *
	 * @param context
	 * @param key     The name of the preference to modify
	 * @param value   The new value for the preference
	 * @return True if the new values were successfully written to persistent storage.
	 */
	public static boolean putString(Context context, String key, String value) {
		SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value);
		return editor.commit();
	}

	/**
	 * get String preferences
	 *
	 * @param context
	 * @param key     The name of the preference to retrieve
	 * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
	 * name that is not a long
	 * @see #getString(Context, String)
	 */
	public static String getString(Context context, String key) {
		return getString(context, key, "");
	}

	/**
	 * get String preferences
	 *
	 * @param context
	 * @param key          The name of the preference to retrieve
	 * @param defaultValue Value to return if this preference does not exist
	 * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
	 * this name that is not a long
	 */
	public static String getString(Context context, String key, String defaultValue) {
		SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		if (settings == null) {
			return defaultValue;
		}
		return settings.getString(key, defaultValue);
	}


	/**
	 * put String list preferences
	 *
	 * @param context
	 * @param key     The name of the preference to modify
	 * @param value   The new value for the preference
	 * @return True if the new values were successfully written to persistent storage.
	 */
	public static boolean putStringList(Context context, String key, Set<String> value) {
		SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putStringSet(key, value);
		return editor.commit();
	}

	/**
	 * get String preferences
	 *
	 * @param context
	 * @param key     The name of the preference to retrieve
	 * @return The preference value if it exists, or -1. Throws ClassCastException if there is a preference with this
	 * name that is not a long
	 * @see #getString(Context, String)
	 */
	public static Set<String> getStringList(Context context, String key) {
		return getStringList(context, key, null);
	}

	/**
	 * get String preferences
	 *
	 * @param context
	 * @param key          The name of the preference to retrieve
	 * @param defaultValue Value to return if this preference does not exist
	 * @return The preference value if it exists, or defValue. Throws ClassCastException if there is a preference with
	 * this name that is not a long
	 */
	public static Set<String> getStringList(Context context, String key, Set<String> defaultValue) {
		SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		return settings.getStringSet(key, defaultValue);
	}


	/**
	 * remove obj in preferences
	 *
	 * @param context
	 * @param key
	 * @return
	 */
	public static boolean removeSharedPreferenceByKey(Context context, String key) {
		SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.remove(key);
		return editor.commit();
	}

	public static boolean hasSharedPreferenceKey(Context context, String key) {
		SharedPreferences settings = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		return settings.contains(key);
	}
}
