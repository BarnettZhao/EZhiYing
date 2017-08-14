package cn.antke.ezy.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.antke.ezy.R;
import cn.antke.ezy.widget.flowlayout.FlowLayoutAdapter;

/**
 * Created by zhaoweiwei on 2017/1/6.
 * 热门搜索流布局adapter
 */
public class HotSearchAdapter extends FlowLayoutAdapter<String> {

	private Context context;

	public HotSearchAdapter(Context context) {
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = LayoutInflater.from(context).inflate(R.layout.home_hot_search_item, null);
		TextView hotSearch = (TextView) view.findViewById(R.id.hot_search_text);
		hotSearch.setText(mDataList.get(position));
		return view;
	}
}