package cn.antke.ezy.product.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.common.adapter.BaseAdapterNew;
import com.common.adapter.ViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.common.CommonConstant;
import cn.antke.ezy.network.entities.ProductDetailEntity;
import cn.antke.ezy.product.controller.NormsActivity;
import cn.antke.ezy.utils.ImageUtils;

/**
 * Created by liuzhichao on 2017/5/22.
 * 购物车商品
 */
public class CarProductAdapter extends BaseAdapterNew<ProductDetailEntity> {

	private ShopCarAdapter shopCarAdapter;
	private Activity activity;
	private int parentPosition;

	public CarProductAdapter(Activity activity, List<ProductDetailEntity> mDatas, ShopCarAdapter shopCarAdapter, int parentPosition) {
		super(activity, mDatas);
		this.activity = activity;
		this.shopCarAdapter = shopCarAdapter;
		this.parentPosition = parentPosition;
	}

	public CarProductAdapter(Context context, List<ProductDetailEntity> mDatas) {
		super(context, mDatas);
	}

	@Override
	protected int getResourceId(int resId) {
		return R.layout.item_car_product;
	}

	@Override
	protected void setViewData(View convertView, int position) {
		ProductDetailEntity productEntity = getItem(position);
		if (productEntity != null) {
			SimpleDraweeView sdvCarProductPic = ViewHolder.get(convertView, R.id.sdv_car_product_pic);
			ImageUtils.setSmallImg(sdvCarProductPic, productEntity.getPicUrl());

			CheckBox cbCarProductCheck = ViewHolder.get(convertView, R.id.cb_car_product_check);
			View rlCarProductInfo = ViewHolder.get(convertView, R.id.rl_car_product_info);
			View rlCarProductEdit = ViewHolder.get(convertView, R.id.rl_car_product_edit);
			TextView tvCarProductName = ViewHolder.get(convertView, R.id.tv_car_product_name);
			TextView tvCarProductDesc = ViewHolder.get(convertView, R.id.tv_car_product_desc);
			TextView tvCarProductCondition = ViewHolder.get(convertView, R.id.tv_car_product_condition);
			TextView tvCarProductNum = ViewHolder.get(convertView, R.id.tv_car_product_num);
			TextView tvCarProductCount = ViewHolder.get(convertView, R.id.tv_car_product_count);
			TextView tvCarProductChange = ViewHolder.get(convertView, R.id.tv_car_product_change);
			View ivCarProductEdit = ViewHolder.get(convertView, R.id.iv_car_product_edit);

			if (productEntity.isEditMode()) {
				rlCarProductInfo.setVisibility(View.GONE);
				rlCarProductEdit.setVisibility(View.VISIBLE);
			} else {
				rlCarProductInfo.setVisibility(View.VISIBLE);
				rlCarProductEdit.setVisibility(View.GONE);
			}

			cbCarProductCheck.setChecked(productEntity.isChecked());

			tvCarProductName.setText(productEntity.getGoodsName());
			tvCarProductDesc.setText(productEntity.getGoodsBrief());

			String condition = "";
			ArrayList<ProductDetailEntity.NormEntity> attributeList = productEntity.getAttributeList();
			for (ProductDetailEntity.NormEntity normEntity : attributeList) {
				condition += (normEntity.getAttrName() + "：" + normEntity.getAttrValue() + "    ");
			}
			tvCarProductCondition.setText(condition);
			tvCarProductNum.setText("x" + productEntity.getCount());

			tvCarProductCount.setText("数量：" + String.valueOf(productEntity.getCount()));
			tvCarProductChange.setText(condition);

			if (activity != null && shopCarAdapter != null) {
				cbCarProductCheck.setOnClickListener(v -> {
					productEntity.setChecked(cbCarProductCheck.isChecked());
					shopCarAdapter.notifyDataSetChanged();
				});
				ivCarProductEdit.setOnClickListener(v -> {
					NormsActivity.startNormsActivity(activity, CommonConstant.REQUEST_ACT_ONE, CommonConstant.FROM_SHOP_CAR_LIST, productEntity.getNormEntities(), parentPosition, position);
				});
			} else {
				cbCarProductCheck.setVisibility(View.GONE);
			}
		}
	}
}
