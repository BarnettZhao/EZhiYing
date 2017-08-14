package cn.antke.ezy.network.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zww on 2017/6/18.
 */

public class ConsumerServicePageEntity {
    @SerializedName("red_integral")
    private String IntegralNum;
    @SerializedName("start_date")
    private String startTime;
    @SerializedName("end_date")
    private String endTime;
    @SerializedName("total_page")
    private int totalPage;
    @SerializedName("effective")
    private String effictive;
    @SerializedName("integral_list")
    private List<ConsumerServiceEntity> entities;

    public String getIntegralNum() {
        return IntegralNum;
    }

    public void setIntegralNum(String integralNum) {
        IntegralNum = integralNum;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public String getEffictive() {
        return effictive;
    }

    public void setEffictive(String effictive) {
        this.effictive = effictive;
    }

    public List<ConsumerServiceEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<ConsumerServiceEntity> entities) {
        this.entities = entities;
    }
}
