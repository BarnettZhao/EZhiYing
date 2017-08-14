package com.common.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import static com.common.FConfigs.State;


/**
 * @author songxudong
 *         检查网络连接工具类
 */
public class NetWorkUtil {
	public static boolean isConnect(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			return true;
		}
		return false;
	}

	public static boolean isWifiConnect(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiInfo != null && wifiInfo.isConnected()) {
			return true;
		}
		return false;
	}

	public static boolean isMobileConnect(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobileNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (mobileNetInfo != null && mobileNetInfo.isConnected()) {
			return true;
		}
		return false;
	}

	/**
	 * 将wifi状态保存在变量里
	 *
	 * @param context
	 */
	public static void syncConnectState(Context context) {
		State.isConnectState = isConnect(context);
		State.isWifiState = isWifiConnect(context);
	}

	public static String getLocalIpAddress(Context context) {
		if (isWifiConnect(context)) {
			//获取wifi服务
			WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			int ipAddress = wifiInfo.getIpAddress();
			return (ipAddress & 0xFF) + "." +
					((ipAddress >> 8) & 0xFF) + "." +
					((ipAddress >> 16) & 0xFF) + "." +
					(ipAddress >> 24 & 0xFF);
		} else {

		}
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			Log.e("WifiPreferenceIpAddress", ex.toString());
		}
		return null;
	}
}
