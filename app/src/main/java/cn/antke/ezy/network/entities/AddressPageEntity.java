package cn.antke.ezy.network.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zhaoweiwei on 2017/5/10.
 * 地址分页
 */

public class AddressPageEntity {
	@SerializedName("receiving_list")
	private List<AddressEntity> addressEntities;
	@SerializedName("total_page")
	private int totalPage;

	public List<AddressEntity> getAddressEntities() {
		return addressEntities;
	}

	public void setAddressEntities(List<AddressEntity> addressEntities) {
		this.addressEntities = addressEntities;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
}
