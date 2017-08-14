package com.common.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.common.interfaces.IActivityHelper;

import java.io.File;


/**
 * @author songxudong
 */
public class ActivityHelperImp implements IActivityHelper {
	private Activity activity;

	public ActivityHelperImp(Activity activity) {
		this.activity = activity;
	}

	public void goHome() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory(Intent.CATEGORY_HOME);
		activity.startActivity(intent);
	}

	public void tell(String telePhoneNum) {
		if (TextUtils.isEmpty(telePhoneNum)) {
			return;
		}
		try {
			Uri uri = Uri.parse("tel:" + telePhoneNum);
			if (uri != null) {
				// Intent intent = new Intent(Intent.ACTION_CALL, uri);
				Intent intent = new Intent(Intent.ACTION_DIAL, uri);
				activity.startActivity(intent);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(activity, "该设备无法提供电话服务！", Toast.LENGTH_SHORT).show();
		}
	}

	public void sendSms(String telePhoneNum, String msg) {
		if (TextUtils.isEmpty(telePhoneNum)) {
			return;
		}
		Uri smsToUri = Uri.parse("smsto:" + telePhoneNum);
		Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
		intent.putExtra("sms_body", msg);
		activity.startActivity(intent);
	}

	public void installApp(File file) {
		if (file == null || !file.exists()) {
			return;
		}
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		activity.startActivity(intent);
	}

	@Override
	public void recommandToYourFriend(String url, String shareTitle) {
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_TEXT, shareTitle + "   " + url);

		Intent itn = Intent.createChooser(intent, "分享");
		activity.startActivity(itn);
	}

	@Override
	public void hideKeyboard(View view) {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

	@Override
	public void killApp() {
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(0);
	}
}
