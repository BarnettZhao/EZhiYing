package cn.antke.ezy.deal.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.common.CommonConstant;
import cn.antke.ezy.login.utils.UserCenter;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.DealConditionEntity;
import cn.antke.ezy.network.entities.Entity;
import cn.antke.ezy.network.entities.PayEntity;
import cn.antke.ezy.pay.util.PayUtils;
import cn.antke.ezy.utils.ViewInjectUtils;
import cn.antke.ezy.widget.dialogplus.DialogPlus;

/**
 * Created by liuzhichao on 2017/5/12.
 * 交易大厅续费
 */
public class DealRechargeActivity extends ToolBarActivity implements View.OnClickListener {

	@ViewInject(R.id.ll_recharge_mode)
	private View llRechargeMode;
	@ViewInject(R.id.tv_recharge_way)
	private TextView tvRechargeWay;
	@ViewInject(R.id.tv_recharge_price)
	private TextView tvRechargePrice;
	@ViewInject(R.id.tv_recharge_wxpay)
	private View tvRechargeWxpay;
	@ViewInject(R.id.tv_recharge_alipay)
	private View tvRechargeAlipay;
	@ViewInject(R.id.tv_recharge_pay)
	private View tvRechargePay;

	private List<String> fees = new ArrayList<>();
	private DealConditionEntity dealCondition;
	private String feeType;

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
			if (TextUtils.equals(resultStatus, "9000")) {
				// 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
				setResult(RESULT_OK);
				finish();
			} else {
				// 该笔订单真实的支付结果，需要依赖服务端的异步通知。
				ToastUtil.shortShow(DealRechargeActivity.this, getString(R.string.pay_failed));
			}
		}
	};

	public static void startDealRechargeActivity(Context context) {
		Intent intent = new Intent(context, DealRechargeActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_deal_recharge);
		ViewInjectUtils.inject(this);
		initView();
		loadData();
	}

	private void initView() {
		setLeftTitle(getString(R.string.renew));
		tvRechargeWxpay.setSelected(true);

		llRechargeMode.setOnClickListener(this);
		tvRechargeWxpay.setOnClickListener(this);
		tvRechargeAlipay.setOnClickListener(this);
		tvRechargePay.setOnClickListener(this);
	}

	private void loadData() {
		showProgressDialog();
		IdentityHashMap<String, String> params = new IdentityHashMap<>();
		params.put("tradeHall_site_id", UserCenter.getUserSiteId(this));
		requestHttpData(Constants.Urls.URL_POST_DEAL_INFO, CommonConstant.REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
	}

	@Override
	public void success(int requestCode, String data) {
		closeProgressDialog();
		Entity result = Parsers.getResult(data);
		if (CommonConstant.REQUEST_NET_SUCCESS.equals(result.getResultCode())) {
			switch (requestCode) {
				case CommonConstant.REQUEST_NET_ONE:
					dealCondition = Parsers.getDealCondition(data);
					if (dealCondition != null) {
						//费用和价格显示
						List<DealConditionEntity.Admission> admissionList = dealCondition.getAdmissionList();
						if (admissionList != null && admissionList.size() > 0) {
							DealConditionEntity.Admission admission = admissionList.get(0);
							feeType = admission.getAdmissionType();
							if ("1".equals(feeType)) {
								tvRechargeWay.setText("个人");
							} else {
								tvRechargeWay.setText("企业");
							}
							tvRechargePrice.setText(admission.getAdmissionMoney());

							for (DealConditionEntity.Admission admission1 : admissionList) {
								if ("1".equals(admission1.getAdmissionType())) {
									fees.add("个人");
								} else {
									fees.add("企业");
								}
							}
						}
					}
					break;
				case CommonConstant.REQUEST_NET_TWO:
					PayEntity pay = Parsers.getPay(data);
					if (tvRechargeWxpay.isSelected()) {
						PayUtils.startWXPay(this, pay.getPayId());
					} else {
						PayUtils.startAliPay(this, handler,pay.getPayId());
					}
					break;
			}
		} else {
			ToastUtil.shortShow(this, result.getResultMsg());
		}
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		closeProgressDialog();
		super.mistake(requestCode, status, errorMessage);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ll_recharge_mode:
				if (fees.size() > 0) {
					ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, fees);
					DialogPlus dialogPlus = DialogPlus.newDialog(this).setAdapter(adapter).setGravity(Gravity.CENTER)
							.setOnItemClickListener((dialog, item, view, position) -> {
								String fee;
								if ("个人".equals(fees.get(position))) {
									fee = "1";
								} else {
									fee = "2";
								}
								if (dealCondition != null) {
									List<DealConditionEntity.Admission> admissionList = dealCondition.getAdmissionList();
									for (DealConditionEntity.Admission admission : admissionList) {
										if (fee.equals(admission.getAdmissionType())) {
											feeType = admission.getAdmissionType();
											if ("1".equals(feeType)) {
												tvRechargeWay.setText("个人");
											} else {
												tvRechargeWay.setText("企业");
											}
											tvRechargePrice.setText(admission.getAdmissionMoney());
										}
									}
								}
								dialog.dismiss();
							}).create();
					dialogPlus.show();
				}
				break;
			case R.id.tv_recharge_wxpay:
				tvRechargeWxpay.setSelected(true);
				tvRechargeAlipay.setSelected(false);
				break;
			case R.id.tv_recharge_alipay:
				tvRechargeWxpay.setSelected(false);
				tvRechargeAlipay.setSelected(true);
				break;
			case R.id.tv_recharge_pay:
				showProgressDialog();
				IdentityHashMap<String, String> params = new IdentityHashMap<>();
				params.put("fee_type", feeType);
				if (tvRechargeWxpay.isSelected()) {
					params.put("pay_type", "2");
				} else {
					params.put("pay_type", "1");
				}
				requestHttpData(Constants.Urls.URL_POST_PAY_DEAL, CommonConstant.REQUEST_NET_TWO, FProtocol.HttpMethod.POST, params);
				break;
		}
	}
}
