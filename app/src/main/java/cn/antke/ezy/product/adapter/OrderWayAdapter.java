package cn.antke.ezy.product.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;

import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.network.entities.OrderConfirmEntity;

/**
 * Created by liuzhichao on 2017/6/17.
 * 确认订单，支付方式
 */
public class OrderWayAdapter extends BaseAdapterNew<OrderConfirmEntity.PayWay> {

	public OrderWayAdapter(Context context, List<OrderConfirmEntity.PayWay> mDatas) {
		super(context, mDatas);
	}

	@Override
	protected int getResourceId(int resId) {
		return R.layout.item_language_area;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		OrderConfirmEntity.PayWay payWay = getItem(position);
		if (payWay != null) {
			TextView tvLanguageAreaName = ViewHolder.get(convertView, R.id.tv_language_area_name);
			tvLanguageAreaName.setText(payWay.getIntegralMultiple());
			if (payWay.isSelected()) {
				tvLanguageAreaName.setSelected(true);
			} else {
				tvLanguageAreaName.setSelected(false);
			}
		}
	}
}
