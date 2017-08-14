package cn.antke.ezy.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.common.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuzhichao on 2017/3/3.
 * 适配6.0系统，运行时权限
 */
public class PermissionUtils {

	public static boolean isGetPermission(Context context, String permission) {
		return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
	}

	public static void requestPermissions(Activity activity, int requestCode, String ... permissions) {
		if (activity == null || permissions.length < 1) {
			return;
		}
		List<String> neadPermission = getNeedsPermission(activity, permissions);
		if (neadPermission.size() > 0) {
			ActivityCompat.requestPermissions(activity, neadPermission.toArray(new String[]{}), requestCode);
		}
	}

	public static void requestPermissions(Fragment fragment, int requestCode, String ... permissions) {
		if (fragment == null || permissions.length < 1) {
			return;
		}
		List<String> neadPermission = getNeedsPermission(fragment.getActivity(), permissions);
		if (neadPermission.size() > 0) {
			fragment.requestPermissions(neadPermission.toArray(new String[]{}), requestCode);
		}
	}

	@NonNull
	private static List<String> getNeedsPermission(Context context, String[] permissions) {
		List<String> neadPermission = new ArrayList<>();
		for (String permission : permissions) {
			if (!isGetPermission(context, permission)) {
				neadPermission.add(permission);
			}
		}
		return neadPermission;
	}


	public static void secondRequest(Activity activity, int requestCode, String permission) {
		if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
			ToastUtil.longShow(activity, "必须同意权限才能使用");
		}
		requestPermissions(activity, requestCode, permission);
	}

	public static void secondRequest(Fragment fragment, int requestCode, String permission) {
		if (fragment.shouldShowRequestPermissionRationale(permission)) {
			ToastUtil.longShow(fragment.getActivity(), "必须同意权限才能使用");
		}
		requestPermissions(fragment, requestCode, permission);
	}
}
