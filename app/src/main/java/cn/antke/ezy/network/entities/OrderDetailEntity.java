package cn.antke.ezy.network.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zww on 2017/6/20.
 */

public class OrderDetailEntity {
    @SerializedName("address")
    private String address;
    @SerializedName("consignee")
    private String userName;
    @SerializedName("contacts")
    private String phone;
    @SerializedName("create_date")
    private String creatData;
    @SerializedName("order_code")
    private String orderCode;
    @SerializedName("order_id")
    private String orderId;
    @SerializedName("order_integral")
    private String orderIntegral;//总积分
    @SerializedName("order_price")
    private String orderPrice;//总金额
    @SerializedName("order_out_itme")
    private String outTime;
    @SerializedName("pay_time")
    private String payTime;
    @SerializedName("status")
    private String status;
    @SerializedName("refund_id")
    private String refundId;
    @SerializedName("refund_reason")
    private String refundReason;
    @SerializedName("refund_describe")
    private String refundDescribe;
    @SerializedName("refund_code")
    private String refundCode;
    @SerializedName("apply_time")
    private String applyTime;
    @SerializedName("refund_time")
    private String refundTime;
    @SerializedName("trade_no")
    private String tradeNo;
    @SerializedName("is_payIntegral")
    private String isPayIntegral;//1 已经用积分支付过 2  未用积分支付过
    @SerializedName("order_list")
    private List<OrderEntity> orderEntities;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreatData() {
        return creatData;
    }

    public void setCreatData(String creatData) {
        this.creatData = creatData;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderIntegral() {
        return orderIntegral;
    }

    public void setOrderIntegral(String orderIntegral) {
        this.orderIntegral = orderIntegral;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusName() {
        //订单状态：1，待支付；2，待发货；3，待收货；4已完成；5：取消 6 退款 10，全部
        String statusName = "";
        switch (status) {
            case "1":
                statusName = "待支付";
                break;
            case "2":
                statusName = "待发货";
                break;
            case "3":
                statusName = "待收货";
                break;
            case "4":
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
            case"10":
                statusName = "取消退款";
                break;
        }
        return statusName;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getRefundDescribe() {
        return refundDescribe;
    }

    public void setRefundDescribe(String refundDescribe) {
        this.refundDescribe = refundDescribe;
    }

    public String getRefundCode() {
        return refundCode;
    }

    public void setRefundCode(String refundCode) {
        this.refundCode = refundCode;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public String getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(String refundTime) {
        this.refundTime = refundTime;
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

    public List<OrderEntity> getOrderEntities() {
        return orderEntities;
    }

    public void setOrderEntities(List<OrderEntity> orderEntities) {
        this.orderEntities = orderEntities;
    }
}
