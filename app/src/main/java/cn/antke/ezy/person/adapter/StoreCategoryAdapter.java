package cn.antke.ezy.person.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;

import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.network.entities.CategoryItemEntity;

/**
 * Created by liuzhichao on 2017/6/18.
 * 店铺分类adapter
 */
public class StoreCategoryAdapter extends BaseAdapterNew<CategoryItemEntity> {

	public StoreCategoryAdapter(Context context, List<CategoryItemEntity> mDatas) {
		super(context, mDatas);
	}

	@Override
	protected int getResourceId(int resId) {
		return R.layout.item_store_category;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		CategoryItemEntity categoryItemEntity = getItem(position);
		if (categoryItemEntity != null) {
			TextView tvStoreCategory = ViewHolder.get(convertView, R.id.tv_store_category);
			tvStoreCategory.setText(categoryItemEntity.getCategoryName());
			tvStoreCategory.setSelected(categoryItemEntity.isChecked());
		}
	}
}
