package cn.antke.ezy.network.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zww on 2017/6/18.
 */

public class ConsumerServiceEntity {
    @SerializedName("consume_date")
    private String consumerTime;
    @SerializedName("consume_desc")
    private String consumerDesc;
    @SerializedName("consume_total")
    private String consumerTotal;
    @SerializedName("integral_id")
    private String integralId;

    public String getConsumerTime() {
        return consumerTime;
    }

    public void setConsumerTime(String consumerTime) {
        this.consumerTime = consumerTime;
    }

    public String getConsumerDesc() {
        return consumerDesc;
    }

    public void setConsumerDesc(String consumerDesc) {
        this.consumerDesc = consumerDesc;
    }

    public String getConsumerTotal() {
        return consumerTotal;
    }

    public void setConsumerTotal(String consumerTotal) {
        this.consumerTotal = consumerTotal;
    }

    public String getIntegralId() {
        return integralId;
    }

    public void setIntegralId(String integralId) {
        this.integralId = integralId;
    }
}
