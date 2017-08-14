package cn.antke.ezy.network.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuzhichao on 2017/6/13.
 * 商品详情
 */
public class ProductDetailEntity extends ProductEntity {

	public ProductDetailEntity(String name, String picUrl, String integral) {
		super(name, picUrl, integral);
	}

	public ProductDetailEntity(String goodsName, String picUrl, String sellingPrice, String sellingIntegral, String address) {
		super(goodsName, picUrl, sellingPrice, sellingIntegral, address);
	}

	@SerializedName("store_id")
	private String storeId;
	@SerializedName("store_name")
	private String storeName;
	@SerializedName("store_icon")
	private String storeLogo;
	@SerializedName("goods_number")
	private String goodsNumber;//库存
	@SerializedName("link_url")
	private String content;//内容url
	@SerializedName("sales_count")
	private String salesCount;//销量
	@SerializedName("address")
	private String address;
	@SerializedName("pic_list")
	private List<String> picList;
	@SerializedName("logistics_cost")
	private String logisticsCost;//物流金额
	@SerializedName("logistics_integral")
	private String logisticsIntegral;//物流积分
	@SerializedName("goods_brief")
	private String goodsBrief;//商品广告语
	@SerializedName("is_open")
	private int isOpen;//是否开店，1-不需要，2需要
	@SerializedName("distribution_mode")
	private String distributionMode;//配送方式
	@SerializedName("attribute_list")
	private ArrayList<NormEntity> attributeList;//属性列表
//	@SerializedName("extend_list")
//	private List<NormEntity> extendList;//扩展属性列表

	//购物车部分
	@SerializedName("brand_id")
	private String brandId;
	@SerializedName("cart_id")
	private String cartId;//购物车id
	@SerializedName("count")
	private int count;//购买数量
	@SerializedName("goods_integral")
	private String goodsIntegral;
	@SerializedName("goods_price")
	private String goodsPrice;
	@SerializedName("sku_id")
	private String skuId;
	@SerializedName("all_attribute_list")
	private ArrayList<NormEntity> normEntities;//全部属性列表

	//确认订单部分
	@SerializedName("order_integral")
	private String orderIntegral;//订单积分
	@SerializedName("order_total")
	private String orderTotal;//订单价格

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getStoreLogo() {
		return storeLogo;
	}

	public void setStoreLogo(String storeLogo) {
		this.storeLogo = storeLogo;
	}

	public String getGoodsNumber() {
		return goodsNumber;
	}

	public void setGoodsNumber(String goodsNumber) {
		this.goodsNumber = goodsNumber;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSalesCount() {
		return salesCount;
	}

	public void setSalesCount(String salesCount) {
		this.salesCount = salesCount;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<String> getPicList() {
		return picList;
	}

	public void setPicList(List<String> picList) {
		this.picList = picList;
	}

	public String getLogisticsCost() {
		return logisticsCost;
	}

	public void setLogisticsCost(String logisticsCost) {
		this.logisticsCost = logisticsCost;
	}

	public String getLogisticsIntegral() {
		return logisticsIntegral;
	}

	public void setLogisticsIntegral(String logisticsIntegral) {
		this.logisticsIntegral = logisticsIntegral;
	}

	public String getGoodsBrief() {
		return goodsBrief;
	}

	public void setGoodsBrief(String goodsBrief) {
		this.goodsBrief = goodsBrief;
	}

	public int getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(int isOpen) {
		this.isOpen = isOpen;
	}

	public String getDistributionMode() {
		return distributionMode;
	}

	public void setDistributionMode(String distributionMode) {
		this.distributionMode = distributionMode;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getCartId() {
		return cartId;
	}

	public void setCartId(String cartId) {
		this.cartId = cartId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getGoodsIntegral() {
		return goodsIntegral;
	}

	public void setGoodsIntegral(String goodsIntegral) {
		this.goodsIntegral = goodsIntegral;
	}

	public String getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(String goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getOrderIntegral() {
		return orderIntegral;
	}

	public void setOrderIntegral(String orderIntegral) {
		this.orderIntegral = orderIntegral;
	}

	public String getOrderTotal() {
		return orderTotal;
	}

	public void setOrderTotal(String orderTotal) {
		this.orderTotal = orderTotal;
	}

	public ArrayList<NormEntity> getAttributeList() {
		return attributeList;
	}

	public void setAttributeList(ArrayList<NormEntity> attributeList) {
		this.attributeList = attributeList;
	}

	public ArrayList<NormEntity> getNormEntities() {
		return normEntities;
	}

	public void setNormEntities(ArrayList<NormEntity> normEntities) {
		this.normEntities = normEntities;
	}

	public static class NormEntity implements Parcelable {

		@SerializedName("atrr_vlaue_list")
		private List<NormEntity> normEntities;

		@SerializedName("attr_name")
		private String attrName;
		@SerializedName("img_url")
		private String imgUrl;
		@SerializedName("relation_id")
		private String relationId;
		@SerializedName("value_id")
		private String attrId;
		@SerializedName("value_name")
		private String attrValue;

		protected NormEntity(Parcel in) {
			normEntities = in.createTypedArrayList(NormEntity.CREATOR);
			attrName = in.readString();
			imgUrl = in.readString();
			relationId = in.readString();
			attrId = in.readString();
			attrValue = in.readString();
		}

		public static final Creator<NormEntity> CREATOR = new Creator<NormEntity>() {
			@Override
			public NormEntity createFromParcel(Parcel in) {
				return new NormEntity(in);
			}

			@Override
			public NormEntity[] newArray(int size) {
				return new NormEntity[size];
			}
		};

		public List<NormEntity> getNormEntities() {
			return normEntities;
		}

		public void setNormEntities(List<NormEntity> normEntities) {
			this.normEntities = normEntities;
		}

		public String getAttrName() {
			return attrName;
		}

		public void setAttrName(String attrName) {
			this.attrName = attrName;
		}

		public String getImgUrl() {
			return imgUrl;
		}

		public void setImgUrl(String imgUrl) {
			this.imgUrl = imgUrl;
		}

		public String getRelationId() {
			return relationId;
		}

		public void setRelationId(String relationId) {
			this.relationId = relationId;
		}

		public String getAttrId() {
			return attrId;
		}

		public void setAttrId(String attrId) {
			this.attrId = attrId;
		}

		public String getAttrValue() {
			return attrValue;
		}

		public void setAttrValue(String attrValue) {
			this.attrValue = attrValue;
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeTypedList(normEntities);
			dest.writeString(attrName);
			dest.writeString(imgUrl);
			dest.writeString(relationId);
			dest.writeString(attrId);
			dest.writeString(attrValue);
		}
	}
}
