package com.common.utils;

import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @author songxudong
 */
public class DeviceUtil {
	private static int height = -1;
	private static int width = -1;
	private static float densityDpi = -1;
	private static float density = -1;
	//<uses-permission android:name="android.permission.READ_PHONE_STATE" />
	private static String deviceId = "";
	private static String model = android.os.Build.MODEL;
	private static String manufacturer = android.os.Build.MANUFACTURER;
	public static String IMEI_KEY = "imei_key";
	public static String ANDROID_ID_KEY = "android_id_key";
	public static String BLUETOOTH_MAC_KEY = "bluetooth_mac_key";
	public static String BOOT_SERIALNO_KEY = "boot_serialno_key";
	public static final String BOOT_SERIALNO = "ro.boot.serialno";
	public static final String SERIALNO = "ro.serialno";
	public static final String MD5_SIGNATURE = "96:A4:6E:7D:9B:9A:00:47:B9:EF:6E:85:74:3F:F6:3F";
	public static final String SHA1_SIGNATURE = "FD:28:C3:F9:4D:AF:73:A3:38:FF:7B:7E:2A:45:24:E9:1C:76:3A:AF";
	public static final String SHA256_SIGNATURE = "3A:4C:84:36:97:C6:58:D5:FA:D3:A8:5D:00:1C:3D:D1:7E:03:5F:7D:36:91:19:5F:AB:B1:02:EA:11:DA:E5:E0";
	public static final String PACKAGE_NAME = "cn.antke.bct";


	/**
	 * 当前设备的 IMEI
	 */
	public static String IMEI;

	/**
	 * 当前设备的 IMEI
	 */
	public static String MD5_KEY = "D93569608DFED17BA63EF17CCC60E93D";

	public static boolean isEmulator(Context context) {
		String deviceId = getDeviceId(context);
		String serialNO = getBootSerialno(context);
		if (StringUtil.isEmpty(deviceId) || StringUtil.isEmpty(serialNO) || StringUtil.isEmpty(Build.MODEL)) {
			return true;
		}
		if ((deviceId != null && deviceId.equals("000000000000000")) || (serialNO != null && serialNO.equals("000000000000000"))) {
			return true;
		} else if (Build.MODEL != null && (Build.MODEL.contains("sdk") || Build.MODEL.contains("sdk_google"))) {
			return true;

		}
		return false;
	}

	public static String getDeviceId(Context context) {
		if (TextUtils.isEmpty(deviceId)) {
			getValues(context);
		}
		return deviceId;
	}

	public static int getHeight(Context context) {
		if (height < 0) {
			getValues(context);
		}
		return height;
	}

	public static int getWidth(Context context) {
		if (width < 0) {
			getValues(context);
		}
		return width;
	}

	public static String getModel() {
		return model;
	}

	public static String getManufacturer() {
		return manufacturer;
	}

	public static float getDensityDpi(Context context) {
		if (densityDpi < 0) {
			getValues(context);
		}
		return densityDpi;
	}

	public static float getDensity(Context context) {
		if (density < 0) {
			getValues(context);
		}
		return density;
	}

	private static void getValues(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		deviceId = tm.getDeviceId();
		DisplayMetrics display = context.getResources().getDisplayMetrics();
		width = display.widthPixels;
		height = display.heightPixels;
		densityDpi = display.densityDpi;
		density = display.density;
		/*
		 * System.out.println("width"+width);
		 * System.out.println("height"+height);
		 * System.out.println("densityDpi"+densityDpi);
		 * System.out.println("density"+density);
		 */
	}

	public static int px_to_dp(Context context, int pxValue) {
		return (int) (pxValue / getDensity(context) + 0.5f);
	}

	public static int dp_to_px(Context context, int dpValue) {
		return (int) (getDensity(context) * dpValue + 0.5f);
	}

	public static boolean existSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		} else
			return false;
	}

	public static String getAppUniqueToken(Context context) {
		try {
			IMEI = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		} catch (Exception e) {
			LogUtil.e("Envi", e.toString());
		}
		String result = IMEI;
		String androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(),
				android.provider.Settings.Secure.ANDROID_ID);
		if (IMEI == null || "".equals(IMEI)) {
			result = androidId;
		} else {
			result = result + androidId;
		}
		result = result + MD5_KEY;
		return result;
	}

	/**
	 * 程序是否在前台运行
	 *
	 * @return
	 */
	public static boolean isAppOnForeground(Context context) {
		// Returns a list of application processes that are running on the
		// device

		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = context.getPackageName();

		List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();
		if (appProcesses == null)
			return false;

		for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {

			// The name of the process that this object is associated with.
			if (appProcess.processName.equals(packageName)
					&& appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 检测某ActivityUpdate是否在当前Task的栈顶
	 */
	public static boolean isTopActivy(Context context) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = context.getPackageName();
		String bingMapClassName = "com.agero.bluelink.BingMapActivity";
		List<ActivityManager.RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
		if (tasksInfo.size() > 0) {
			ComponentName topConponent = tasksInfo.get(0).topActivity;
			if (packageName.equals(topConponent.getPackageName())) {
				//当前的APP在前台运行
				if (topConponent.getClassName().equals(bingMapClassName)) {
					//当前正在运行的是不是期望的Activity
				} else {
				}
				return true;
			} else {
				//当前的APP在后台运行
				return false;
			}
		}
		return false;
	}

	public static void setDeviceIdData(Context context) {
		PreferencesUtils.putString(context, IMEI_KEY, getDeviceId(context));
	}


	public static String getDeviceIdData(Context context) {
		String deviceId = PreferencesUtils.getString(context, IMEI_KEY, "");
		if (StringUtil.isEmpty(deviceId)) {
			deviceId = getDeviceId(context);
			setDeviceIdData(context);
		}
		return deviceId;
	}

	public static String getBootSerialno(Context context) {
		String bootSerialno = PreferencesUtils.getString(context, BOOT_SERIALNO_KEY, "");
		if (StringUtil.isEmpty(bootSerialno)) {
			bootSerialno = getAndroidOsSystemProperties(BOOT_SERIALNO);
			PreferencesUtils.putString(context, BOOT_SERIALNO_KEY, bootSerialno);
		}
		return bootSerialno;
	}

	public static String getSerialno() {
		return getAndroidOsSystemProperties(SERIALNO);
	}

	public static String getBluetoothMac(Context context) {
		String bluetoothMac = PreferencesUtils.getString(context, BLUETOOTH_MAC_KEY, "");
		try {
			if (StringUtil.isEmpty(bluetoothMac)) {
				bluetoothMac = BluetoothAdapter.getDefaultAdapter().getAddress();
				PreferencesUtils.putString(context, BLUETOOTH_MAC_KEY, bluetoothMac);
			}
		} catch (Exception e) {

		}

		return bluetoothMac;
	}

	public static String getAndroidId(Context context) {
		String androidId = PreferencesUtils.getString(context, ANDROID_ID_KEY, "");
		if (StringUtil.isEmpty(androidId)) {
			androidId = android.provider.Settings.Secure.getString(context.getContentResolver(),
					android.provider.Settings.Secure.ANDROID_ID);
			PreferencesUtils.putString(context, ANDROID_ID_KEY, androidId);
		}
		return androidId;
	}

	public static String getAppMD5Signature(Context context) {
		try {
			PackageInfo packageInfo = context.getPackageManager().getPackageInfo(PACKAGE_NAME, PackageManager.GET_SIGNATURES);
			/******* 通过返回的包信息获得签名数组 *******/
			Signature[] signatures = packageInfo.signatures;

			for (Signature signature : signatures) {

				MessageDigest md = null;
				try {
					md = MessageDigest.getInstance("MD5");
					md.update(signature.toByteArray());
					byte[] digest = md.digest();
					String res = toHexString(digest);
					return res;
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				}

			}


		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

    /*

* Converts a byte array to hex string

*/

	private static String toHexString(byte[] block) {

		StringBuffer buf = new StringBuffer();
		int len = block.length;
		for (int i = 0; i < len; i++) {
			byte2hex(block[i], buf);
			if (i < len - 1) {
				buf.append(":");
			}
		}

		return buf.toString();

	}

	private static void byte2hex(byte b, StringBuffer buf) {

		char[] hexChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
				'9', 'A', 'B', 'C', 'D', 'E', 'F'};

		int high = ((b & 0xf0) >> 4);
		int low = (b & 0x0f);
		buf.append(hexChars[high]);
		buf.append(hexChars[low]);

	}

	public static String getAppPkgName(Context context) {
		ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
		List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
		return context.getPackageName();
	}

	static String getAndroidOsSystemProperties(String key) {
		String ret;
		try {
			Method systemProperties_get = Class.forName("android.os.SystemProperties").getMethod("get", String.class);
			if ((ret = (String) systemProperties_get.invoke(null, key)) != null)
				return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return "";
	}
}
