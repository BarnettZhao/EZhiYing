package com.common.interfaces;

import android.view.View;

import java.io.File;

/**
 * @author songxudong activity或fragment基类接口，为子类提供的工具方法
 */
public interface IActivityHelper {

	void goHome();

	void tell(String telePhoneNum);

	void sendSms(String telePhoneNum, String msg);

	void installApp(File file);

	void recommandToYourFriend(String url, String shareTitle);

	void hideKeyboard(View view);

	void killApp();

}
