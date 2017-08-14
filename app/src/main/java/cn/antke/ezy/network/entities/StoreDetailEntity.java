package cn.antke.ezy.network.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liuzhichao on 2017/5/17.
 * 店铺详情
 */
public class StoreDetailEntity extends Entity {

	@SerializedName("store_id")
	private String storeId;
	@SerializedName("owner_head")
	private String logo;
	@SerializedName("first_pic")
	private String picUrl;
	@SerializedName("owner_name")
	private String name;
	@SerializedName("store_name")
	private String merchant;
	@SerializedName("goods_list")
	private List<ProductDetailEntity> productEntities;
	@SerializedName("totalPage")
	private int totalPage;

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMerchant() {
		return merchant;
	}

	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}

	public List<ProductDetailEntity> getProductEntities() {
		return productEntities;
	}

	public void setProductEntities(List<ProductDetailEntity> productEntities) {
		this.productEntities = productEntities;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
}
