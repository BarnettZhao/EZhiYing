package cn.antke.ezy.person.controller;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.common.network.FProtocol;
import com.common.viewinject.annotation.ViewInject;

import java.util.IdentityHashMap;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.ConsumerBindQueryEntity;
import cn.antke.ezy.network.entities.RecommenderEntity;
import cn.antke.ezy.utils.ViewInjectUtils;

import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_ONE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_THREE;
import static cn.antke.ezy.common.CommonConstant.REQUEST_NET_TWO;

/**
 * Created by zhaoweiwei on 2017/5/17.
 * 绑定消费服务中心
 */

public class ConsumeServiceBindActivity extends ToolBarActivity {

	@ViewInject(R.id.consumer_service_bind_code)
	private EditText bindCode;
	@ViewInject(R.id.consumer_service_bind_realname)
	private TextView bindName;
	@ViewInject(R.id.consumer_service_bind_phone)
	private TextView bindPhone;
	@ViewInject(R.id.consumer_service_bind_btn)
	private TextView bindBtn;
	private String serviceType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_consume_service_bind);
		ViewInjectUtils.inject(this);
		setLeftTitle(getString(R.string.person_consumer_service_bind));

		loadData();
		bindBtn.setOnClickListener(v -> {
			String serviceCode = bindCode.getText().toString();
			showProgressDialog();
			IdentityHashMap<String, String> params = new IdentityHashMap<>();
			params.put("service_code", serviceCode);
			params.put("operation", serviceType);
			requestHttpData(Constants.Urls.URL_POST_BIND_CONSUMER, REQUEST_NET_TWO, FProtocol.HttpMethod.POST, params);
		});
	}

	private void loadData() {
		showProgressDialog();
		requestHttpData(Constants.Urls.URL_POST_BIND_CONSUMER_QUERY, REQUEST_NET_ONE, FProtocol.HttpMethod.POST, new IdentityHashMap<>());
	}

	@Override
	protected void parseData(int requestCode, String data) {
		super.parseData(requestCode, data);
		switch (requestCode) {
			case REQUEST_NET_ONE://查询
				ConsumerBindQueryEntity entity = Parsers.getBindInfo(data);
				serviceType = entity.getServiceType();
				if ("1".equals(serviceType)) {//未绑定
					bindBtn.setText(getString(R.string.consumer_service_bind));
					bindCode.addTextChangedListener(new TextWatcher() {
						@Override
						public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

						}

						@Override
						public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

						}

						@Override
						public void afterTextChanged(Editable editable) {
							String userCode = bindCode.getText().toString();
							if (7 == userCode.length()) {
								showProgressDialog();
								IdentityHashMap<String, String> params = new IdentityHashMap<>();
								params.put("recommender", userCode);
								requestHttpData(Constants.Urls.URL_POST_USER_RECOMMEND_CODE, REQUEST_NET_THREE, FProtocol.HttpMethod.POST, params);
							}
						}
					});
				} else {
					bindBtn.setText(getString(R.string.consumer_service_unbind));
					bindCode.setEnabled(false);
					bindCode.setText(entity.getUserCode());
					bindName.setText(entity.getUserName());
					bindPhone.setText(entity.getUserPhone());
				}
				break;
			case REQUEST_NET_TWO://绑定解绑
				finish();
				break;
			case REQUEST_NET_THREE://获取用户信息
				RecommenderEntity recommenderEntity = Parsers.getRecommender(data);
				String user = recommenderEntity.getUserName();
				String phone = recommenderEntity.getPhone();

				bindName.setText(user);
				bindPhone.setText(phone);
				break;
		}
	}
}
