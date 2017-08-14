package cn.antke.ezy.network.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liuzhichao on 2017/6/16.
 * 交易大厅
 */
public class DealHallEntity {

	@SerializedName("now_rate_value")
	private String nowRateValue;
	@SerializedName("rate")
	private String rate;
	@SerializedName("volume")
	private String volume;
	@SerializedName("user_multifunctional")
	private String userMultifunctional;
	@SerializedName("purchase_list")
	private List<DealRecordEntity> dealRecordEntities;
	@SerializedName("rate_list")
	private List<Purchase> purchaseList;

	public String getNowRateValue() {
		return nowRateValue;
	}

	public void setNowRateValue(String nowRateValue) {
		this.nowRateValue = nowRateValue;
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getUserMultifunctional() {
		return userMultifunctional;
	}

	public void setUserMultifunctional(String userMultifunctional) {
		this.userMultifunctional = userMultifunctional;
	}

	public List<DealRecordEntity> getDealRecordEntities() {
		return dealRecordEntities;
	}

	public void setDealRecordEntities(List<DealRecordEntity> dealRecordEntities) {
		this.dealRecordEntities = dealRecordEntities;
	}

	public List<Purchase> getPurchaseList() {
		return purchaseList;
	}

	public void setPurchaseList(List<Purchase> purchaseList) {
		this.purchaseList = purchaseList;
	}

	public class Purchase {
		@SerializedName("count")
		private int y;
		@SerializedName("tradingDate")
		private int x;

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}
	}
}
