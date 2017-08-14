package cn.antke.ezy.base;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;

import cn.antke.ezy.R;


public abstract class BaseTabsDrawerActivity extends BaseActivity implements OnTabChangeListener {
	/**
	 * Tab面板
	 */
	protected FragmentTabHost mTabHost;
	/**
	 * Tab控件
	 */
	protected TabWidget mTabWidget;
	/**
	 * 当前Tab页下标
	 */
	protected int currentIndex;
	protected TextView titleTextView;
	protected ImageView right_button;
	protected int preIndex;

	//	protected Toolbar toolbar;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.base_tabs_drawerlayout);
		importantInit();
//		initToolBar();
//		toolbar= (Toolbar) findViewById(R.id.toolbar);
//		toolbar.setVisibility(View.GONE);
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.fcontainer);
		addTabs();
		mTabHost.setOnTabChangedListener(this);
		mTabWidget = mTabHost.getTabWidget();
		mTabWidget.setDividerDrawable(null);
		if (savedInstanceState != null) {
			mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
			currentIndex = mTabHost.getCurrentTab();
		}
	}

	/**
	 * 加载页面之前需要完成的事情
	 */
	protected void importantInit() {

	}

//	private void initToolBar() {
//		titleTextView = (TextView)findViewById(R.id.toolbar_title);
//		right_button = (ImageView)findViewById(R.id.right_button);
//	}

	/**
	 * 设置Tab切换
	 *
	 * @param tabIndex 切换的Tab下标
	 */
	protected void setCurrentTab(int tabIndex) {
		mTabHost.setCurrentTab(tabIndex);
	}

	protected abstract void addTabs();

	protected void addTab(View tabView, Class<?> cls, Bundle bundle) {
		mTabHost.addTab(mTabHost.newTabSpec(cls.getSimpleName()).setIndicator(tabView), cls, bundle);
	}

	protected void addTab(View tabView, Class<?> cls, String tabName, Bundle bundle) {
		mTabHost.addTab(mTabHost.newTabSpec(tabName).setIndicator(tabView), cls, bundle);
	}

	public int getTabPosition() {
		return currentIndex;
	}

	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("tab", mTabHost.getCurrentTabTag());
	}

	@Override
	public void onTabChanged(String tabId) {
		preIndex = currentIndex;
		currentIndex = mTabHost.getCurrentTab();
	}

	public void showTabHost(boolean show) {
		if (show) {
			mTabWidget.setVisibility(View.VISIBLE);
		} else {
			mTabWidget.setVisibility(View.GONE);
		}
	}

	public boolean tabHostIsShow() {
		return mTabWidget.getVisibility() == View.VISIBLE;
	}
}
