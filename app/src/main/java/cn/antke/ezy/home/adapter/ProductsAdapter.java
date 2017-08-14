package cn.antke.ezy.home.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.utils.StringUtil;
import com.common.widget.BaseRecycleAdapter;
import com.common.widget.RecyclerViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.network.entities.ProductDetailEntity;
import cn.antke.ezy.network.entities.ProductEntity;
import cn.antke.ezy.utils.ImageUtils;
import cn.antke.ezy.utils.InputUtil;

/**
 * Created by liuzhichao on 2017/5/4.
 * 商品
 */
public class ProductsAdapter extends BaseRecycleAdapter<ProductDetailEntity> {

	private View.OnClickListener onClickListener;

	public ProductsAdapter(List<ProductDetailEntity> datas, View.OnClickListener onClickListener) {
		super(datas);
		this.onClickListener = onClickListener;
	}

	@Override
	public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new RecyclerViewHolder(parent, R.layout.item_product);
	}

	@Override
	public void onBindViewHolder(RecyclerViewHolder holder, int position) {
		ProductEntity productEntity = getItemData(position);
		SimpleDraweeView productPic = holder.getView(R.id.sdv_item_product_pic);
		TextView goodsName = holder.getView(R.id.tv_item_product_name);
		TextView price = holder.getView(R.id.tv_item_product_price);
		RelativeLayout storeLayout = holder.getView(R.id.rl_item_store);
		TextView storeName = holder.getView(R.id.tv_item_store_name);
		TextView address = holder.getView(R.id.tv_item_store_address);

		holder.setTag(R.id.ll_item_product_layout, productEntity);
		holder.setOnClickListener(R.id.ll_item_product_layout, onClickListener);

		ImageUtils.setSmallImg(productPic, productEntity.getPicUrl());
		goodsName.setText(productEntity.getGoodsName());

		price.setText(InputUtil.formatPrice(productEntity.getSellingIntegral(), productEntity.getSellingPrice()));

		if (StringUtil.isEmpty(productEntity.getBrandName()) && StringUtil.isEmpty(productEntity.getAreaName())) {
			storeLayout.setVisibility(View.GONE);
		} else {
			storeLayout.setVisibility(View.VISIBLE);
			storeName.setText(productEntity.getBrandName());
			address.setText(productEntity.getAreaName());
		}
	}
}
