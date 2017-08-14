package cn.antke.ezy.product.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.antke.ezy.R;
import cn.antke.ezy.widget.flowlayout.FlowLayoutAdapter;

/**
 * Created by liuzhichao on 2017/6/14.
 * 规格
 */
public class NormsAdapter extends FlowLayoutAdapter<String> {

	private Context context;

	public NormsAdapter(Context context) {
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = View.inflate(context, R.layout.item_norms_size, null);
		TextView textView = (TextView) view.findViewById(R.id.shopcar_fixstyle_size_tv);
		String t = mDataList.get(position);
		textView.setText(t);
		return view;
	}
}
