package cn.antke.ezy.network.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuzhichao on 2017/6/17.
 * 支付
 */
public class PayEntity extends Entity {

	@SerializedName("prepay_id")
	private String payId;
	@SerializedName("trade_no")
	private String tradeNo;
	@SerializedName("pay_type")
	private String payWay;
	@SerializedName("total")
	private String total;

	public String getPayId() {
		return payId;
	}

	public void setPayId(String payId) {
		this.payId = payId;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getPayWay() {
		return payWay;
	}

	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}
}
