package cn.antke.ezy.network.entities;

import com.google.gson.annotations.SerializedName;

/**
 * Created by zww on 2017/6/16.
 */

public class BondExchangeEntity {

    @SerializedName("bond")
    private String bond;
    @SerializedName("exchange_bond")
    private String exchangeBond;
    @SerializedName("mulIntegral")
    private String mulIntegral;
    @SerializedName("ratio")
    private String ratio;

    public String getBond() {
        return bond;
    }

    public void setBond(String bond) {
        this.bond = bond;
    }

    public String getExchangeBond() {
        return exchangeBond;
    }

    public void setExchangeBond(String exchangeBond) {
        this.exchangeBond = exchangeBond;
    }

    public String getMulIntegral() {
        return mulIntegral;
    }

    public void setMulIntegral(String mulIntegral) {
        this.mulIntegral = mulIntegral;
    }

    public String getRatio() {
        return ratio;
    }

    public void setRatio(String ratio) {
        this.ratio = ratio;
    }
}
