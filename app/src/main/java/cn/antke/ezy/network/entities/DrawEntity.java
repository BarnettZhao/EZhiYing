package cn.antke.ezy.network.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by liuzhichao on 2017/5/11.
 * 抽奖
 */
public class DrawEntity {

	@SerializedName("link_url")
	private String contentUrl;

	@SerializedName("picInfoList")
	private List<String> picList;
	@SerializedName("goods_pic")
	private String picUrl;

	@SerializedName("prize")
	private DrawEntity drawEntity;
	@SerializedName("goods_id")
	private String productId;
	@SerializedName("prize_id")
	private String drawId;
	@SerializedName("goods_name")
	private String name;
	@SerializedName("pic_url")
	private String imgUrl;
	@SerializedName("integral")
	private String price;
	@SerializedName("prize_count")
	private String num;
	@SerializedName("prize_times")
	private String allNum;
	@SerializedName("prize_status")
	private String status;//1-未开奖，2-已开奖

	public DrawEntity(String name, String picUrl, String price, String num, String allNum) {
		this.name = name;
		this.picUrl = picUrl;
		this.price = price;
		this.num = num;
		this.allNum = allNum;
	}

	public String getContentUrl() {
		return contentUrl;
	}

	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public List<String> getPicList() {
		return picList;
	}

	public void setPicList(List<String> picList) {
		this.picList = picList;
	}

	public DrawEntity getDrawEntity() {
		return drawEntity;
	}

	public void setDrawEntity(DrawEntity drawEntity) {
		this.drawEntity = drawEntity;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getDrawId() {
		return drawId;
	}

	public void setDrawId(String drawId) {
		this.drawId = drawId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getAllNum() {
		return allNum;
	}

	public void setAllNum(String allNum) {
		this.allNum = allNum;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
