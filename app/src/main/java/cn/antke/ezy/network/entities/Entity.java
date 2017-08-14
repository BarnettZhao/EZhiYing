package cn.antke.ezy.network.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jacktian on 15/8/19.
 * 实体基类
 */
public class Entity {

	@SerializedName("result_code")
	private String resultCode;
	@SerializedName("result_desc")
	private String resultMsg;

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}
}
