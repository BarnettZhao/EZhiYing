package com.common.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.common.utils.NetWorkUtil;

/**
 * @author songxudong
 */
public class ConnectionChangeReceiver extends BroadcastReceiver {
	private static String TAG = ConnectionChangeReceiver.class.getSimpleName();

	@Override
	public void onReceive(Context context, Intent intent) {
		NetWorkUtil.syncConnectState(context);
	}

}
