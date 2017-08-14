package cn.antke.ezy.base.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by liuzhichao on 2017/1/5.
 * ViewPager的Adapter的基类
 */
public abstract class BasePagerAdapter<T> extends PagerAdapter {

	protected List<T> datas;

	public BasePagerAdapter(List<T> datas) {
		this.datas = datas;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
}
