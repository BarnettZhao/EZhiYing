package cn.antke.ezy.network.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuzhichao on 2017/5/12.
 * 交易记录
 */
public class DealRecordEntity {

	@SerializedName("usable_integral")
	private String consumeNum;
	@SerializedName("multifunctional_integral")
	private String buyNum;
	@SerializedName("buy_time")
	private String date;

	public DealRecordEntity(String consumeNum, String buyNum, String date) {
		this.consumeNum = consumeNum;
		this.buyNum = buyNum;
		this.date = date;
	}

	public String getConsumeNum() {
		return consumeNum;
	}

	public void setConsumeNum(String consumeNum) {
		this.consumeNum = consumeNum;
	}

	public String getBuyNum() {
		return buyNum;
	}

	public void setBuyNum(String buyNum) {
		this.buyNum = buyNum;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
