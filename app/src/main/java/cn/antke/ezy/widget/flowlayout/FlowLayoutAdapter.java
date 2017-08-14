package cn.antke.ezy.widget.flowlayout;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaoweiwei on 2017/1/6.
 * 流布局父adapter
 */
public class FlowLayoutAdapter<T> extends BaseAdapter {

	protected final List<T> mDataList = new ArrayList<>();

	@Override
	public int getCount() {
		return mDataList.size();
	}

	@Override
	public Object getItem(int position) {
		return mDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		return null;
	}

	public void onlyAddAll(List<T> datas) {
		mDataList.addAll(datas);
		notifyDataSetChanged();
	}

	public void clearAndAddAll(List<T> datas) {
		mDataList.clear();
		onlyAddAll(datas);
	}

	public void clearAll(){
		mDataList.clear();
		notifyDataSetChanged();
	}
}
