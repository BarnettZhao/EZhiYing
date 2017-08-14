package cn.antke.ezy.network.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhaoweiwei on 2017/5/21.
 */

public class LotteryInquiryEntity {
	@SerializedName("prize_num")
	private String periods;
	@SerializedName("strCreateDate")
	private String time;
	@SerializedName("total")
	private String integral;
	@SerializedName("result")
	private String result;

	public String getPeriods() {
		return periods;
	}

	public void setPeriods(String periods) {
		this.periods = periods;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getIntegral() {
		return integral;
	}

	public void setIntegral(String integral) {
		this.integral = integral;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
