package cn.antke.ezy.deal.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.ToastUtil;
import com.common.viewinject.annotation.ViewInject;

import java.util.IdentityHashMap;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.common.CommonConstant;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.DealBuySellEntity;
import cn.antke.ezy.network.entities.Entity;
import cn.antke.ezy.utils.DialogUtils;
import cn.antke.ezy.utils.ViewInjectUtils;

/**
 * Created by liuzhichao on 2017/5/12.
 * 购买积分
 */
public class BuyIntegralActivity extends ToolBarActivity {

	@ViewInject(R.id.tv_buy_integral_all)
	private TextView tvBuyIntegralAll;
	@ViewInject(R.id.tv_buy_integral_unit)
	private TextView tvBuyIntegralUnit;
	@ViewInject(R.id.tv_buy_integral_price)
	private TextView tvBuyIntegralPrice;
	@ViewInject(R.id.et_buy_integral_num)
	private EditText etBuyIntegralNum;
	@ViewInject(R.id.tv_buy_integral_amount)
	private TextView tvBuyIntegralAmount;
	@ViewInject(R.id.tv_buy_integral_buy)
	private View tvBuyIntegralBuy;

	private String num = "";

	public static void startBuyIntegralActivity(Context context, Fragment fragment, int requestCode) {
		Intent intent = new Intent(context, BuyIntegralActivity.class);
		fragment.startActivityForResult(intent, requestCode);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_buy_integral);
		ViewInjectUtils.inject(this);
		initView();
		loadData();
	}

	private void initView() {
		setLeftTitle(getString(R.string.purchase));
		tvBuyIntegralBuy.setOnClickListener(v -> {
			String temp = etBuyIntegralNum.getText().toString();
			if (TextUtils.isEmpty(temp)) {
				ToastUtil.shortShow(this, getString(R.string.please_input_buy_integral));
				return;
			}
			int buyNum = Integer.parseInt(temp);
			if (buyNum <= 0) {
				ToastUtil.shortShow(this, getString(R.string.buy_integral_greater_than_zero));
				return;
			}
			num = temp;
			DialogUtils.showPwdInputDialog(this, (v1, editText) -> {
				String pwd = editText.getText().toString();
				showProgressDialog();
				IdentityHashMap<String, String> params = new IdentityHashMap<>();
				params.put("integral_password", pwd);
				params.put("purchase_num", num);
				requestHttpData(Constants.Urls.URL_POST_BUY_INTEGRAL, CommonConstant.REQUEST_NET_TWO, FProtocol.HttpMethod.POST, params);
				DialogUtils.closeDialog();
			}, new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					DialogUtils.closeDialog();
				}
			});
		});
	}

	private void loadData() {
		showProgressDialog();
		requestHttpData(Constants.Urls.URL_POST_DEAL_CONDITION, CommonConstant.REQUEST_NET_ONE, FProtocol.HttpMethod.POST, null);
	}

	@Override
	public void success(int requestCode, String data) {
		closeProgressDialog();
		Entity result = Parsers.getResult(data);
		if (CommonConstant.REQUEST_NET_SUCCESS.equals(result.getResultCode())) {
			switch (requestCode) {
				case CommonConstant.REQUEST_NET_ONE:
					DealBuySellEntity dealBuySell = Parsers.getDealBuySell(data);
					if (dealBuySell != null) {
						tvBuyIntegralAll.setText(String.valueOf(dealBuySell.getVolume()));
						tvBuyIntegralUnit.setText(String.valueOf(dealBuySell.getMaxUsageIntegral()));
						tvBuyIntegralPrice.setText(String.valueOf(dealBuySell.getUserMultifunctional()));
					}
					break;
				case CommonConstant.REQUEST_NET_TWO:
					setResult(RESULT_OK);
					finish();
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
}
