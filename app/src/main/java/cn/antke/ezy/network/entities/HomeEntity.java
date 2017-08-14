package cn.antke.ezy.network.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liuzhichao on 2017/5/16.
 * 首页实体类
 */
public class HomeEntity extends Entity {

	@SerializedName("goods_list")
	private List<ProductDetailEntity> productEntities;
	@SerializedName("pic_list")
	private List<BannerEntity> bannerEntities;
	@SerializedName("channel_list")
	private List<PlateEntity> plateEntities;

	public List<ProductDetailEntity> getProductEntities() {
		return productEntities;
	}

	public void setProductEntities(List<ProductDetailEntity> productEntities) {
		this.productEntities = productEntities;
	}

	public List<BannerEntity> getBannerEntities() {
		return bannerEntities;
	}

	public void setBannerEntities(List<BannerEntity> bannerEntities) {
		this.bannerEntities = bannerEntities;
	}

	public List<PlateEntity> getPlateEntities() {
		return plateEntities;
	}

	public void setPlateEntities(List<PlateEntity> plateEntities) {
		this.plateEntities = plateEntities;
	}
}
