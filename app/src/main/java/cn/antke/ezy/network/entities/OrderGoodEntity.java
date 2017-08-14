package cn.antke.ezy.network.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zhaoweiwei on 2017/5/18.
 */

public class OrderGoodEntity implements Parcelable {
    @SerializedName("head_pic")
    private String goodsPic;
    @SerializedName("count")
    private String goodsNum;
    @SerializedName("goods_name")
    private String goodsName;
    @SerializedName("goods_id")
    private String goodsId;
    @SerializedName("attribute")
    private String attribute;
    @SerializedName("goods_brief")
    private String goodsBrief;//商品描述

    protected OrderGoodEntity(Parcel in) {
        goodsPic = in.readString();
        goodsNum = in.readString();
        goodsName = in.readString();
        goodsId = in.readString();
        attribute = in.readString();
        goodsBrief = in.readString();
    }

    public static final Creator<OrderGoodEntity> CREATOR = new Creator<OrderGoodEntity>() {
        @Override
        public OrderGoodEntity createFromParcel(Parcel in) {
            return new OrderGoodEntity(in);
        }

        @Override
        public OrderGoodEntity[] newArray(int size) {
            return new OrderGoodEntity[size];
        }
    };

    public String getGoodsPic() {
        return goodsPic;
    }

    public void setGoodsPic(String goodsPic) {
        this.goodsPic = goodsPic;
    }

    public String getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(String goodsNum) {
        this.goodsNum = goodsNum;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getGoodsBrief() {
        return goodsBrief;
    }

    public void setGoodsBrief(String goodsBrief) {
        this.goodsBrief = goodsBrief;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(goodsPic);
        parcel.writeString(goodsNum);
        parcel.writeString(goodsName);
        parcel.writeString(goodsId);
        parcel.writeString(attribute);
        parcel.writeString(goodsBrief);
    }
}
