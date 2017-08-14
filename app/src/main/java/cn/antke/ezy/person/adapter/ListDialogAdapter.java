package cn.antke.ezy.person.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;

import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.network.entities.ListDialogEntity;
import cn.antke.ezy.network.entities.ListDialogPageEntity;

/**
 * Created by zhaoweiwei on 2017/2/27.
 */

public class ListDialogAdapter extends BaseAdapterNew<ListDialogEntity> {
	private Resources resources;
	public ListDialogAdapter(Context context, List<ListDialogEntity> mDatas) {
		super(context, mDatas);
		resources = context.getResources();
	}

	@Override
	protected int getResourceId(int Position) {
		return R.layout.item_person_constellation;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		ListDialogEntity entity = getItem(position);
		TextView textView = ViewHolder.get(convertView,R.id.item_constellation);
		textView.setText(entity.getItemContent());
		if (entity.isChecked()) {
			textView.setTextColor(resources.getColor(R.color.primary_color));
		} else {
			textView.setTextColor(resources.getColor(R.color.primary_color_text));
		}
	}
}
