package cn.antke.ezy.network.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liuzhichao on 2017/5/22.
 * 购物车
 */
public class ShopCarEntity {

	@SerializedName("store_id")
	private String id;
	@SerializedName("store_name")
	private String store;
	@SerializedName("goods_list")
	private List<ProductDetailEntity> productDetailEntities;
	private boolean isChecked;
	private boolean isEditMode;

	public ShopCarEntity(String store) {
		this.store = store;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public List<ProductDetailEntity> getProductDetailEntities() {
		return productDetailEntities;
	}

	public void setProductDetailEntities(List<ProductDetailEntity> productDetailEntities) {
		this.productDetailEntities = productDetailEntities;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean checked) {
		isChecked = checked;
	}

	public boolean isEditMode() {
		return isEditMode;
	}

	public void setEditMode(boolean editMode) {
		isEditMode = editMode;
	}
}
