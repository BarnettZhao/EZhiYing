package cn.antke.ezy.network.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuzhichao on 2017/5/23.
 * 交易大厅-卖出
 */
public class SaleEntity {

	@SerializedName("sellout_id")
	private String id;
	@SerializedName("usable_integral")
	private String saleIntegral;
	@SerializedName("multifunctional_integral")
	private String salePrice;
	@SerializedName("sellout_date")
	private String date;
	@SerializedName("is_undo")
	private int isUndo;//1-可以撤销，2-不能撤销

	public SaleEntity(String saleIntegral, String salePrice, String date) {
		this.saleIntegral = saleIntegral;
		this.salePrice = salePrice;
		this.date = date;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSaleIntegral() {
		return saleIntegral;
	}

	public void setSaleIntegral(String saleIntegral) {
		this.saleIntegral = saleIntegral;
	}

	public String getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(String salePrice) {
		this.salePrice = salePrice;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getIsUndo() {
		return isUndo;
	}

	public void setIsUndo(int isUndo) {
		this.isUndo = isUndo;
	}
}
