package cn.antke.ezy.network.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liuzhichao on 2017/6/17.
 * 订单确认页
 */
public class OrderConfirmEntity {

	@SerializedName("receiving_id")
	private String receivingId;
	@SerializedName("consignee")
	private String consignee;
	@SerializedName("contacts")
	private String contacts;
	@SerializedName("address")
	private String address;

	@SerializedName("integral_paylist")
	private List<PayWay> integralPayList;
	@SerializedName("store_list")
	private List<StoreDetailEntity> storeList;

	public String getReceivingId() {
		return receivingId;
	}

	public void setReceivingId(String receivingId) {
		this.receivingId = receivingId;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getContacts() {
		return contacts;
	}

	public void setContacts(String contacts) {
		this.contacts = contacts;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<PayWay> getIntegralPayList() {
		return integralPayList;
	}

	public void setIntegralPayList(List<PayWay> integralPayList) {
		this.integralPayList = integralPayList;
	}

	public List<StoreDetailEntity> getStoreList() {
		return storeList;
	}

	public void setStoreList(List<StoreDetailEntity> storeList) {
		this.storeList = storeList;
	}

	public class PayWay {

		@SerializedName("integral_type")
		private String integralType;
		@SerializedName("integral_multiple")
		private String integralMultiple;
		@SerializedName("multiple")
		private int multiple;
		private boolean isSelected;

		public String getIntegralType() {
			return integralType;
		}

		public void setIntegralType(String integralType) {
			this.integralType = integralType;
		}

		public String getIntegralMultiple() {
			return integralMultiple;
		}

		public void setIntegralMultiple(String integralMultiple) {
			this.integralMultiple = integralMultiple;
		}

		public int getMultiple() {
			return multiple;
		}

		public void setMultiple(int multiple) {
			this.multiple = multiple;
		}

		public boolean isSelected() {
			return isSelected;
		}

		public void setSelected(boolean selected) {
			isSelected = selected;
		}
	}
}
