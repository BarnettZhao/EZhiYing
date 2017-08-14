package cn.antke.ezy.network.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuzhichao on 2017/5/4.
 * 商品
 */
public class ProductEntity {

	@SerializedName("goods_id")
	private String goodsId;
	@SerializedName("goods_name")
	private String goodsName;
	@SerializedName("pic_url")
	private String picUrl;
	@SerializedName("selling_price")
	private String sellingPrice;
	@SerializedName("selling_integral")
	private String sellingIntegral;
	@SerializedName("brand_name")
	private String brandName;
	@SerializedName("areaName")
	private String areaName;

	private boolean isChecked;
	private boolean isEditMode;

	public ProductEntity(String name, String picUrl, String integral) {
		this.goodsName = name;
		this.picUrl = picUrl;
		this.sellingIntegral = integral;
	}

	public ProductEntity(String goodsName, String picUrl, String sellingPrice, String sellingIntegral, String areaName) {
		this.goodsName = goodsName;
		this.picUrl = picUrl;
		this.sellingPrice = sellingPrice;
		this.sellingIntegral = sellingIntegral;
		this.areaName = areaName;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(String sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public String getSellingIntegral() {
		return sellingIntegral;
	}

	public void setSellingIntegral(String sellingIntegral) {
		this.sellingIntegral = sellingIntegral;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
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
