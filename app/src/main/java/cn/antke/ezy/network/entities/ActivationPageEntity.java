package cn.antke.ezy.network.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zww on 2017/6/18.
 */

public class ActivationPageEntity {
    @SerializedName("total_page")
    private int totalPage;
    @SerializedName("integral_red")
    private int integralRed;
    @SerializedName("integral_use")
    private int integralUse;
    @SerializedName("ratio")
    private int ratio;
    @SerializedName("activation_list")
    private List<ActivationEntity> entities;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getIntegralRed() {
        return integralRed;
    }

    public void setIntegralRed(int integralRed) {
        this.integralRed = integralRed;
    }

    public int getIntegralUse() {
        return integralUse;
    }

    public void setIntegralUse(int integralUse) {
        this.integralUse = integralUse;
    }

    public int getRatio() {
        return ratio;
    }

    public void setRatio(int ratio) {
        this.ratio = ratio;
    }

    public List<ActivationEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<ActivationEntity> entities) {
        this.entities = entities;
    }
}
