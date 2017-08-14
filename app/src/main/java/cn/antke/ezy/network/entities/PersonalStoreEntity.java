package cn.antke.ezy.network.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuzhichao on 2017/6/21.
 * 个人店铺
 */
public class PersonalStoreEntity {

	@SerializedName("first_pic")
	private String picUrl;
	@SerializedName("store_name")
	private String name;
	@SerializedName("contact")
	private String contact;
	@SerializedName("area_name")
	private String area;
	@SerializedName("address")
	private String address;
	@SerializedName("contact_name")
	private String contactName;

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}
}
