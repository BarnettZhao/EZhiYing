package cn.antke.ezy.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import cn.antke.ezy.common.CommonConfig;
import cn.antke.ezy.common.CommonConstant;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

	private IWXAPI api;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		api = WXAPIFactory.createWXAPI(this, CommonConfig.WECHAT_APP_ID);
		api.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Intent intent = new Intent(CommonConstant.ACTION_BROADCAST_PAY_RESULT);
		intent.putExtra(CommonConstant.EXTRA_CODE, resp.errCode);
		intent.putExtra(CommonConstant.EXTRA_TYPE, CommonConstant.TYPE_WXPAY);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
		finish();
	}
}