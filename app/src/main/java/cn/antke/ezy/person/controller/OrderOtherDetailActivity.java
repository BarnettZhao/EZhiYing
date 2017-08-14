package cn.antke.ezy.person.controller;

import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.viewinject.annotation.ViewInject;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.utils.ViewInjectUtils;

/**
 * Created by zhaoweiwei on 2017/5/15.
 * 除购物订单外的其他订单详情
 */

public class OrderOtherDetailActivity extends ToolBarActivity {
	@ViewInject(R.id.order_detail_other_type)
	private TextView orderOtherType;
	@ViewInject(R.id.order_detail_other_status)
	private TextView orderOtherState;
	@ViewInject(R.id.order_detail_other_desc)
	private TextView orderOtherDesc;
	@ViewInject(R.id.order_detail_other_integral)
	private TextView orderOtherIntegral;
	@ViewInject(R.id.order_detail_other_time)
	private TextView orderOtherTime;
	@ViewInject(R.id.order_detail_other_multiple)
	private TextView orderOtherMultiple;
	@ViewInject(R.id.order_detail_other_text1)
	private TextView orderOtherText1;
	@ViewInject(R.id.order_detail_other_text2)
	private TextView orderOtherText2;
	@ViewInject(R.id.order_detail_other_text3)
	private TextView orderOtherText3;
	@ViewInject(R.id.order_detail_other_bottom_rl)
	private RelativeLayout orderOtherBottom;
	@ViewInject(R.id.order_detail_other_price)
	private TextView orderOtherPrice;
	@ViewInject(R.id.order_detail_other_btn)
	private TextView orderOtherBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_order_detail_other);
		ViewInjectUtils.inject(this);
	}
}
