package cn.antke.ezy.network.entities;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by zww on 2017/6/19.
 */

public class IntegralGivePageEntity {

    @SerializedName("total_page")
    private int totalPage;
    @SerializedName("integral_list")
    private List<IntegralGiveEntity> entities;

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List<IntegralGiveEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<IntegralGiveEntity> entities) {
        this.entities = entities;
    }
}
