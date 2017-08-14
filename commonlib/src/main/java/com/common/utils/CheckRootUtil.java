package com.common.utils;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by jacktian on 16/3/8.
 */
public class CheckRootUtil {


	public static boolean isDeviceRooted() {
		if (checkRootMethod1()) {
			return true;
		}
		if (checkRootMethod2()) {
			return true;
		}
		if (checkRootMethod4()) {
			return true;
		}
		if (checkRootMethod3()) {
			return true;
		}
		return false;
	}

	public static boolean checkRootMethod1() {
		String buildTags = android.os.Build.TAGS;
		if (buildTags != null && buildTags.contains("test-keys")) {
			return true;
		}
		return false;
	}

	public static boolean checkRootMethod2() {
		try {
			File file = new File("/system/app/Superuser.apk");
			if (file.exists()) {
				return true;
			}
		} catch (Exception e) {
		}

		return false;
	}

	/**
	 * 判断当前手机是否有ROOT权限
	 *
	 * @return
	 */
	public static boolean checkRootMethod4() {
		boolean bool = false;

		try {
			if ((!new File("/system/bin/su").exists()) && (!new File("/system/xbin/su").exists())) {
				bool = false;
			} else {
				bool = true;
			}
		} catch (Exception e) {

		}
		return bool;
	}

	public static boolean checkRootMethod3() {
		if (executeCommand(SHELL_CMD.check_su_binary) != null) {
			return true;
		} else {
			return false;
		}
	}

	public static enum SHELL_CMD {
		check_su_binary(new String[]{"/system/xbin/which", "su"}),;

		String[] command;

		SHELL_CMD(String[] command) {
			this.command = command;
		}
	}

	public static ArrayList<String> executeCommand(SHELL_CMD shellCmd) {
		String line = null;
		ArrayList<String> fullResponse = new ArrayList<String>();
		Process localProcess = null;

		try {
			localProcess = Runtime.getRuntime().exec(shellCmd.command);
		} catch (Exception e) {
			return null;
			//e.printStackTrace();
		}

		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(localProcess.getOutputStream()));
		BufferedReader in = new BufferedReader(new InputStreamReader(localProcess.getInputStream()));

		try {
			while ((line = in.readLine()) != null) {
				fullResponse.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}


		return fullResponse;
	}
}


