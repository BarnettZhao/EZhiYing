package cn.antke.ezy.person.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;

import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.network.entities.LogisticItemEntity;

/**
 * Created by zhaoweiwei on 2017/1/12.
 */

public class LogisticDetailAdapter extends BaseAdapterNew<LogisticItemEntity> {
	private int lastPosition;
	private Resources resources;

	public LogisticDetailAdapter(Context context, List<LogisticItemEntity> mDatas) {
		super(context, mDatas);
		resources = context.getResources();
		lastPosition = mDatas.size() - 1;
	}

	@Override
	protected int getResourceId(int Position) {
		return R.layout.item_logistic_detail;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		LogisticItemEntity entity = getItem(position);
		View view1 = ViewHolder.get(convertView, R.id.item_logistic_view1);
		ImageView img = ViewHolder.get(convertView, R.id.item_logistic_img);
		View view2 = ViewHolder.get(convertView, R.id.item_logistic_view2);
		TextView describe = ViewHolder.get(convertView, R.id.item_logistic_describe);
		TextView time = ViewHolder.get(convertView, R.id.item_logistic_time);
		if (0 == position) {
			view1.setVisibility(View.INVISIBLE);
			view2.setVisibility(View.VISIBLE);
			describe.setTextColor(resources.getColor(R.color.primary_color));
			time.setTextColor(resources.getColor(R.color.primary_color));
			img.setImageResource(R.drawable.logistic_green);
		} else if (lastPosition == position) {
			view1.setVisibility(View.VISIBLE);
			view2.setVisibility(View.INVISIBLE);
			describe.setTextColor(resources.getColor(R.color.text_introduce_color));
			time.setTextColor(resources.getColor(R.color.text_introduce_color));
			img.setImageResource(R.drawable.logistics_gray);
		} else {
			view1.setVisibility(View.VISIBLE);
			view2.setVisibility(View.VISIBLE);
			describe.setTextColor(resources.getColor(R.color.text_introduce_color));
			time.setTextColor(resources.getColor(R.color.text_introduce_color));
			img.setImageResource(R.drawable.logistics_gray);
		}

		describe.setText(entity.getDescribe());
		time.setText(entity.getTime());
	}
}
