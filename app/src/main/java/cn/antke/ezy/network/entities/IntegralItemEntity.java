package cn.antke.ezy.network.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zww on 2017/6/16.
 */

public class IntegralItemEntity {
    @SerializedName("integral_no")
    private String integralNo;
    @SerializedName("remark")
    private String remark;
    @SerializedName("return_date")
    private String returnDate;
    @SerializedName("total")
    private String total;
    @SerializedName("trade_date")
    private String tradeDate;

    public String getIntegralNo() {
        return integralNo;
    }

    public void setIntegralNo(String integralNo) {
        this.integralNo = integralNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }
}
