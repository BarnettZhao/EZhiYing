package cn.antke.ezy.pay.util;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.alipay.sdk.app.PayTask;
import com.common.utils.EnCryptionUtils;
import com.common.utils.StringUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import cn.antke.ezy.common.CommonConfig;

/**
 * Created by liuzhichao on 2017/3/16.
 * 支付工具类
 */
public final class PayUtils {

	/**
	 * @param prepayId 微信支付凭证，调起微信支付必要参数
	 */
	public static void startWXPay(Context context, String prepayId) {
		if (context == null || StringUtil.isEmpty(prepayId)) {
			return;
		}
		//注册ID
		IWXAPI wxapi = WXAPIFactory.createWXAPI(context, CommonConfig.WECHAT_APP_ID);

		PayReq request = new PayReq();
		request.appId = CommonConfig.WECHAT_APP_ID;
		request.partnerId = CommonConfig.WECHAT_PARTNER_ID;
		//只有这个参数是通过订单获取的
		request.prepayId = prepayId;
		//随机数
		request.nonceStr = EnCryptionUtils.MD5(String.valueOf(new Random().nextInt(10000)));
		request.packageValue = CommonConfig.WECHAT_PACKAGE_VALUE;
		request.timeStamp = String.valueOf(System.currentTimeMillis() / 1000);

		LinkedHashMap<String, String> params = new LinkedHashMap<>();
		params.put("appid", request.appId);
		params.put("noncestr", request.nonceStr);
		params.put("package", request.packageValue);
		params.put("partnerid", request.partnerId);
		params.put("prepayid", request.prepayId);
		params.put("timestamp", request.timeStamp);

		//签名
		request.sign = genPackageSign(params);
		wxapi.sendReq(request);
	}

	/**
	 * 微信支付生成签名
	 */
	private static String genPackageSign(LinkedHashMap<String, String> params) {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<String, String> entry: params.entrySet()) {
			sb.append(entry.getKey());
			sb.append('=');
			sb.append(entry.getValue());
			sb.append('&');
		}
		//一定要注意这个地方拼的是API密钥不是APPID
		sb.append("key=" + CommonConfig.WECHAT_API_KEY);
		return EnCryptionUtils.MD5(sb.toString()).toUpperCase();
	}

	/**
	 * @param mHandler 支付成功回调消息
	 * @param orderInfo 订单信息
	 */
	public static void startAliPay(Activity activity, Handler mHandler, String orderInfo) {
		if (activity == null || mHandler == null || StringUtil.isEmpty(orderInfo)) {
			return;
		}
		Runnable payRunnable = () -> {
			PayTask alipay = new PayTask(activity);
			Map<String, String> result1 = alipay.payV2(orderInfo, true);

			Message msg = new Message();
			msg.what = 10;
			msg.obj = result1;
			mHandler.sendMessage(msg);
		};
		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}
}
