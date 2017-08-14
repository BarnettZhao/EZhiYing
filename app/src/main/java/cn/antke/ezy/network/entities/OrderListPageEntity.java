package cn.antke.ezy.network.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zhaoweiwei on 2017/5/11.
 */

public class OrderListPageEntity {
	@SerializedName("order_list")
	private List<OrderEntity> orderEntities;
	@SerializedName("totalPage")
	private int totalPager;

	public List<OrderEntity> getOrderEntities() {
		return orderEntities;
	}

	public void setOrderEntities(List<OrderEntity> orderEntities) {
		this.orderEntities = orderEntities;
	}

	public int getTotalPager() {
		return totalPager;
	}

	public void setTotalPager(int totalPager) {
		this.totalPager = totalPager;
	}
}
