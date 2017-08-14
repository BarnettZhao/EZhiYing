package cn.antke.ezy.deal.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.utils.StringUtil;
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
import cn.antke.ezy.utils.ViewInjectUtils;

/**
 * Created by liuzhichao on 2017/5/12.
 * 卖出积分
 */
public class SaleIntegralActivity extends ToolBarActivity implements View.OnClickListener {

	@ViewInject(R.id.tv_sale_integral_available)
	private TextView tvSaleIntegralAvailable;
	@ViewInject(R.id.et_sale_integral_num)
	private EditText etSaleIntegralNum;
	@ViewInject(R.id.et_sale_integral_price)
	private EditText etSaleIntegralPrice;
	@ViewInject(R.id.tv_sale_integral_sell)
	private View tvSaleIntegralSell;

	private DealBuySellEntity dealBuySell;

	public static void startSaleIntegralActivity(Context context) {
		Intent intent = new Intent(context, SaleIntegralActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_sale_integral);
		ViewInjectUtils.inject(this);
		initView();
		loadData();
	}

	private void initView() {
		setLeftTitle(getString(R.string.dialog_integral_sale));
		setRightText(getString(R.string.cancel_sell));
		rightText.setOnClickListener(this);
		tvSaleIntegralSell.setOnClickListener(this);
	}

	private void loadData() {
		showProgressDialog();
		requestHttpData(Constants.Urls.URL_POST_DEAL_CONDITION, CommonConstant.REQUEST_NET_ONE, FProtocol.HttpMethod.POST, null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.rigth_text:
				CancelSaleActivity.startCancelSaleActivity(this);
				break;
			case R.id.tv_sale_integral_sell:
				String sNum = etSaleIntegralNum.getText().toString();
				if (TextUtils.isEmpty(sNum)) {
					ToastUtil.shortShow(this, getString(R.string.please_input_sell_integral));
					return;
				}
				int num = StringUtil.parseInt(sNum, 0);
				if (num <= 0) {
					ToastUtil.shortShow(this, getString(R.string.sell_integral_greater_than_zero));
					return;
				}
				String sPrice = etSaleIntegralPrice.getText().toString();
				if (TextUtils.isEmpty(sPrice)) {
					ToastUtil.shortShow(this, getString(R.string.please_sell_price));
					return;
				}
				int price = Integer.parseInt(sPrice);
				if (dealBuySell != null) {
					double minPrice = num / dealBuySell.getRate();
					if (price < minPrice) {
						ToastUtil.shortShow(this, "卖出价格太低");
						return;
					}
				}

				showProgressDialog();
				IdentityHashMap<String, String> params = new IdentityHashMap<>();
				params.put("sellout_integral", sNum);
				params.put("sellout_price", sPrice);
				requestHttpData(Constants.Urls.URL_POST_SELL_INTEGRAL, CommonConstant.REQUEST_NET_TWO, FProtocol.HttpMethod.POST, params);
				break;
		}
	}

	@Override
	public void success(int requestCode, String data) {
		closeProgressDialog();
		Entity entity = Parsers.getResult(data);
		if (CommonConstant.REQUEST_NET_SUCCESS.equals(entity.getResultCode())) {
			switch (requestCode) {
				case CommonConstant.REQUEST_NET_ONE:
					dealBuySell = Parsers.getDealBuySell(data);
					if (dealBuySell != null) {
						tvSaleIntegralAvailable.setText(String.valueOf(dealBuySell.getUsageIntegral()));
					}
					break;
				case CommonConstant.REQUEST_NET_TWO:
					ToastUtil.shortShow(this, "卖出挂单成功");
					loadData();
					break;
			}
		} else {
			ToastUtil.shortShow(this, entity.getResultMsg());
		}
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		closeProgressDialog();
		super.mistake(requestCode, status, errorMessage);
	}
}
