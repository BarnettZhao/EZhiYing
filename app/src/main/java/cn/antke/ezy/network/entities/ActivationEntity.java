package cn.antke.ezy.network.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhaoweiwei on 2017/5/18.
 * 消费服务中心激活列表
 */

public class ActivationEntity {
@SerializedName("activation_date")
	private String activationData;
	@SerializedName("integral")
	private int integral;
	@SerializedName("user_code")
	private String userCode;
	@SerializedName("integral_id")
	private String integralId;

	private boolean isSelected;

	public String getActivationData() {
		return activationData;
	}

	public void setActivationData(String activationData) {
		this.activationData = activationData;
	}

	public int getIntegral() {
		return integral;
	}

	public void setIntegral(int integral) {
		this.integral = integral;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getIntegralId() {
		return integralId;
	}

	public void setIntegralId(String integralId) {
		this.integralId = integralId;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}
}
