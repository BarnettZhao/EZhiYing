package cn.antke.ezy.person.controller;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.RadioGroup;

import com.common.viewinject.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.base.ToolBarActivity;
import cn.antke.ezy.person.adapter.StoreOrderPageAdapter;
import cn.antke.ezy.utils.ViewInjectUtils;

import static cn.antke.ezy.common.CommonConstant.EXTRA_ID;
import static cn.antke.ezy.common.CommonConstant.ORDERSTATE_DELIVED;
import static cn.antke.ezy.common.CommonConstant.ORDERSTATE_DELIVING;
import static cn.antke.ezy.common.CommonConstant.ORDERSTATE_FINISHED;
import static cn.antke.ezy.common.CommonConstant.ORDERSTATE_REFUND;

/**
 * Created by zhaoweiwei on 2016/12/21.
 * 订单
 */

public class StoreOrderActivity extends ToolBarActivity {

	@ViewInject(R.id.store_order_radiogroup)
	private RadioGroup orderGroup;
	@ViewInject(R.id.store_order_viewpager)
	private ViewPager viewPager;
	private String storeId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_store_order_manager);
		ViewInjectUtils.inject(this);
		setLeftTitle(getString(R.string.store_order_manager));
		storeId = getIntent().getStringExtra(EXTRA_ID);
		initFragment();
	}

	/*private void showState() {
		switch (state) {
			case ORDERTYPE_ALL:
				viewPager.setCurrentItem(0);
				orderGroup.check(R.id.store_order_deliving);
				break;
			case ORDERTYPE_PAYING:
				viewPager.setCurrentItem(1);
				orderGroup.check(R.id.store_order_delived);
				break;
			case ORDERTYPE_DELIVER:
				viewPager.setCurrentItem(2);
				orderGroup.check(R.id.store_order_finished);
				break;
			case ORDERTYPE_GETING:
				viewPager.setCurrentItem(3);
				orderGroup.check(R.id.store_order_refund);
				break;
		}
	}*/

	private void initFragment() {
		StoreOrderListFragment allOrderFrag = new StoreOrderListFragment();
		allOrderFrag.setArgs(ORDERSTATE_DELIVING,storeId);
		StoreOrderListFragment payingOrderFrag = new StoreOrderListFragment();
		payingOrderFrag.setArgs(ORDERSTATE_DELIVED,storeId);
		StoreOrderListFragment deliverOrderFrag = new StoreOrderListFragment();
		deliverOrderFrag.setArgs(ORDERSTATE_FINISHED,storeId);
		StoreOrderListFragment gettingOrderFrag = new StoreOrderListFragment();
		gettingOrderFrag.setArgs(ORDERSTATE_REFUND,storeId);

		List<StoreOrderListFragment> fragments = new ArrayList<>();
		fragments.add(allOrderFrag);
		fragments.add(payingOrderFrag);
		fragments.add(deliverOrderFrag);
		fragments.add(gettingOrderFrag);

		StoreOrderPageAdapter orderPageAdapter = new StoreOrderPageAdapter(getSupportFragmentManager(), fragments);
		viewPager.setAdapter(orderPageAdapter);
		viewPager.addOnPageChangeListener(new MyPageChangeListener());
		orderGroup.setOnCheckedChangeListener(new MyGroupCheckChanged());
	}

	private class MyPageChangeListener implements ViewPager.OnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		}

		@Override
		public void onPageSelected(int position) {
			switch (position) {
				case 0:
					orderGroup.check(R.id.store_order_deliving);
					break;
				case 1:
					orderGroup.check(R.id.store_order_delived);
					break;
				case 2:
					orderGroup.check(R.id.store_order_finished);
					break;
				case 3:
					orderGroup.check(R.id.store_order_refund);
					break;
			}
		}

		@Override
		public void onPageScrollStateChanged(int state) {
		}
	}

	private class MyGroupCheckChanged implements RadioGroup.OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup radioGroup, int i) {
			switch (i) {
				case R.id.store_order_deliving:
					viewPager.setCurrentItem(0);
					break;
				case R.id.store_order_delived:
					viewPager.setCurrentItem(1);
					break;
				case R.id.store_order_finished:
					viewPager.setCurrentItem(2);
					break;
				case R.id.store_order_refund:
					viewPager.setCurrentItem(3);
					break;
			}
		}
	}
}
