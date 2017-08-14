package cn.antke.ezy.network.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zww on 2017/6/24.
 */

public class LogisticEnity {
    @SerializedName("logistics_name")
    private String logisticName;
    @SerializedName("logistics_no")
    private String logisticNo;
    @SerializedName("logistics_phone")
    private String logisticPhone;
    @SerializedName("logistics_list")
    private List<LogisticItemEntity> logisticItemEntities;

    public String getLogisticName() {
        return logisticName;
    }

    public void setLogisticName(String logisticName) {
        this.logisticName = logisticName;
    }

    public String getLogisticNo() {
        return logisticNo;
    }

    public void setLogisticNo(String logisticNo) {
        this.logisticNo = logisticNo;
    }

    public String getLogisticPhone() {
        return logisticPhone;
    }

    public void setLogisticPhone(String logisticPhone) {
        this.logisticPhone = logisticPhone;
    }

    public List<LogisticItemEntity> getLogisticItemEntities() {
        return logisticItemEntities;
    }

    public void setLogisticItemEntities(List<LogisticItemEntity> logisticItemEntities) {
        this.logisticItemEntities = logisticItemEntities;
    }
}
