package cn.antke.ezy.person.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.network.entities.OrderGoodEntity;
import cn.antke.ezy.utils.ImageUtils;


/**
 * Created by zhaoweiwei on 2017/5/18.
 * 订单商品
 */

public class OrderGoodAdapter extends BaseAdapterNew<OrderGoodEntity> {
	private Context context;
	public OrderGoodAdapter(Context context, List<OrderGoodEntity> mDatas) {
		super(context, mDatas);
		this.context = context;
	}

	@Override
	protected int getResourceId(int resId) {
		return R.layout.item_person_order_good;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		OrderGoodEntity entity = getItem(position);
		SimpleDraweeView goodPic = ViewHolder.get(convertView, R.id.item_order_good_pic);
		TextView goodName = ViewHolder.get(convertView, R.id.item_order_good_name);
		TextView goodAttrs = ViewHolder.get(convertView, R.id.item_order_good_spec);
		TextView goodNum = ViewHolder.get(convertView, R.id.item_order_good_num);
		if (entity != null) {
			ImageUtils.setSmallImg(goodPic, entity.getGoodsPic());
			goodName.setText(entity.getGoodsName());
			goodAttrs.setText(context.getString(R.string.person_order_goods_spec,entity.getAttribute()));
			goodNum.setText(context.getString(R.string.person_order_goods_num,entity.getGoodsNum()));
		}
	}
}
