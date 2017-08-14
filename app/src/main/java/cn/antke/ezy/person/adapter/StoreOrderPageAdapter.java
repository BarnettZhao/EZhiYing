package cn.antke.ezy.person.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

import cn.antke.ezy.person.controller.StoreOrderListFragment;

/**
 * Created by zhaoweiwei on 2016/12/21.
 */

public class StoreOrderPageAdapter extends FragmentPagerAdapter {
	private List<StoreOrderListFragment> fragments;

	public StoreOrderPageAdapter(FragmentManager fm, List<StoreOrderListFragment> fragments) {
		super(fm);
		this.fragments = fragments;
	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}
}
