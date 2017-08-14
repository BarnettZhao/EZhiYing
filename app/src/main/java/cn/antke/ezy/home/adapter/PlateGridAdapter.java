package cn.antke.ezy.home.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;

import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.network.entities.PlateDetailEntity;
import cn.antke.ezy.utils.ImageUtils;

/**
 * Created by liuzhichao on 2017/5/11.
 * 板块列表
 */
public class PlateGridAdapter extends BaseAdapterNew<PlateDetailEntity> {

	public PlateGridAdapter(Context context, List<PlateDetailEntity> mDatas) {
		super(context, mDatas);
	}

	@Override
	protected int getResourceId(int resId) {
		return R.layout.item_plate_grid;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		PlateDetailEntity plateDetail = getItem(position);
		if (plateDetail != null) {
			ImageUtils.setSmallImg(ViewHolder.get(convertView, R.id.sdv_item_plate_pic), plateDetail.getPicUrl());
			TextView tvItemPlateName = ViewHolder.get(convertView, R.id.tv_item_plate_name);
			tvItemPlateName.setText(plateDetail.getName());
		}
	}
}
