package cn.antke.ezy.network.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuzhichao on 2017/6/16.
 * 交易大厅买卖信息
 */
public class DealBuySellEntity {

	@SerializedName("maxUsageIntegral")
	private int maxUsageIntegral;//最多可买
	@SerializedName("rate")
	private double rate;//价格比例
	@SerializedName("usage_integral")
	private int usageIntegral;//最多可卖
	@SerializedName("user_multifunctional")
	private int userMultifunctional;//多功能积分
	@SerializedName("volume")
	private int volume;//交易大厅总积分

	public int getMaxUsageIntegral() {
		return maxUsageIntegral;
	}

	public void setMaxUsageIntegral(int maxUsageIntegral) {
		this.maxUsageIntegral = maxUsageIntegral;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public int getUsageIntegral() {
		return usageIntegral;
	}

	public void setUsageIntegral(int usageIntegral) {
		this.usageIntegral = usageIntegral;
	}

	public int getUserMultifunctional() {
		return userMultifunctional;
	}

	public void setUserMultifunctional(int userMultifunctional) {
		this.userMultifunctional = userMultifunctional;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}
}
