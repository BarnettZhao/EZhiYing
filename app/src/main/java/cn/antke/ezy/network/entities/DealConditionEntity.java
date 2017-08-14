package cn.antke.ezy.network.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liuzhichao on 2017/6/16.
 * 交易大厅缴费信息查询
 */
public class DealConditionEntity {

	@SerializedName("admission_list")
	private List<Admission> admissionList;
	@SerializedName("is_pay")
	private int isPay;//1: 是  2：否
	@SerializedName("pay_type")
	private String payType;//1，支付宝；2，微信

	public int getIsPay() {
		return isPay;
	}

	public void setIsPay(int isPay) {
		this.isPay = isPay;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public List<Admission> getAdmissionList() {
		return admissionList;
	}

	public void setAdmissionList(List<Admission> admissionList) {
		this.admissionList = admissionList;
	}

	public class Admission {

		@SerializedName("admission_type")
		private String admissionType;//1,个人；2，企业
		@SerializedName("admission_money")
		private String admissionMoney;

		public String getAdmissionType() {
			return admissionType;
		}

		public void setAdmissionType(String admissionType) {
			this.admissionType = admissionType;
		}

		public String getAdmissionMoney() {
			return admissionMoney;
		}

		public void setAdmissionMoney(String admissionMoney) {
			this.admissionMoney = admissionMoney;
		}
	}
}
