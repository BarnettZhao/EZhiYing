package cn.antke.ezy.category.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;

import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.network.entities.CategoryItemEntity;

/**
 * Created by zhaoweiwei on 2017/5/8.
 * 分类页面分类条目
 */

public class CategoryAdapter extends BaseAdapterNew<CategoryItemEntity> {
	private Context context;

	public CategoryAdapter(Context context, List<CategoryItemEntity> mDatas) {
		super(context, mDatas);
		this.context = context;
	}

	@Override
	protected int getResourceId(int Position) {
		return R.layout.item_categrory_categrory;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		CategoryItemEntity categrory = getItem(position);
		TextView textView = ViewHolder.get(convertView, R.id.item_categrory);
		if (categrory != null) {
			textView.setText(categrory.getCategoryName());
			if (categrory.isChecked()) {
				textView.setTextColor(ContextCompat.getColor(context, R.color.primary_color));
				textView.setBackgroundColor(ContextCompat.getColor(context, R.color.common_bg));
			} else {
				textView.setTextColor(ContextCompat.getColor(context, R.color.middle_gray));
				textView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
			}
		}
	}
}
