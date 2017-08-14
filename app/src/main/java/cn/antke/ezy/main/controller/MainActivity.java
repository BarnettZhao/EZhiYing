package cn.antke.ezy.main.controller;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.common.network.FProtocol;
import com.common.utils.LogUtil;
import com.common.utils.ToastUtil;

import java.util.IdentityHashMap;

import cn.antke.ezy.R;
import cn.antke.ezy.base.BaseTabsDrawerActivity;
import cn.antke.ezy.category.controller.CategoryFragment;
import cn.antke.ezy.common.CommonConstant;
import cn.antke.ezy.deal.controller.DealHallFragment;
import cn.antke.ezy.deal.controller.DealRechargeActivity;
import cn.antke.ezy.home.controller.HomeFragment;
import cn.antke.ezy.login.controller.LoginActivity;
import cn.antke.ezy.login.utils.UserCenter;
import cn.antke.ezy.network.Constants;
import cn.antke.ezy.network.Parsers;
import cn.antke.ezy.network.entities.DealConditionEntity;
import cn.antke.ezy.network.entities.Entity;
import cn.antke.ezy.person.PersonFragment1;
import cn.antke.ezy.special.controller.SpecialFragment;
import cn.antke.ezy.utils.ExitManager;
import cn.antke.ezy.utils.PermissionUtils;
import cn.jpush.android.api.JPushInterface;

public class MainActivity extends BaseTabsDrawerActivity {

	public static final String EXTRA_WHICH_TAB = "extra_which_tab";
	public static final long DIFF_DEFAULT_BACK_TIME = 2000;

	private long mBackTime = -1;
	private boolean flag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isHasFragment = true;
		LogUtil.e("MainActivity", "registrationID=" + JPushInterface.getRegistrationID(this));
		PermissionUtils.requestPermissions(this, REQUEST_PERMISSION_CODE, Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE);

//		getNewActivity();
	}

	//获取新的活动
	private void getNewActivity() {
		//添加活动测试数据
		NewActivityActivity.startNewActivityActivity(this, "", "http://onau582bt.bkt.clouddn.com/6505a939-75b0-4c84-b4fd-01ab58d040f2");
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == REQUEST_PERMISSION_CODE) {
			//如果取消了，结果数组将会为0，结果数组数量对应请求权限的个数
			if (grantResults.length < 1) {
				ToastUtil.shortShow(this, getString(R.string.get_permission_failed));
			}
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		int whichTab = intent.getIntExtra(EXTRA_WHICH_TAB, 0);
		setCurrentTab(whichTab);
	}

	@Override
	protected void addTabs() {
		addTab(initTabView(R.drawable.navigation_ic_home_selector, R.string.main_tab_home), HomeFragment.class, null);
		addTab(initTabView(R.drawable.navigation_ic_category_selector, R.string.main_tab_category), CategoryFragment.class, null);
		addTab(initTabView(R.drawable.navigation_ic_deal_selector, R.string.main_tab_deal), DealHallFragment.class, null);
		addTab(initTabView(R.drawable.navigation_ic_special_selector, R.string.main_tab_special), SpecialFragment.class, null);
		addTab(initTabView(R.drawable.navigation_ic_person_selector, R.string.main_tab_person), PersonFragment1.class, null);
	}

	private View initTabView(int tabIcon, int tabText) {
		ViewGroup tab = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.main_tab_item, null);
		ImageView imageView = (ImageView) tab.findViewById(R.id.navigation);
		imageView.setImageResource(tabIcon);

		TextView textView = (TextView) tab.findViewById(R.id.txt_navigation);
		textView.setText(tabText);
		return tab;
	}

	@Override
	public void onTabChanged(String tabId) {
		super.onTabChanged(tabId);
		switch (currentIndex) {
			case 2:
				if (!UserCenter.isLogin(this)) {
					setCurrentTab(preIndex);
					startActivity(new Intent(this, LoginActivity.class));
				} else {
					if (!flag) {
						setCurrentTab(preIndex);
						showProgressDialog();
						IdentityHashMap<String, String> params = new IdentityHashMap<>();
						params.put("tradeHall_site_id", UserCenter.getUserSiteId(this));
						requestHttpData(Constants.Urls.URL_POST_DEAL_INFO, CommonConstant.REQUEST_NET_ONE, FProtocol.HttpMethod.POST, params);
					}
				}
				break;
			case 4:
//				if (UserCenter.personFirst(this)){
//					setCurrentTab(preIndex);
//					ProtocalActivity.startProtocalActivity(this,FROM_ACT_FIVE);
//				}
				if (!UserCenter.isLogin(this)){
					setCurrentTab(preIndex);
					startActivity(new Intent(this,LoginActivity.class));
				}
				break;
		}
	}

	@Override
	public void success(int requestCode, String data) {
		closeProgressDialog();
		Entity result = Parsers.getResult(data);
		if (CommonConstant.REQUEST_NET_SUCCESS.equals(result.getResultCode())) {
			switch (requestCode) {
				case CommonConstant.REQUEST_NET_ONE:
					DealConditionEntity dealCondition = Parsers.getDealCondition(data);
					if (dealCondition.getIsPay() == 1) {
						flag = true;
						setCurrentTab(2);
					} else {
						DealRechargeActivity.startDealRechargeActivity(this);
					}
					break;
			}
		}
	}

	@Override
	public void mistake(int requestCode, FProtocol.NetDataProtocol.ResponseStatus status, String errorMessage) {
		closeProgressDialog();
		super.mistake(requestCode, status, errorMessage);
	}

	@Override
	public void onBackPressed() {
		long nowTime = System.currentTimeMillis();
		long diff = nowTime - mBackTime;
		if (diff >= DIFF_DEFAULT_BACK_TIME) {
			mBackTime = nowTime;
			Toast.makeText(getApplicationContext(), R.string.toast_back_again_exit, Toast.LENGTH_SHORT).show();
		} else {
			ExitManager.instance.exit();
		}
	}
}
