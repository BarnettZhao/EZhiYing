package cn.antke.ezy.home.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;

import java.util.List;

import cn.antke.ezy.R;

/**
 * Created by zhaoweiwei on 2017/1/6.
 * 历史搜索adapter
 */
public class HistorySearchAdapter extends BaseAdapterNew<String> {

	public HistorySearchAdapter(Context context, List<String> mDatas) {
		super(context, mDatas);
	}

	@Override
	protected int getResourceId(int Position) {
		return R.layout.home_search_history_item;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		TextView textView = ViewHolder.get(convertView, R.id.search_history_item);
		textView.setText(getItem(position));
	}
}
