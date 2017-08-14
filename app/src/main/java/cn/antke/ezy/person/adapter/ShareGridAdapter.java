package cn.antke.ezy.person.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;

import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.network.entities.ShareEntity;

/**
 * Created by Liu_ZhiChao on 2015/9/18 18:10.
 * 第三方分享
 */
public class ShareGridAdapter extends BaseAdapterNew<ShareEntity> {

	public ShareGridAdapter(Context context, List<ShareEntity> mDatas) {
		super(context, mDatas);
	}

	@Override
	protected int getResourceId(int Position) {
		return R.layout.share_grid_item;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		ShareEntity item = getItem(position);
		if (item != null) {
			TextView personShareName = ViewHolder.get(convertView, R.id.person_share_name);
			personShareName.setText(item.getName());
			personShareName.setCompoundDrawablesWithIntrinsicBounds(null,item.getLogo(),null,null);
		}
	}
}
