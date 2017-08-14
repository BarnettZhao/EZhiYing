package cn.antke.ezy.network.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhaoweiwei on 2016/12/25.
 * 收货地址
 */
public class AddressEntity implements Parcelable {

	@SerializedName("receiving_id")
	private String reciveId;
	@SerializedName("consignee")
	private String userName;
	@SerializedName("contacts")
	private String userPhone;
	@SerializedName("province_name")
	private String provinceName;
	@SerializedName("province")
	private String provinceId;
	@SerializedName("city_name")
	private String cityName;
	@SerializedName("city")
	private String cityId;
	@SerializedName("district_name")
	private String districtName;
	@SerializedName("district")
	private String districtId;
	@SerializedName("address")
	private String address;
	@SerializedName("is_default")
	private Boolean isDefault;

	protected AddressEntity(Parcel in) {
		reciveId = in.readString();
		userName = in.readString();
		userPhone = in.readString();
		provinceName = in.readString();
		provinceId = in.readString();
		cityName = in.readString();
		cityId = in.readString();
		districtName = in.readString();
		districtId = in.readString();
		address = in.readString();
		isDefault = in.readByte() != 0;
	}

	public static final Creator<AddressEntity> CREATOR = new Creator<AddressEntity>() {
		@Override
		public AddressEntity createFromParcel(Parcel in) {
			return new AddressEntity(in);
		}

		@Override
		public AddressEntity[] newArray(int size) {
			return new AddressEntity[size];
		}
	};

	public String getReciveId() {
		return reciveId;
	}

	public void setReciveId(String reciveId) {
		this.reciveId = reciveId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public String getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(String provinceId) {
		this.provinceId = provinceId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getDistrictId() {
		return districtId;
	}

	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Boolean isDefault() {
		return isDefault;
	}

	public void setDefault(Boolean aDefault) {
		isDefault = aDefault;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(reciveId);
		dest.writeString(userName);
		dest.writeString(userPhone);
		dest.writeString(provinceName);
		dest.writeString(provinceId);
		dest.writeString(cityName);
		dest.writeString(cityId);
		dest.writeString(districtName);
		dest.writeString(districtId);
		dest.writeString(address);
		dest.writeByte((byte) (isDefault ? 1 : 0));
	}
}
