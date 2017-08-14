package cn.antke.ezy.network.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zww on 2017/6/19.
 */

public class IntegralEntity {
    @SerializedName("doubly_total")
    private String doublyTotal;
    @SerializedName("integral_bond")
    private String integralBond;
    @SerializedName("integral_freeze")
    private String integralFreeze;
    @SerializedName("integral_multifunctional")
    private String integralFunction;
    @SerializedName("integral_red")
    private String integralRed;
    @SerializedName("integral_share")
    private String integralShare;
    @SerializedName("integral_shopping")
    private String integralShop;
    @SerializedName("integral_usable")
    private String integralUsable;
    @SerializedName("operation_status")
    private String operationStatus;

    public String getDoublyTotal() {
        return doublyTotal;
    }

    public void setDoublyTotal(String doublyTotal) {
        this.doublyTotal = doublyTotal;
    }

    public String getIntegralBond() {
        return integralBond;
    }

    public void setIntegralBond(String integralBond) {
        this.integralBond = integralBond;
    }

    public String getIntegralFreeze() {
        return integralFreeze;
    }

    public void setIntegralFreeze(String integralFreeze) {
        this.integralFreeze = integralFreeze;
    }

    public String getIntegralFunction() {
        return integralFunction;
    }

    public void setIntegralFunction(String integralFunction) {
        this.integralFunction = integralFunction;
    }

    public String getIntegralRed() {
        return integralRed;
    }

    public void setIntegralRed(String integralRed) {
        this.integralRed = integralRed;
    }

    public String getIntegralShare() {
        return integralShare;
    }

    public void setIntegralShare(String integralShare) {
        this.integralShare = integralShare;
    }

    public String getIntegralShop() {
        return integralShop;
    }

    public void setIntegralShop(String integralShop) {
        this.integralShop = integralShop;
    }

    public String getIntegralUsable() {
        return integralUsable;
    }

    public void setIntegralUsable(String integralUsable) {
        this.integralUsable = integralUsable;
    }

    public String getOperationStatus() {
        return operationStatus;
    }

    public void setOperationStatus(String operationStatus) {
        this.operationStatus = operationStatus;
    }
}
