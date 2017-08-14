package com.common;

/**
 * @author songxudong
 */
public class FConfigs {

	public static boolean downloadOnlyWifi = false;

	public static class State {
		//程序启动时同步一次，以后监听变化
		public static boolean isConnectState = true;
		public static boolean isWifiState = true;
	}
}
