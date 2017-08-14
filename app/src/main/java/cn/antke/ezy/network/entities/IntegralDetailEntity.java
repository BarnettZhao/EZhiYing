package cn.antke.ezy.network.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zhaoweiwei on 2017/5/26.
 * 积分
 */

public class IntegralDetailEntity {
    @SerializedName("integral")
    private String integral;
    @SerializedName("integral_list")
    private List<IntegralItemEntity> integralListEntities;
    @SerializedName("total_page")
    private int totalPage;

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    public List<IntegralItemEntity> getIntegralListEntities() {
        return integralListEntities;
    }

    public void setIntegralListEntities(List<IntegralItemEntity> integralListEntities) {
        this.integralListEntities = integralListEntities;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
