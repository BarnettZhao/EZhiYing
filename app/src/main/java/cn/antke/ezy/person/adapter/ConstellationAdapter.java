package cn.antke.ezy.person.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.common.utils.StringUtil;

import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.network.entities.LogisticCompanyEntity;

/**
 * Created by zhaoweiwei on 2017/5/6.
 * 星座
 */

public class ConstellationAdapter extends BaseAdapterNew<LogisticCompanyEntity> {
	public ConstellationAdapter(Context context, List<LogisticCompanyEntity> mDatas) {
		super(context, mDatas);
	}

	@Override
	protected int getResourceId(int Position) {
		return R.layout.item_person_constellation;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		LogisticCompanyEntity entity = getItem(position);
		TextView textView = ViewHolder.get(convertView,R.id.item_constellation);
		if (!StringUtil.isEmpty(entity.getLogisticName())) {
			textView.setText(entity.getLogisticName());
		}
	}
}
