package cn.antke.ezy.network.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by liuzhichao on 2017/5/16.
 * 首页轮播
 */
public class BannerEntity {

	@SerializedName("pic_url")
	private String imgUrl;
	@SerializedName("descriptions")
	private String title;
	@SerializedName("link_url")
	private String linkUrl;
	@SerializedName("link_type")
	private String type;//1网页,2店铺,3商品

	public BannerEntity(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
