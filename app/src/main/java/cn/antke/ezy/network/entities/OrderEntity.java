package cn.antke.ezy.network.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zhaoweiwei on 2017/5/11.
 */

public class OrderEntity implements Parcelable {
    @SerializedName("store_name")
    private String storeName;
    @SerializedName("store_id")
    private String storeId;
    @SerializedName("status")
    private String status;
    @SerializedName("total_price")
    private String orderTotal;//商品小计
    @SerializedName("total_integral")
    private String orderTotalInregral;//商品小计积分
    @SerializedName("order_id")
    private String orderId;
    @SerializedName("refund_id")
    private String refundId;
    @SerializedName("refund_reason")
    private String refundReason;
    @SerializedName("order_code")
    private String orderCode;
    @SerializedName("refund_code")
    private String refundCode;
    @SerializedName("logistics_cost")
    private String logisticCost;//运费金额
    @SerializedName("logistics_integral")
    private String logisticCostIntegral;//运费积分
    @SerializedName("consignee")
    private String consignee;//申请人
    @SerializedName("contacts")
    private String contacts;//申请电话
    @SerializedName("address")
    private String address;
    @SerializedName("trade_no")
    private String tradeNo;
    @SerializedName("is_payIntegral")
    private String isPayIntegral;//1 已经用积分支付过 2  未用积分支付过

    @SerializedName("goods_list")
    private List<OrderGoodEntity> goodEntities;

    protected OrderEntity(Parcel in) {
        storeName = in.readString();
        storeId = in.readString();
        status = in.readString();
        orderTotal = in.readString();
        orderTotalInregral = in.readString();
        orderId = in.readString();
        refundId = in.readString();
        refundReason = in.readString();
        orderCode = in.readString();
        refundCode = in.readString();
        logisticCost = in.readString();
        logisticCostIntegral = in.readString();
        consignee = in.readString();
        contacts = in.readString();
        address = in.readString();
        tradeNo = in.readString();
        isPayIntegral = in.readString();
        goodEntities = in.createTypedArrayList(OrderGoodEntity.CREATOR);
    }

    public static final Creator<OrderEntity> CREATOR = new Creator<OrderEntity>() {
        @Override
        public OrderEntity createFromParcel(Parcel in) {
            return new OrderEntity(in);
        }

        @Override
        public OrderEntity[] newArray(int size) {
            return new OrderEntity[size];
        }
    };

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusName() {
        //订单状态：0 全部 1 待支付 2 已支付 3 已发货 4 已完成 5 取消 6、申请中；7、退款中；8、已退款；9、拒绝；10、取消
        //20:待支付 21：待发货 22：待收货 23 已完成  24 已取消  25：等待商家确认
        String statusName = "";
        switch (status) {
            case "1":
                statusName = "待支付";
                break;
            case "2":
            case "21":
                statusName = "待发货";
                break;
            case "3":
            case "22":
                statusName = "待收货";
                break;
            case "4":
            case "23":
                statusName = "已完成";
                break;
            case "5":
                statusName = "取消订单";
                break;
            case "6":
                statusName = "退款申请";
                break;
            case "7":
                statusName = "退款中";
                break;
            case "8":
                statusName = "已退款";
                break;
            case "9":
                statusName = "拒绝退款";
                break;
            case "10":
                statusName="取消退款";
                break;
        }
        return statusName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(String orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getOrderTotalInregral() {
        return orderTotalInregral;
    }

    public void setOrderTotalInregral(String orderTotalInregral) {
        this.orderTotalInregral = orderTotalInregral;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getRefundCode() {
        return refundCode;
    }

    public void setRefundCode(String refundCode) {
        this.refundCode = refundCode;
    }

    public String getLogisticCost() {
        return logisticCost;
    }

    public void setLogisticCost(String logisticCost) {
        this.logisticCost = logisticCost;
    }

    public String getLogisticCostIntegral() {
        return logisticCostIntegral;
    }

    public void setLogisticCostIntegral(String logisticCostIntegral) {
        this.logisticCostIntegral = logisticCostIntegral;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getIsPayIntegral() {
        return isPayIntegral;
    }

    public void setIsPayIntegral(String isPayIntegral) {
        this.isPayIntegral = isPayIntegral;
    }

    public List<OrderGoodEntity> getGoodEntities() {
        return goodEntities;
    }

    public void setGoodEntities(List<OrderGoodEntity> goodEntities) {
        this.goodEntities = goodEntities;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(storeName);
        parcel.writeString(storeId);
        parcel.writeString(status);
        parcel.writeString(orderTotal);
        parcel.writeString(orderTotalInregral);
        parcel.writeString(orderId);
        parcel.writeString(refundId);
        parcel.writeString(refundReason);
        parcel.writeString(orderCode);
        parcel.writeString(refundCode);
        parcel.writeString(logisticCost);
        parcel.writeString(logisticCostIntegral);
        parcel.writeString(consignee);
        parcel.writeString(contacts);
        parcel.writeString(address);
        parcel.writeString(tradeNo);
        parcel.writeString(isPayIntegral);
        parcel.writeTypedList(goodEntities);
    }
}


