package cn.antke.ezy.network.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zww on 2017/7/4.
 */

public class LogisticCompanyEntity {
    @SerializedName("logisticsId")
    private String logisticId;
    @SerializedName("logisticsName")
    private String logisticName;

    public String getLogisticId() {
        return logisticId;
    }

    public void setLogisticId(String logisticId) {
        this.logisticId = logisticId;
    }

    public String getLogisticName() {
        return logisticName;
    }

    public void setLogisticName(String logisticName) {
        this.logisticName = logisticName;
    }
}
