package cn.antke.ezy.person.controller;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.RecommenderEntity;
import cn.antke.ezy.utils.DialogUtils;
import cn.antke.ezy.utils.ViewInjectUtils;

import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_ONE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_THREE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_TWO;

/**
 * Created by zhaoweiwei on 2017/5/18.
 * 转积分
 */
public class TransferIntegralActivity extends ToolBarActivity {

	@ViewInject(R.id.transfer_integral_usercode)
	private EditText transferUsercode;
	@ViewInject(R.id.transfer_integral_username)
	private TextView transferUsername;
	@ViewInject(R.id.transfer_integral_phone)
	private TextView transferPhone;
	@ViewInject(R.id.transfer_integral_integral)
	private EditText transferIntegral;
	@ViewInject(R.id.transfer_integral_confirm)
	private TextView tansferConfirm;
	private String outUserId;
	private String integral;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_transger_integral);
		ViewInjectUtils.inject(this);
		setLeftTitle(getString(R.string.person_transfer_integral));

		transferUsercode.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void afterTextChanged(Editable editable) {
				String userCode = transferUsercode.getText().toString();
				if (7 == userCode.length()) {
					showProgressDialog();
					IdentityHashMap<String, String> params = new IdentityHashMap<>();
					params.put("recommender", userCode);
					requestHttpData(Constants.Urls.URL_POST_USER_RECOMMEND_CODE, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
				}
			}
		});

		tansferConfirm.setOnClickListener(v -> {
			integral = transferIntegral.getText().toString();
			if (!StringUtil.isEmpty(integral)) {
				int integralInt = Integer.parseInt(integral);
				if (integralInt > 0) {
					DialogUtils.showPwdInputDialog(this, (v1, editText) -> {
						String pwd = editText.getText().toString();
						showProgressDialog();
						IdentityHashMap<String, String> params = new IdentityHashMap<>();
						params.put("pay_password", pwd);
//							params.put("purchase_num", num);
						requestHttpData(Constants.Urls.URL_POST_VERIFY_PWD, REQUEST_NET_THREE, FProtocol.HttpMethod.POST, params);
						DialogUtils.closeDialog();
					}, new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							DialogUtils.closeDialog();
						}
					});
				} else {
					ToastUtil.shortShow(TransferIntegralActivity.this, getString(R.string.integral_exchange_mines));
				}

			} else {
				ToastUtil.shortShow(TransferIntegralActivity.this, getString(R.string.integral_exchange_mines));
			}
		});
	}

	private void transferIntegral(String integral) {
		showProgressDialog();
		IdentityHashMap<String, String> params = new IdentityHashMap<>();
		params.put("in_user_id", outUserId);
		params.put("integral", integral);
		requestHttpData(Constants.Urls.URL_POST_INTEGRAL_EXCHANGE, REQUEST_NET_TWO, FProtocol.HttpMethod.POST, params);
	}

	@Override
	protected void parseData(int requestCode, String data) {
		super.parseData(requestCode, data);
		switch (requestCode) {
			case REQUEST_NET_ONE://获取用户编号
				RecommenderEntity recommenderEntity = Parsers.getRecommender(data);
				String user = recommenderEntity.getUserName();
				String phone = recommenderEntity.getPhone();
				outUserId = recommenderEntity.getUserId();

				transferUsername.setText(user);
				transferPhone.setText(phone);
				break;
			case REQUEST_NET_TWO://转积分
				finish();
				break;
			case REQUEST_NET_THREE://验证交易密码
				transferIntegral(integral);
				break;
		}
	}
}
