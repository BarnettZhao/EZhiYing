package cn.antke.ezy.product.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.common.utils.StringUtil;
import com.common.view.ListViewForInner;

import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.network.entities.ProductDetailEntity;
import cn.antke.ezy.network.entities.StoreDetailEntity;
import cn.antke.ezy.utils.InputUtil;

/**
 * Created by liuzhichao on 2017/6/18.
 * 确认订单店铺列表
 */
public class OrderStoreAdapter extends BaseAdapterNew<StoreDetailEntity> {

	public OrderStoreAdapter(Context context, List<StoreDetailEntity> mDatas) {
		super(context, mDatas);
	}

	@Override
	protected int getResourceId(int resId) {
		return R.layout.item_order_confrim_store;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		StoreDetailEntity storeDetailEntity = getItem(position);

		if (storeDetailEntity != null) {
			TextView tvConfirmStoreName = ViewHolder.get(convertView, R.id.tv_confirm_store_name);
			ListViewForInner lvfiConfirmOrderProduct = ViewHolder.get(convertView, R.id.lvfi_confirm_order_product);
			TextView tvConfirmStoreSubtotal = ViewHolder.get(convertView, R.id.tv_confirm_store_subtotal);
			TextView tvConfirmStoreSubintegral = ViewHolder.get(convertView, R.id.tv_confirm_store_subintegral);

			tvConfirmStoreName.setText(storeDetailEntity.getMerchant());

			List<ProductDetailEntity> productEntities = storeDetailEntity.getProductEntities();

			int productIntegral = 0;
			int productPrice = 0;
			int logisticsIntegral = 0;
			int logisticsPrice = 0;
			for (ProductDetailEntity productDetailEntity : productEntities) {
				productIntegral += StringUtil.parseInt(productDetailEntity.getOrderIntegral(), 0);
				productPrice += StringUtil.parseInt(productDetailEntity.getOrderTotal(), 0);
				logisticsIntegral += StringUtil.parseInt(productDetailEntity.getLogisticsIntegral(), 0);
				logisticsPrice += StringUtil.parseInt(productDetailEntity.getLogisticsCost(), 0);
			}

			tvConfirmStoreSubtotal.setText("商品小计：" + InputUtil.formatPrice(productIntegral, productPrice));
			tvConfirmStoreSubintegral.setText("运费：" + InputUtil.formatLogistics(logisticsIntegral, logisticsPrice));
			CarProductAdapter adapter = new CarProductAdapter(getContext(), productEntities);
			lvfiConfirmOrderProduct.setAdapter(adapter);
		}
	}
}
