package cn.antke.ezy.pay.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;

import java.util.IdentityHashMap;
import java.util.Map;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.common.CommonConstant;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.Entity;
import cn.antke.ezy.network.entities.PayEntity;
import cn.antke.ezy.pay.util.PayUtils;
import cn.antke.ezy.utils.ExitManager;
import cn.antke.ezy.utils.ViewInjectUtils;

/**
 * Created by liuzhichao on 2017/5/21.
 * 在线支付
 */
public class OnlinePayActivity extends ToolBarActivity implements View.OnClickListener {

	@ViewInject(R.id.tv_online_wxpay)
	private View tvOnlineWxpay;
	@ViewInject(R.id.tv_online_alipay)
	private View tvOnlineAlipay;
	@ViewInject(R.id.tv_online_postage)
	private TextView tvOnlinePostage;
	@ViewInject(R.id.tv_online_pay)
	private View tvOnlinePay;

	private String orderId;
	private String amount;
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			Map<String, String> result = (Map<String, String>) msg.obj;
			String resultStatus = "";
			for (String key : result.keySet()) {
				if (TextUtils.equals(key, "resultStatus")) {
					resultStatus = result.get(key);
				}
			}
			/*
			 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
			 */
			// 判断resultStatus 为9000则代表支付成功
			Intent intent = new Intent(CommonConstant.ACTION_BROADCAST_PAY_RESULT);
			intent.putExtra(CommonConstant.EXTRA_TYPE, CommonConstant.TYPE_ALIPAY);
			if (TextUtils.equals(resultStatus, "9000")) {
				// 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
				intent.putExtra(CommonConstant.EXTRA_CODE, 0);
			} else {
				// 该笔订单真实的支付结果，需要依赖服务端的异步通知。
				intent.putExtra(CommonConstant.EXTRA_CODE, -1);
			}
			LocalBroadcastManager.getInstance(OnlinePayActivity.this).sendBroadcast(intent);
		}
	};
	private PayEntity pay;

	public static void startOnlinePayActivity(Context context, String orderId, String amount) {
		Intent intent = new Intent(context, OnlinePayActivity.class);
		intent.putExtra(CommonConstant.EXTRA_ID, orderId);
		intent.putExtra(CommonConstant.EXTRA_AMOUNT, amount);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_online_pay);
		ExitManager.instance.addBuyNowActivity(this);
		ViewInjectUtils.inject(this);
		initView();
	}

	private void initView() {
		setLeftTitle(getString(R.string.online_pay));
		orderId = getIntent().getStringExtra(CommonConstant.EXTRA_ID);
		amount = getIntent().getStringExtra(CommonConstant.EXTRA_AMOUNT);

		tvOnlinePostage.setText("总计：" + amount + "元");

		tvOnlineWxpay.setSelected(true);

		tvOnlineWxpay.setOnClickListener(this);
		tvOnlineAlipay.setOnClickListener(this);
		tvOnlinePay.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tv_online_wxpay:
				tvOnlineWxpay.setSelected(true);
				tvOnlineAlipay.setSelected(false);
				break;
			case R.id.tv_online_alipay:
				tvOnlineWxpay.setSelected(false);
				tvOnlineAlipay.setSelected(true);
				break;
			case R.id.tv_online_pay:
				BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
					@Override
					public void onReceive(Context context, Intent intent) {
						//BroadcastReceiver是否需要变成全局变量
						LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
						int intExtra = intent.getIntExtra(CommonConstant.EXTRA_CODE, -1);
						switch (intExtra) {
							case 0:
								ToastUtil.shortShow(context, "支付成功");
								PayResultActivity.startPayResultActivity(OnlinePayActivity.this, pay.getTotal(), pay.getPayWay(), intent.getIntExtra(CommonConstant.EXTRA_TYPE, 0));
								ExitManager.instance.closeBuyNowActivity();
								break;
							case -1:
								ToastUtil.shortShow(context, "支付失败");
								break;
							case -2:
								ToastUtil.shortShow(context, "取消支付");
								break;
						}
					}
				};
				IntentFilter intentFilter = new IntentFilter(CommonConstant.ACTION_BROADCAST_PAY_RESULT);
				LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter);
				showProgressDialog();
				IdentityHashMap<String, String> params = new IdentityHashMap<>();
				params.put("order_id", orderId);
				if (tvOnlineWxpay.isSelected()) {
					params.put("pay_type", "2");//1：支付宝、2：微信
				} else {
					params.put("pay_type", "1");
				}
				requestHttpData(Constants.Urls.URL_POST_PAYMENT, CommonConstant.REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
				break;
		}
	}

	@Override
	public void success(int requestCode, String data) {
		showProgressDialog();
		super.success(requestCode, data);
		Entity result = Parsers.getResult(data);
		if (CommonConstant.REQUEST_NET_SUCCESS.equals(result.getResultCode())) {
			pay = Parsers.getPay(data);
			if (tvOnlineWxpay.isSelected()) {
				PayUtils.startWXPay(this, pay.getPayId());
			} else {
				PayUtils.startAliPay(this, handler, pay.getPayId());
			}
		} else {
			ToastUtil.shortShow(this, result.getResultMsg());
		}
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		showProgressDialog();
		super.mistake(requestCode, status, errorMessage);
	}
}
