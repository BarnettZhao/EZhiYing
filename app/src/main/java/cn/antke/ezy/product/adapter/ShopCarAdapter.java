package cn.antke.ezy.product.adapter;

import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.common.utils.StringUtil;
import com.common.view.ListViewForInner;
import com.common.widget.BaseRecycleAdapter;
import com.common.widget.RecyclerViewHolder;

import java.util.List;

import cn.antke.ezy.R;
import cn.antke.ezy.network.entities.ProductDetailEntity;
import cn.antke.ezy.network.entities.ProductEntity;
import cn.antke.ezy.network.entities.ShopCarEntity;
import cn.antke.ezy.product.controller.ShopCarActivity;
import cn.antke.ezy.utils.InputUtil;

/**
 * Created by liuzhichao on 2017/5/22.
 * 购物车adapter
 */
public class ShopCarAdapter extends BaseRecycleAdapter<ShopCarEntity> {

	private ShopCarActivity shopCarActivity;

	public ShopCarAdapter(List<ShopCarEntity> datas, ShopCarActivity shopCarActivity) {
		super(datas);
		this.shopCarActivity = shopCarActivity;
	}

	@Override
	public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return new RecyclerViewHolder(parent, R.layout.item_shop_car);
	}

	@Override
	public void onBindViewHolder(RecyclerViewHolder holder, int position) {
		ShopCarEntity shopCarEntity = getItemData(position);
		List<ProductDetailEntity> productEntities = shopCarEntity.getProductDetailEntities();

		CheckBox cbCarItemStore = holder.getView(R.id.cb_car_item_store);
		cbCarItemStore.setText(shopCarEntity.getStore());

		TextView tvCarItemEdit = holder.getView(R.id.tv_car_item_edit);
		if (shopCarEntity.isEditMode()) {
			tvCarItemEdit.setText(holder.getContext().getString(R.string.finished));
		} else {
			tvCarItemEdit.setText(holder.getContext().getString(R.string.edit));
		}

		for (ProductEntity productEntity : productEntities) {
			productEntity.setEditMode(shopCarEntity.isEditMode());
		}

		tvCarItemEdit.setOnClickListener(v -> {
			//让Activity也进入编辑模式
			shopCarEntity.setEditMode(!shopCarEntity.isEditMode());
			shopCarActivity.editMode(shopCarEntity.isEditMode());
			if (!shopCarEntity.isEditMode()) {
				//完成，退出编辑模式，保存修改后的数据

			}
			notifyDataSetChanged();
		});

		updateSubtotal(holder, shopCarEntity, productEntities);

		cbCarItemStore.setChecked(shopCarEntity.isChecked());

		if (productEntities.size() > 0) {

			ListViewForInner lvfiCarItemProducts = holder.getView(R.id.lvfi_car_item_products);
			CarProductAdapter adapter = new CarProductAdapter(shopCarActivity, productEntities, this, position);
			lvfiCarItemProducts.setAdapter(adapter);

			cbCarItemStore.setOnClickListener(v -> {
				//设置店铺选中状态
				shopCarEntity.setChecked(cbCarItemStore.isChecked());
				//设置店铺下的商品
				for (ProductEntity productEntity : productEntities) {
					productEntity.setChecked(cbCarItemStore.isChecked());
				}
				updateSubtotal(holder, shopCarEntity, productEntities);
				adapter.notifyDataSetChanged();
			});
		}
	}

	/**
	 * 更新小计统计，根据商品选中数量控制店铺的选中状态，更新总计
	 */
	void updateSubtotal(RecyclerViewHolder holder, ShopCarEntity shopCarEntity, List<ProductDetailEntity> productEntities) {
		int subIntegral = 0;
		int subPrice = 0;
		int checkNum = 0;
		for (ProductDetailEntity productEntity : productEntities) {
			if (productEntity.isChecked()) {
				checkNum++;
				int sellIntegral = StringUtil.parseInt(productEntity.getGoodsIntegral(), 0);
				int sellPrice = StringUtil.parseInt(productEntity.getGoodsPrice(), 0);

				subIntegral += (sellIntegral * productEntity.getCount());
				subPrice += (sellPrice * productEntity.getCount());
			}
		}
		if (checkNum == productEntities.size()) {
			shopCarEntity.setChecked(true);
		} else {
			shopCarEntity.setChecked(false);
		}
		holder.setText(R.id.tv_car_item_subtotal, holder.getContext().getString(R.string.subtotal_integral, InputUtil.formatPrice(subIntegral, subPrice)));
		shopCarActivity.updateCount();
	}
}
