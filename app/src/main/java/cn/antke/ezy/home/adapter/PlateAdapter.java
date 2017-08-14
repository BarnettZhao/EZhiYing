package cn.antke.ezy.home.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;

import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.home.listener.OnItemHeightGetListener;
import cn.antke.ezy.network.entities.PlateEntity;
import cn.antke.ezy.utils.ImageUtils;

/**
 * Created by liuzhichao on 2017/5/5.
 * 专区
 */
public class PlateAdapter extends BaseAdapterNew<PlateEntity> {

	private OnItemHeightGetListener onItemHeightGetListener;
	private View.OnClickListener onClickListener;

	public PlateAdapter(Context context, List<PlateEntity> mDatas, View.OnClickListener onClickListener) {
		super(context, mDatas);
		this.onClickListener = onClickListener;
	}

	public void setOnItemHeightGetListener(OnItemHeightGetListener onItemHeightGetListener) {
		this.onItemHeightGetListener = onItemHeightGetListener;
	}

	@Override
	protected int getResourceId(int Position) {
		return R.layout.item_home_plate_single;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		PlateEntity plateEntity = getItem(position);
		if (plateEntity != null) {
			ImageUtils.setSmallImg(ViewHolder.get(convertView, R.id.sdv_item_home_plate_pic), plateEntity.getImgUrl());
			TextView tvItemHomePlateName = ViewHolder.get(convertView, R.id.tv_item_home_plate_name);
			tvItemHomePlateName.setText(plateEntity.getName());

			View view = ViewHolder.get(convertView, R.id.ll_item_home_plate);
			view.setTag(R.id.plate_click_tag, plateEntity);
			view.setOnClickListener(onClickListener);

			int measuredHeight = view.getMeasuredHeight();
			if (measuredHeight > 0) {
				onItemHeightGetListener.onItemHeightGet(measuredHeight);
			}
		}
	}
}
